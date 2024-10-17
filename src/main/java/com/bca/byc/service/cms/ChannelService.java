package com.bca.byc.service.cms;

import com.bca.byc.entity.Channel;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.InvalidFileTypeImageException;
import com.bca.byc.model.*;
import com.bca.byc.response.ResultPageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ChannelService {

    ResultPageResponseDTO<ChanelIndexResponse> listDataChanelIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ChanelDetailResponse findDataById(String id) throws BadRequestException;

    void saveData(Channel data, MultipartFile logo) throws IOException, InvalidFileTypeImageException;

    void updateData(String id, ChannelCreateUpdateRequest dto) throws IOException, InvalidFileTypeImageException;

    void updateStatusChannel(String id);

    void deleteData(String id) throws BadRequestException;

    ResultPageResponseDTO<ChanelListContentResponse<Long>> listDataContentChannel(Integer pages, Integer limit, String sortBy, String direction, String keyword, String channelId);
}

