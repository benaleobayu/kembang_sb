package com.bca.byc.service;

import com.bca.byc.model.ChangeStatusRequest;
import com.bca.byc.model.ReportCommentDetailResponse;
import com.bca.byc.model.ReportCommentIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.time.LocalDate;

public interface ReportCommentService {
    ResultPageResponseDTO<ReportCommentIndexResponse> listDataReportComment(Integer pages, Integer limit, String sortBy, String direction, String keyword, LocalDate startDate, LocalDate endDate, String reportStatus, String reportType);

    ReportCommentDetailResponse findDataById(String id);

}
