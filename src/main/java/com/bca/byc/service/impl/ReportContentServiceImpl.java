package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.model.ReportContentDetailResponse;
import com.bca.byc.model.ReportContentIndexResponse;
import com.bca.byc.model.projection.ReportContentIndexProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.ReportRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReportContentService;
import com.bca.byc.util.helper.Formatter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
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

        Page<ReportContentIndexProjection> pageResult = reportRepository.getDataReportContent(set.keyword(), set.pageable());
        List<ReportContentIndexResponse> dtos = pageResult.stream().map((c) -> new ReportContentIndexResponse(
                c.getId(),
                GlobalConverter.convertListToArray(c.getHighlight()),
                GlobalConverter.getParseImage(c.getThumbnail(), baseUrl),
                c.getDescription(),
                c.getTags(),
                c.getCreator(),
                c.getStatusReport(),
                c.getTotalReport(),
                Formatter.formatterAppsWithSeconds(c.getLastReportAt())
        )).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ReportContentDetailResponse findDataById(String id) {
        return null;
    }
}
