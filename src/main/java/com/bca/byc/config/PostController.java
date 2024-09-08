package com.bca.byc.config;

import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.PostService;
import com.bca.byc.util.FileUploadHelper;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
@Tag(name = "Apps Post API")
public class PostController {

    private final PostService postService;
    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    private FileUploadHelper fileUploadHelper;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> createPost(
            @RequestPart(value = "post", required = false) PostCreateUpdateRequest dto,
            @RequestPart("files") List<MultipartFile> files) {

        String email = ContextPrincipal.getPrincipal();

        // Handle multiple file uploads and set content and type
        List<String> filePaths = new ArrayList<>();
        String fileType = null; // To track the type of the files (image or video)

        try {
            for (MultipartFile file : files) {
                String filePath = FileUploadHelper.saveFile(file, UPLOAD_DIR);
                filePaths.add(filePath);
                String contentType = file.getContentType();
                if (contentType != null) {
                    if (contentType.startsWith("image/")) {
                        fileType = "image";
                    } else if (contentType.startsWith("video/")) {
                        fileType = "video";
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while uploading files", e);
        }
        String fileNames = filePaths.stream()
                .map(filePath -> "\"" + filePath + "\"")  // Add quotes around each file path
                .collect(Collectors.joining(",", "[", "]"));  // Join with commas and wrap in square brackets

        dto.setContent(fileNames);
        dto.setType(fileType);  // "image" or "video"
        try {
            postService.save(email, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Post created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // READ (Get a post by ID)
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
    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponse> updatePost(
            @PathVariable Long id,
            @RequestPart("post") PostCreateUpdateRequest post,
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

        post.setContent(String.join(",", filePaths));
        postService.update(id, post);

        return ResponseEntity.ok(new ApiResponse(true, "Post updated successfully"));
    }

    // DELETE Post
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


//    @PostMapping
//    public ResponseEntity<String> uploadPost(
//            @RequestParam("file")MultipartFile file
//            ) throws IOException {
//        String uploadContent = postService.uploadContent(file);
//        return ResponseEntity.status(HttpStatus.OK).body(uploadContent);
//    }
//
//    @GetMapping("/{fileName}")
//    public ResponseEntity<String> downloadPost(
//            @PathVariable String fileName) throws IOException {
//        byte[] postContent = postService.downloadContent(fileName);
//        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(new String(postContent));
//    }

}
