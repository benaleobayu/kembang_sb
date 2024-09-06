package com.bca.byc.service.impl;

import com.bca.byc.converter.PostDTOConverter;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostDTOConverter converter;

    @Override
    public void init() {
//        try {
//            Path rootDir = Paths.get(UPLOAD_DIR);
//            if (!Files.exists(rootDir)) {
//                Files.createDirectories(rootDir);
//                System.out.println("Directory created: " + rootDir.toAbsolutePath());
//            } else {
//                System.out.println("Directory already exists: " + rootDir.toAbsolutePath());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Error creating directory: " + UPLOAD_DIR, e);
//        }
    }

    @Override
    public void save(PostCreateUpdateRequest dto) throws Exception {
        Post post = new Post();

        post.setName(dto.getName());
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setContent(dto.getContent());
        post.setType(dto.getType());

        postRepository.save(post);
    }


    @Override
    public PostDetailResponse findById(Long id) throws Exception {
        Post data = postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public void update(Long id, PostCreateUpdateRequest post) throws Exception {

        Post data = postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        converter.convertToUpdateRequest(data, post);

        data.setUpdatedAt(LocalDateTime.now());

        postRepository.save(data);
    }

    @Override
    public void deleteData(Long id) throws Exception {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found");
        } else {
            postRepository.deleteById(id);
        }
    }

//    @Override
//    public String uploadContent(MultipartFile fileName) throws IOException {
//
//        Post post = postRepository.save(Post.builder()
//                .name(fileName.getOriginalFilename())
//                .type(fileName.getContentType())
//                .content(ImageUtil.compressImage(fileName.getBytes()))
//                .build());
//
//        if (post != null){
//            return "fileName uploaded successfully: " + fileName.getOriginalFilename();
//        }
//
//        return null;
//    }
//
//    @Override
//    public byte[] downloadContent(String fileName) throws IOException {
//        Optional<Post> dbImageData = postRepository.findByName(fileName);
//        byte[] content = ImageUtil.decompressImage(dbImageData.get().getContent());
//        return content;
//    }


}
