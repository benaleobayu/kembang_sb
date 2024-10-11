package com.bca.byc.controller;


import com.bca.byc.entity.Channel;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AdminContentCreateUpdateRequest;
import com.bca.byc.model.AdminContentDetailResponse;
import com.bca.byc.model.AdminContentIndexResponse;
import com.bca.byc.model.attribute.PostContentRequest;
import com.bca.byc.repository.ChannelRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.TagRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.AdminContentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.bca.byc.util.FileUploadHelper.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AdminContentController.urlRoute)
@Tag(name = "Admin Content API")
@SecurityRequirement(name = "Authorization")
public class AdminContentController {

    static final String urlRoute = "/cms/v1/content";
    private final AdminContentService service;
    private final TagRepository tagRepository;
    private final ChannelRepository channelRepository;
    private final PostRepository postRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    static final String VIDEO_PATH = "/post/admin-content/";
    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @GetMapping
    public ResponseEntity<PaginationCmsResponse<ResultPageResponseDTO<AdminContentIndexResponse>>> listDataAdminContentIndex(
            @RequestParam(name = "pages", required = false, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = false, defaultValue = "LikesCount") String sortBy,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "keyword", required = false) String keyword) {
        // response true
        log.info("GET " + urlRoute + " endpoint hit");
        return ResponseEntity.ok().body(new PaginationCmsResponse<>(true, "Success get list post content", service.listDataAdminContentIndex(pages, limit, sortBy, direction, keyword)));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        log.info("GET " + urlRoute + "/{id} endpoint hit");
        try {
            AdminContentDetailResponse item = service.findDataById(id);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Successfully found post content", item));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> create(
            @ModelAttribute(value = "files") List<MultipartFile> files,
            @ModelAttribute(value = "thumbnail", binding = false) MultipartFile thumbnail,
            @ModelAttribute(value = "content") String contentString,
            @ModelAttribute("channelId") String channelId,
            @ModelAttribute("highlight") List<String> highlight,
            @ModelAttribute("description") String description,
            @ModelAttribute("tags") List<String> tags,
            @ModelAttribute("status") Boolean status,
            @ModelAttribute(value = "promotionStatus", binding = false) String promotionStatus,
            @ModelAttribute(value = "promotedAt", binding = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime promotedAt,
            @ModelAttribute(value = "promotedUntil", binding = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime promotedUntil,
            @ModelAttribute(value = "postAt", binding = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime postAt
    ) {
        log.info("POST " + urlRoute + " endpoint hit");

        try {
            List<PostContent> contentList = new ArrayList<>();

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
                    PostContent postContent = processFile(file, thumbnail, contentRequest, i, UPLOAD_DIR, null);
                    postContent.setContent(m3u8Path.replaceAll(UPLOAD_DIR, "uploads"));
                    postContent.setMediaUrl(videoPath.replaceAll(UPLOAD_DIR, "uploads"));
                    contentList.add(postContent);
                } else {
                    // Handle other file types as necessary
                    PostContent postContent = processFile(file, null, contentRequest, i, UPLOAD_DIR, null);
                    contentList.add(postContent);
                }
            }
            Post post = new Post();
            Post newPost = parseToPost(channelId, highlight, description, tags, status, promotionStatus, promotedAt, promotedUntil, postAt, channelRepository, tagRepository, post);
            service.saveData(contentList, newPost);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created post content"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error processing files"));
        }
    }

    @PutMapping(value = "{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> update(
            @PathVariable("postId") String id,
            @ModelAttribute(value = "files") List<MultipartFile> files,
            @ModelAttribute(value = "thumbnail", binding = false) MultipartFile thumbnail,
            @ModelAttribute(value = "content") String contentString,
            @ModelAttribute("channelId") String channelId,
            @ModelAttribute("highlight") List<String> highlight,
            @ModelAttribute("description") String description,
            @ModelAttribute("tags") List<String> tags,
            @ModelAttribute("status") Boolean status,
            @ModelAttribute(value = "promotionStatus") String promotionStatus,
            @ModelAttribute(value = "promotedAt", binding = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime promotedAt,
            @ModelAttribute(value = "promotedUntil", binding = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime promotedUntil,
            @ModelAttribute(value = "postAt", binding = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime postAt
    ) {
        log.info("PUT " + urlRoute + "/{id} endpoint hit");
        try {
            Post existingPost = HandlerRepository.getEntityBySecureId(id, postRepository, "Post not found");
            for (PostContent existingContent : existingPost.getPostContents()) {
                String filePath = existingContent.getContent();
                deleteFile(filePath, UPLOAD_DIR);
            }

            List<PostContent> contentList = new ArrayList<>();

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
                    PostContent postContent = processFile(file, thumbnail, contentRequest, i, UPLOAD_DIR, null);
                    postContent.setContent(m3u8Path.replaceAll(UPLOAD_DIR, "uploads"));
                    postContent.setMediaUrl(videoPath.replaceAll(UPLOAD_DIR, "uploads"));
                    contentList.add(postContent);
                } else {
                    // Handle other file types as necessary
                    PostContent postContent = processFile(file, null, contentRequest, i, UPLOAD_DIR, null);
                    contentList.add(postContent);
                }
            }
            Post updatePost = parseToPost(channelId, highlight, description, tags, status, promotionStatus, promotedAt, promotedUntil, postAt, channelRepository, tagRepository, existingPost);
            service.updateData(updatePost);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated post content"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted post content"));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // ----- Helper functions -----
    private Post parseToPost(String channelId,
                             List<String> highlight,
                             String description,
                             List<String> taglist,
                             Boolean status,
                             String promotionStatus,
                             LocalDateTime promotedAt,
                             LocalDateTime promotedUntil,
                             LocalDateTime postAt,
                             ChannelRepository channelRepository,
                             TagRepository tagRepository,
                             Post post
                             ) {
        Channel c = HandlerRepository.getEntityBySecureId(channelId, channelRepository, "channel not found");
        post.setChannel(c);

        List<String> highlightList = null;
        if (highlight != null) {
            highlightList = highlight.stream()
                    .map(s -> s.replaceAll("[^a-zA-Z0-9]", "").trim()).toList();
        }
        assert highlightList != null;
        post.setHighlight(String.join(", ", highlightList));

        post.setDescription(description);
        // Set list of Tags
        Set<com.bca.byc.entity.Tag> tags = new HashSet<>();
        if (taglist != null) {
            for (String tagName : taglist) {
                Optional<com.bca.byc.entity.Tag> tag = tagRepository.findByName(tagName);
                tag.ifPresentOrElse(tags::add, () -> {
                    com.bca.byc.entity.Tag newTag = new com.bca.byc.entity.Tag();
                    newTag.setName(tagName);
                    tags.add(tagRepository.save(newTag));
                });
            }
        }
        post.setTags(tags);
        post.setIsActive(status);
        post.setPromotedStatus(promotionStatus != null ? promotionStatus : "False");
        post.setPromotedAt(promotedAt);
        post.setPromotedUntil(promotedUntil);
        post.setPostAt(postAt != null ? postAt : LocalDateTime.now());
        return post;
    }
}
