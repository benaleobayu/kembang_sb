package com.bca.byc.service.cms;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ChanelCreateUpdateRequest;
import com.bca.byc.model.ChanelDetailResponse;
import com.bca.byc.model.ChanelIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

public interface ChannelService {

    ResultPageResponseDTO<ChanelIndexResponse> listDataChanelIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ChanelDetailResponse findDataById(String id) throws BadRequestException;

    void saveData(@Valid ChanelCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid ChanelCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}

