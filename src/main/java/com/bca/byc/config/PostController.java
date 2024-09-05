package com.bca.byc.config;

import com.bca.byc.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.Multipart;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/post")
@Tag(name = "Apps Post API")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<String> uploadPost(
            @RequestParam("file")MultipartFile file
            ) throws IOException {
        String uploadContent = postService.uploadContent(file);
        return ResponseEntity.status(HttpStatus.OK).body(uploadContent);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<String> downloadPost(
            @PathVariable String fileName) throws IOException {
        byte[] postContent = postService.downloadContent(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(new String(postContent));
    }

 }
