package com.bca.byc.service;

import com.bca.byc.model.ReportUserDetailResponse;
import com.bca.byc.model.ReportUserIndexResponse;
import com.bca.byc.model.ReportUserStatusRequest;
import com.bca.byc.response.ResultPageResponseDTO;

import java.time.LocalDate;

public interface ReportUserService {

    ResultPageResponseDTO<ReportUserIndexResponse> listDataReportUser(Integer pages, Integer limit, String sortBy, String direction, String keyword, LocalDate startDate, LocalDate endDate);

    ReportUserDetailResponse findDataById(String id);

    String updateStatusReportUser(ReportUserStatusRequest id);
}
