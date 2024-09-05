package com.bca.byc.service.impl;

import com.bca.byc.entity.Post;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.service.PostService;
import com.bca.byc.util.ImageUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public String uploadContent(MultipartFile fileName) throws IOException {

        Post post = postRepository.save(Post.builder()
                .name(fileName.getOriginalFilename())
                .type(fileName.getContentType())
                .content(ImageUtil.compressImage(fileName.getBytes()))
                .build());

        if (post != null){
            return "fileName uploaded successfully: " + fileName.getOriginalFilename();
        }

        return null;
    }

    @Override
    public byte[] downloadContent(String fileName) throws IOException {
        Optional<Post> dbImageData = postRepository.findByName(fileName);
        byte[] content = ImageUtil.decompressImage(dbImageData.get().getContent());
        return content;
    }


}
