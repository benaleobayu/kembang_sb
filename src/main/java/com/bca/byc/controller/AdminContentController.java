package com.bca.byc.controller;


import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Channel;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.PostContent;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AdminContentDetailResponse;
import com.bca.byc.model.AdminContentIndexResponse;
import com.bca.byc.model.ContentStringRequest;
import com.bca.byc.model.attribute.PostContentRequest;
import com.bca.byc.repository.ChannelRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.TagRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.PaginationCmsResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.GlobalAttributeService;
import com.bca.byc.service.cms.AdminContentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

import static com.bca.byc.util.FileUploadHelper.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(AdminContentController.urlRoute)
@Tag(name = "Admin Content API")
@SecurityRequirement(name = "Authorization")
public class AdminContentController {

    static final String urlRoute = "/cms/v1/content";
    static final String VIDEO_PATH = "/post/admin-content/";
    private final AdminContentService service;
    private final TagRepository tagRepository;
    private final ChannelRepository channelRepository;
    private final PostRepository postRepository;
    private final GlobalAttributeService globalAttributeService;
    private final ObjectMapper objectMapper = new ObjectMapper();
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
            @RequestPart(value = "files") List<MultipartFile> files,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam("channelId") String channelId,
            @RequestParam("highlight") List<String> highlight,
            @RequestParam("description") String description,
            @RequestParam("tags") List<String> tags,
            @RequestParam("isPublish") Boolean isPublish,
            @RequestParam("isSchedule") Boolean isSchedule,
            @RequestParam("totalFile") Integer totalFile,
            @RequestParam(value = "promotedActive", required = false) Boolean promotedActive,
            @RequestParam(value = "promotedAt", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime promotedAt,
            @RequestParam(value = "promotedUntil", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime promotedUntil,
            @RequestParam(value = "postAt", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime postAt
    ) {
        log.info("POST " + urlRoute + " endpoint hit");

        try {
            Post post = new Post();
            List<PostContent> contentList = new ArrayList<>();
            if (isSchedule) {
                postAt = LocalDateTime.now();
            }

            List<ContentStringRequest> contentString = new ArrayList<>();
            for (int i = 0; i < totalFile; i++) {
                String name = RandomStringUtils.randomAlphanumeric(8);
                contentString.add(new ContentStringRequest(name));
            }

            List<PostContentRequest> contentRequests = new ArrayList<>();
            for (ContentStringRequest contentStringRequest : contentString) {
                PostContentRequest postContentRequest = new PostContentRequest();
                postContentRequest.setOriginalName(contentStringRequest.getOriginalName());
                contentRequests.add(postContentRequest);
            }
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
            Post newPost = parseToPost(channelId, highlight, description, tags, isPublish, isSchedule, promotedActive, promotedAt, promotedUntil, postAt, channelRepository, tagRepository, post);
            service.saveData(contentList, newPost);
            return ResponseEntity.created(URI.create(urlRoute))
                    .body(new ApiResponse(true, "Successfully created post content"));
        } catch (BadRequestException | IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PutMapping(value = "{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> update(
            @PathVariable("postId") String id,
            @RequestPart(value = "files") List<MultipartFile> files,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "content") String contentString,
            @RequestParam("channelId") String channelId,
            @RequestParam("highlight") List<String> highlight,
            @RequestParam("description") String description,
            @RequestParam("tags") List<String> tags,
            @RequestParam("isPublish") Boolean isPublish,
            @RequestParam("isSchedule") Boolean isSchedule,
            @RequestParam(value = "promotedActive") Boolean promotedActive,
            @RequestParam(value = "promotedAt", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime promotedAt,
            @RequestParam(value = "promotedUntil", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime promotedUntil,
            @RequestParam(value = "postAt", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime postAt
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
            Post updatePost = HandlerRepository.getEntityBySecureId(id, postRepository, "Post not found");
            parseToPost(channelId, highlight, description, tags, isPublish, isSchedule, promotedActive, promotedAt, promotedUntil, postAt, channelRepository, tagRepository, existingPost);
            service.updateData(updatePost);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully updated post content"));
        } catch (BadRequestException | IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable("id") String id) {
        log.info("DELETE " + urlRoute + "/{id} endpoint hit");
        try {
            service.deleteData(id);
            return ResponseEntity.ok(new ApiResponse(true, "Successfully deleted post content"));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    // ----- Helper functions -----
    private Post parseToPost(String channelId,
                             List<String> highlight,
                             String description,
                             List<String> taglist,
                             Boolean isPublished,
                             Boolean isScheduled,
                             Boolean promotedActive,
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
                    .map(GlobalConverter::convertTagString).toList();
        }
        assert highlightList != null;
        post.setHighlight(String.join(", ", highlightList));

        post.setDescription(description);
        // Set list of Tags
        Set<com.bca.byc.entity.Tag> tags = new HashSet<>();
        if (taglist != null) {
            for (String tagName : taglist) {
                // Transform the tag name by removing spaces and capitalizing the first letter of each word
                tagName = GlobalConverter.convertTagString(tagName);

                Optional<com.bca.byc.entity.Tag> tag = tagRepository.findByName(tagName);
                String finalTagName = tagName;
                tag.ifPresentOrElse(tags::add, () -> {
                    com.bca.byc.entity.Tag newTag = new com.bca.byc.entity.Tag();
                    newTag.setName(finalTagName);
                    tags.add(tagRepository.save(newTag));
                });
            }
        }

        post.setTags(tags);
        post.setIsActive(isPublished);
        post.setPromotedActive(promotedActive ? promotedActive : false);
        post.setPromotedStatus(promotedActive ? "SCHEDULED" : "NOT_DEFINED");
        post.setPromotedAt(promotedAt);
        post.setPromotedUntil(promotedUntil);
        post.setPostAt(postAt != null ? postAt : LocalDateTime.now());
        return post;
    }
}
