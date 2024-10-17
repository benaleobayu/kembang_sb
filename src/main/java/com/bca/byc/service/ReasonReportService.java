package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ReasonReportCreateUpdateRequest;
import com.bca.byc.model.ReasonReportDetailResponse;
import com.bca.byc.model.ReasonReportIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.IOException;
import java.util.List;

public interface ReasonReportService {

    // public
    List<ReasonReportIndexResponse> findAllData();

    // cms
    ResultPageResponseDTO<ReasonReportIndexResponse> listDataReasonReportIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ReasonReportDetailResponse findDataById(String id) throws BadRequestException;

    void saveData(@Valid ReasonReportCreateUpdateRequest dto) throws BadRequestException, IOException;

    void updateData(String id, @Valid ReasonReportCreateUpdateRequest dto) throws BadRequestException, IOException;

    void deleteData(String id) throws BadRequestException;
}
