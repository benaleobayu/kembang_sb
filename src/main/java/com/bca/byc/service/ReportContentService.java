package com.bca.byc.service;

import com.bca.byc.model.ReportContentDetailResponse;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.time.LocalDate;

public interface ReportContentService {

    ResultPageResponseDTO<ReportContentIndexResponse> listDataReportContent(Integer pages,
                                                                            Integer limit,
                                                                            String sortBy,
                                                                            String direction,
                                                                            String keyword,
                                                                            LocalDate startDate,
                                                                            LocalDate endDate,
                                                                            String reportStatus,
                                                                            String reportType);

    ReportContentDetailResponse findDataById(String id);

}
