package com.bca.byc.controller;

import com.bca.byc.entity.PostContent;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.model.attribute.PostContentRequest;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.PostService;
import com.bca.byc.util.FileUploadHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/post")
@Tag(name = "Apps Post API")
@SecurityRequirement(name = "Authorization")
public class PostController {

    private final PostService postService;
    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Operation(summary = "Get list post", description = "Get list post")
    @GetMapping
    public ResponseEntity<PaginationResponse<ResultPageResponseDTO<PostDetailResponse>>> listFollowUser(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "description") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "categories", required = false, defaultValue = "popular") String categories) {
        // response true
        String email = ContextPrincipal.getPrincipal();

        try {
            return ResponseEntity.ok().body(new PaginationResponse<>(true, "Success get list post", postService.listData(email, pages, limit, sortBy, direction, keyword, categories)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new PaginationResponse<>(false, e.getMessage(), null));
        }
    }


    @Operation(summary = "Create post", description = "Create post")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<ApiResponse> createPost(
            @RequestPart(value = "post", required = false) PostCreateUpdateRequest dto,
            @RequestPart(value = "content", required = false) PostContentRequest content,
            @RequestPart("files") List<MultipartFile> files) {

        String email = ContextPrincipal.getPrincipal();

        // Handle multiple file uploads and set content and type
        List<PostContent> contentList = new ArrayList<>();
        try {
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
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

                    PostContent postContent = new PostContent();
                    postContent.setContent(filePath.replaceAll("src/main/resources/static/", "/"));
                    postContent.setType(fileType);

                    contentList.add(postContent);
                }
            }

            postService.save(email, dto, contentList);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Post created successfully"));

        } catch (IOFileUploadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        } catch (IOException e) {
            throw new RuntimeException(e);
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

}
