package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.ChanelDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreeChannel;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Channel;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.InvalidFileTypeImageException;
import com.bca.byc.model.*;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.ChannelRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.ChannelService;
import com.bca.byc.util.FileUploadHelper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.util.FileUploadHelper.saveFile;

@Service
@AllArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final AppAdminRepository adminRepository;

    private final ChannelRepository repository;
    private final ChanelDTOConverter converter;

    private final PostRepository postRepository;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;
    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public ResultPageResponseDTO<ChanelIndexResponse> listDataChanelIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Channel> pageResult = repository.findByNameLikeIgnoreCaseOrderByOrders(set.keyword(), set.pageable());
        List<ChanelIndexResponse> dtos = pageResult.stream().map((c) -> {
            ChanelIndexResponse dto = converter.convertToIndexResponse(c, baseUrl);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ChanelDetailResponse findDataById(String id) throws BadRequestException {
        Channel data = HandlerRepository.getEntityBySecureId(id, repository, "Channel not found");

        return converter.convertToDetailResponse(data, baseUrl);
    }

    @Override
    @Transactional
    public void saveData(Channel data, MultipartFile logo) throws IOException , InvalidFileTypeImageException {
        FileUploadHelper.validateFileTypeImage(logo);

        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        data.setCreatedBy(admin);
        data.setCreatedAt(LocalDateTime.now());
        data.setUpdatedAt(LocalDateTime.now());
        String newLogo = saveFile(logo, UPLOAD_DIR + "/images/channel");
        data.setLogo(GlobalConverter.replaceImagePath(newLogo));
        repository.save(data);
    }

    @Override
    @Transactional
    public void updateData(String id, ChannelCreateUpdateRequest dto) throws IOException, InvalidFileTypeImageException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Channel data = HandlerRepository.getEntityBySecureId(id, repository, "Channel not found");
        TreeChannel.updatedChannel(dto, data, admin);

        if (dto.logo() != null ) {
            FileUploadHelper.validateFileTypeImage(dto.logo());
            String oldLogo = data.getLogo();
            String newLogo = saveFile(dto.logo(), UPLOAD_DIR + "/images/channel");
            data.setLogo(GlobalConverter.replaceImagePath(newLogo));
            if (!oldLogo.equals(newLogo)) {
                FileUploadHelper.deleteFile(oldLogo, UPLOAD_DIR);
            }
        }
        repository.save(data);

    }

    @Override
    public void updateStatusChannel(String id) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Channel data = getDataEntity(id);
        data.setIsActive(!data.getIsActive());
        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        Channel data = HandlerRepository.getEntityBySecureId(id, repository, "Channel not found");
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("Channel not found");
        } else {
            repository.deleteById(data.getId());
        }
    }

    @Override
    public ResultPageResponseDTO<ChanelListContentResponse<Long>> listDataContentChannel(Integer pages, Integer limit, String sortBy, String direction, String keyword, String channelId) {
        Channel channel = getDataEntity(channelId);
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Post> pageResult = postRepository.findPostOnChannel(set.keyword(), set.pageable(), channel.getId());
        List<ChanelListContentResponse<Long>> dtos = pageResult.stream().map((c) -> {
            ChanelListContentResponse<Long> dto = converter.convertListContentResponse(c, baseUrl);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    // --- Helper ---
    private Channel getDataEntity(String id) {
        return HandlerRepository.getIdBySecureId(
                id,
                repository::findBySecureId,
                projection -> repository.findById(projection.getId()),
                "Channel not found"
        );
    }
}
