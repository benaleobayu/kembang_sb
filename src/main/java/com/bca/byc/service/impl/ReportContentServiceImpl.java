package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.model.ReportContentDetailResponse;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.model.projection.ReportContentProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.ReportRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReportContentService;
import com.bca.byc.util.helper.Formatter;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportContentServiceImpl implements ReportContentService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public ResultPageResponseDTO<ReportContentIndexResponse> listDataReportContent(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<ReportContentProjection> pageResult = reportRepository.getDataReportContent(null, set.keyword(), set.pageable());
        List<ReportContentIndexResponse> dtos = pageResult.stream().map((c) -> new ReportContentIndexResponse(
                c.getId(),
                GlobalConverter.convertListToArray(c.getHighlight()),
                GlobalConverter.getParseImage(c.getThumbnail(), baseUrl),
                c.getPostDescription(),
                c.getTags(),
                c.getCreator(),
                c.getStatusReport(),
                c.getTotalReport(),
                Formatter.formatLocalDateTime(c.getLastReportAt())
        )).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ReportContentDetailResponse findDataById(String id) {
        Page<ReportContentProjection> pageResult = reportRepository.getDataReportContent(id, null, null);
        if (pageResult.isEmpty()) {
            throw new EntityNotFoundException("Report content not found for ID: " + id);
        }

        ReportContentProjection data = pageResult.getContent().get(0);

        List<Map<String, String>> postContent = data.getPost().getPostContents().stream()
                .map(pc -> {
                    Map<String, String> contentMap = new HashMap<>();
                    contentMap.put("content", GlobalConverter.getParseImage(pc.getContent(), baseUrl));
                    return contentMap;
                })
                .collect(Collectors.toList());

        return new ReportContentDetailResponse(
                data.getId(),
                data.getCreator(),
                Formatter.formatLocalDateTime(data.getLastReportAt()),
                data.getChannelName(),
                data.getPost().getIsActive(),
                data.getPostDescription(),
                GlobalConverter.convertListToArray(data.getHighlight()),
                GlobalConverter.convertListToArray(data.getTags()),
                postContent
        );
    }
}
