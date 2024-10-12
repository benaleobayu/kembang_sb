package com.bca.byc.service.impl;

import com.bca.byc.model.ReportContentDetailResponse;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReportContentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportContentServiceImpl implements ReportContentService {
    @Override
    public ResultPageResponseDTO<ReportContentIndexResponse> listDataReportContent(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        return null;
    }

    @Override
    public ReportContentDetailResponse findDataById(String id) {
        return null;
    }
}
