package com.bca.byc.service;

import com.bca.byc.model.ReportRequest;

public interface RepostService {
    String SendReport(ReportRequest dto) throws Exception;
}
