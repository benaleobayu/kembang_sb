package com.bca.byc.service;

import com.bca.byc.model.ReportContentDetailResponse;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface ReportContentService {
    ResultPageResponseDTO<ReportContentIndexResponse> listDataReportContent(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ReportContentDetailResponse findDataById(String id);
}
