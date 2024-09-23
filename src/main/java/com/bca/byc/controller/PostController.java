package com.bca.byc.controller;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.PostContent;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.attribute.PostContentRequest;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.AppPaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.PostService;
import com.bca.byc.util.FileUploadHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
@Tag(name = "Apps Post API")
@SecurityRequirement(name = "Authorization")
public class PostController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PostService postService;
    private final AppUserRepository userRepository;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Operation(summary = "Get list post", description = "Get list post")
    @GetMapping
    public ResponseEntity<AppPaginationResponse<ResultPageResponseDTO<PostDetailResponse>>> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        try {
            return ResponseEntity.ok().body(new AppPaginationResponse<>(true, "Success get list post", postService.listData(email, pages, limit, sortBy, direction, keyword)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AppPaginationResponse<>(false, e.getMessage(), null));
        }
    }


    @Operation(summary = "Create Post", description = "Create Post")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> createPost(
            @RequestPart(value = "post", required = false) String postString,
            @RequestPart(value = "content", required = false) String contentString,
            @RequestPart("files") List<MultipartFile> files) {

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
                    new TypeReference<List<PostContentRequest>>() {});

            // Validate if contentRequests and files match in size
            if (files.size() != contentRequests.size()) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Mismatch between files and content data"));
            }

            // Process files and content
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                PostContentRequest contentRequest = contentRequests.get(i);
                PostContent postContent = processFile(file, contentRequest, i);
                contentList.add(postContent);
            }
            // log debug TODO deleted after test
            System.out.println("Post String: " + postString);
            System.out.println("Content String: " + contentString);

            // Save post and its content
            postService.save(email, dto, contentList);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Post created successfully"));

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid JSON format: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // READ (Get a post by ID)
    @Operation(summary = "Get a post by ID", description = "Get a post by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPost(@PathVariable Long id) {
        try {
            PostDetailResponse item = postService.findById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully found post", item));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    // UPDATE Post
    @Operation(summary = "Update post", description = "Update post")
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse> updatePost(
            @PathVariable Long id,
            @RequestPart(value = "post") PostCreateUpdateRequest post,
            @RequestPart(value = "content", required = false) PostContentRequest content,
            @RequestPart("files") List<MultipartFile> files) throws Exception {

        // Similar file upload handling as in createPost
        List<String> filePaths = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String filePath = FileUploadHelper.saveFile(file, UPLOAD_DIR);
                filePaths.add(filePath);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        content.setContent(String.join(",", filePaths));
        postService.update(id, post);

        return ResponseEntity.ok(new ApiResponse(true, "Post updated successfully"));
    }

    // DELETE Post
    @Operation(summary = "Delete post", description = "Delete post")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long id) {
        try {
            PostDetailResponse post = postService.findById(id);

            String content = post.getContent();
            if (content != null && !content.isEmpty()) {
                String[] filePaths = content.split(",");
                for (String filePath : filePaths) {
                    String getFilePaths = filePath.replaceAll("\"", "").replaceAll("[\\[\\]]", "");
                    FileUploadHelper.deleteFile(getFilePaths.trim());
                }
            }

            postService.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Post deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }




    // ----------------------------------------- method ---------------------------------------------
    private PostContent processFile(MultipartFile file, PostContentRequest contentRequest, int index) throws IOException {
        // Simpan file ke direktori tertentu
        String filePath = FileUploadHelper.saveFile(file, UPLOAD_DIR);
        String contentType = file.getContentType();
        String fileType = null;

        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                fileType = "image";
            } else if (contentType.startsWith("video/")) {
                fileType = "video";
            }
        }

        // Membuat PostContent dari file dan contentRequest yang sesuai
        PostContent postContent = new PostContent();
        postContent.setIndex(index);
        postContent.setContent(filePath.replaceAll("src/main/resources/static/", "/"));
        postContent.setType(fileType);
        postContent.setOriginalName(contentRequest.getOriginalName());

        // Menangani tagUserIds
        Set<AppUser> appUsers = new HashSet<>();
        if (contentRequest.getTagUserIds() != null) {
            for (Long userId : contentRequest.getTagUserIds()) {
                AppUser user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found: " + userId));
                appUsers.add(user);
            }
        }
        postContent.setTagUsers(appUsers);

        return postContent;
    }
}
