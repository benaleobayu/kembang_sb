package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.attribute.PostContentRequest;
import com.bca.byc.model.projection.IdSecureIdProjection;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationAppsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.PostService;
import com.bca.byc.util.FileUploadHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.bca.byc.util.FileUploadHelper.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
@Tag(name = "Apps Post API")
@SecurityRequirement(name = "Authorization")
public class PostController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PostService postService;

    private final AppUserRepository userRepository;
    private final PostRepository postRepository;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    private String VIDEO_PATH = "/post/video/";

    @Operation(summary = "Get list post home", description = "Get list post home")
    @GetMapping
    public ResponseEntity<PaginationAppsResponse<ResultPageResponseDTO<PostDetailResponse>>> listDataPostHome(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @Schema(example = "top-picks", description = "top-picks | following | discover")
            @RequestParam(name = "category", required = false, defaultValue = "top-picks") String category) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        return ResponseEntity.ok().body(new PaginationAppsResponse<>(true, "Success get list post", postService.listDataPostHome(email, pages, limit, sortBy, direction, keyword, category)));
    }

    @Operation(summary = "Create Post", description = "Create Post")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> createPost(
            @RequestPart(value = "post", required = false) String postString,
            @RequestPart(value = "content", required = false) String contentString,
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail) {

        String email = ContextPrincipal.getPrincipal();
        List<PostContent> contentList = new ArrayList<>();

        try {
            // Validate postString
            if (postString == null || postString.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Post data is missing"));
            }
            PostCreateUpdateRequest dto = objectMapper.readValue(postString, PostCreateUpdateRequest.class);

            // Validate contentString
            if (contentString == null || contentString.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Content data is missing"));
            }
            List<PostContentRequest> contentRequests = objectMapper.readValue(contentString,
                    new TypeReference<List<PostContentRequest>>() {
                    });

            // Validate if contentRequests and files match in size
            if (files.size() != contentRequests.size()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Mismatch between files and content data"));
            }

            // Process files and content
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                PostContentRequest contentRequest = contentRequests.get(i);

                // Check if the file is a video and convert it if necessary
                if (isVideoFile(file)) {
                    String videoPath = saveFile(file, UPLOAD_DIR + VIDEO_PATH);
                    String m3u8Path = convertVideoToM3U8(videoPath, UPLOAD_DIR, VIDEO_PATH);
                    PostContent postContent = processFile(file, thumbnail, contentRequest, i);
                    postContent.setContent(m3u8Path.replaceAll(UPLOAD_DIR, "uploads"));
                    contentList.add(postContent);
                } else {
                    // Handle other file types as necessary
                    PostContent postContent = processFile(file, null, contentRequest, i);
                    contentList.add(postContent);
                }
            }
            // Save post and its content
            postService.save(email, dto, contentList);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Post created successfully"));

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid JSON format: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    private PostContent createPostContent(PostContentRequest contentRequest, String m3u8Path) {
        PostContent postContent = new PostContent();

        // Set mediaUrl ke jalur M3U8
        postContent.setContent(m3u8Path);

        return postContent; // Mengembalikan objek PostContent yang sudah terisi
    }


    // READ (Get a post by ID)
    @Operation(summary = "Get a post by ID", description = "Get a post by ID")
    @GetMapping("/{secureId}")
    public ResponseEntity<?> getPost(@PathVariable String secureId) {
        try {
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found post", postService.findBySecureId(secureId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // UPDATE Post
    @Operation(summary = "Update post", description = "Update post")
    @PutMapping(value = "/{secureId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse> updatePost(
            @PathVariable String secureId,
            @RequestPart(value = "post") PostCreateUpdateRequest post,
            @RequestPart(value = "content", required = false) PostContentRequest content,
            @RequestPart("files") List<MultipartFile> files) throws Exception {

        // Similar file upload handling as in createPost
        List<String> filePaths = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String filePath = saveFile(file, UPLOAD_DIR);
                filePaths.add(filePath);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        content.setContent(String.join(",", filePaths));
        postService.update(secureId, post);

        return ResponseEntity.ok(new ApiResponse(true, "Post updated successfully"));
    }

    // DELETE Post
    @Operation(summary = "Delete post", description = "Delete post")
    @DeleteMapping("/{secureId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable String secureId) {
        try {
            Post post = postRepository.findBySecureId(secureId)
                    .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            String email = ContextPrincipal.getPrincipal();
            // if user id is not same with user_id user posted
            if (!post.getUser().getEmail().equals(email)) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "You are not authorized to delete this post"));
            }

            String content = post.getPostContents().stream().map(PostContent::getContent).collect(Collectors.joining(","));
            if (content != null && !content.isEmpty()) {
                String[] filePaths = content.split(",");
                for (String filePath : filePaths) {
                    String getFilePaths = filePath.replaceAll("\"", "").replaceAll("[\\[\\]]", "");
                    FileUploadHelper.deleteFile(getFilePaths.trim());
                }
            }

            postService.deleteData(secureId);
            return ResponseEntity.ok(new ApiResponse(true, "Post deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }


    // ----------------------------------------- method ---------------------------------------------
    private PostContent processFile(MultipartFile file, MultipartFile thumbnail, PostContentRequest contentRequest, int index) throws IOException {
        String filePath = saveFile(file, UPLOAD_DIR + "/post/");
        String contentType = file.getContentType();
        String fileType = null;


        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") ||
                    fileName.endsWith(".gif") || fileName.endsWith(".bmp") ||
                    fileName.endsWith(".tiff") || fileName.endsWith(".webp") ||
                    fileName.endsWith(".svg") || fileName.endsWith(".ico")) {
                contentType = "image/webp"; // Default to WEBP
                fileType = "image";
            } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") ||
                    fileName.endsWith(".mkv") || fileName.endsWith(".mov") ||
                    fileName.endsWith(".wmv") || fileName.endsWith(".flv") ||
                    fileName.endsWith(".mpeg") || fileName.endsWith(".3gp")) {
                contentType = "video/mp4"; // Default to MP4
                fileType = "video";
            } else {
                throw new RuntimeException("Unsupported file type: " + fileName);
            }
        }


        // Membuat PostContent dari file dan contentRequest yang sesuai
        PostContent postContent = new PostContent();
        postContent.setIndex(index);
        postContent.setContent(filePath.replaceAll("src/main/resources/static/", "/"));
        postContent.setType(fileType);
        postContent.setOriginalName(contentRequest.getOriginalName());

        if (thumbnail != null) {
            String thumbnailPath = saveFile(thumbnail, UPLOAD_DIR + "/thumbnail/");
            postContent.setThumbnail(thumbnailPath.replaceAll("src/main/resources/static/", "/"));
        }

        // Menangani tagUserIds
        Set<AppUser> appUsers = new HashSet<>();
        if (contentRequest.getTagUserIds() != null) {
            for (String userId : contentRequest.getTagUserIds()) {
                try {
                    UUID.fromString(userId); // Validate UUID
                    IdSecureIdProjection gotId = userRepository.findUserBySecureId(userId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
                    appUsers.add(gotId.toAppUser());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid UUID format: " + userId);
                }

            }
        }
        postContent.setTagUsers(appUsers);

        return postContent;
    }
}
