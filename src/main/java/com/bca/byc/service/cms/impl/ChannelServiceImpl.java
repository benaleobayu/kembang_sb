package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.ChanelDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.converter.parsing.TreeChannel;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Channel;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.InvalidFileTypeImageException;
import com.bca.byc.model.ChannelCreateUpdateRequest;
import com.bca.byc.model.ChanelDetailResponse;
import com.bca.byc.model.ChanelIndexResponse;
import com.bca.byc.repository.ChannelRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.ChannelService;
import com.bca.byc.util.FileUploadHelper;
import com.bca.byc.util.PaginationUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

    private ChannelRepository repository;
    private ChanelDTOConverter converter;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;
    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public ResultPageResponseDTO<ChanelIndexResponse> listDataChanelIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Channel> pageResult = repository.findByNameLikeIgnoreCaseOrderByOrders(keyword, pageable);
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
        FileUploadHelper.validateFileTypeImage(dto.logo());

        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Channel data = HandlerRepository.getEntityBySecureId(id, repository, "Channel not found");
        TreeChannel.updatedChannel(dto, data, admin);

        String oldLogo = data.getLogo();
        String newLogo = saveFile(dto.logo(), UPLOAD_DIR + "/images/channel");
        data.setLogo(GlobalConverter.replaceImagePath(newLogo));
        if (!oldLogo.equals(newLogo)) {
            FileUploadHelper.deleteFile(oldLogo);
        }
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
}
