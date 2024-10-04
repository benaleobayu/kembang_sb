package com.bca.byc.service;

import com.bca.byc.model.ReportRequest;

public interface RepostService {
    void SendReport(ReportRequest dto) throws Exception;
}
