package com.bca.byc.service;

import com.bca.byc.model.ChangeStatusRequest;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.model.ReportRequest;
import com.bca.byc.response.ResultPageResponseDTO;

public interface ReportService {

    String SendReport(ReportRequest dto) throws Exception;

    String SendReportCommentReply(ReportRequest dto);

    ResultPageResponseDTO<ReportContentIndexResponse> listReportOnDetail(
            Integer pages, Integer limit, String sortBy, String direction, String keyword, String reportId, String detailOf);

    void updateReportStatus(ChangeStatusRequest dto);
}
