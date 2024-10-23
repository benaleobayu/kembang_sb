package com.bca.byc.service.impl;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Report;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ReportUserDetailResponse;
import com.bca.byc.model.ReportUserIndexResponse;
import com.bca.byc.model.ReportUserStatusRequest;
import com.bca.byc.model.projection.ReportUserIndexProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.ReportRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReportUserService;
import com.bca.byc.service.UserProjectionService;
import com.bca.byc.util.PaginationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportUserServiceImpl implements ReportUserService {

    private final AppUserRepository userRepository;
    private final ReportRepository reportRepository;
    private final UserProjectionService userProjectionService;

    @Override
    public ResultPageResponseDTO<ReportUserIndexResponse> listDataReportUser(
            Integer pages, Integer limit, String sortBy, String direction,
            String keyword, LocalDate startDate, LocalDate endDate) {

        ListOfFilterPagination filter = new ListOfFilterPagination(keyword, startDate, endDate);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        // Set date
        LocalDateTime start = (startDate == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null) ? LocalDateTime.now() : endDate.atTime(23, 59, 59);

        Page<ReportUserIndexProjection> pageResult = reportRepository.getDataReportUserIndex(null, set.keyword(), set.pageable(), start, end);

        // Retrieve all user names and phones for reported IDs
        List<Long> reportedIds = pageResult.stream()
                .map(ReportUserIndexProjection::getReportedId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, String> userNames = userProjectionService.findUserNameMaps(reportedIds);
        Map<Long, String> userPhones = userProjectionService.findUserPhoneMaps(reportedIds);

        List<ReportUserIndexResponse> dtos = pageResult.stream()
                .map(data -> {
                    Long reportedId = data.getReportedId();
                    String name = userNames.get(reportedId); // Directly get the name
                    String phone = userPhones.get(reportedId); // Directly get the phone

                    return new ReportUserIndexResponse(
                            data.getId(),
                            data.getIndex(),
                            name,
                            data.getReportedEmail(),
                            phone,
                            reportRepository.countByReportedUser(data.getReportedUser()),
                            data.getReporterEmail()
                    );
                })
                .collect(Collectors.toList());

        int currentPage = pageResult.getNumber() + 1; // Current page (1-based)
        long totalItems = pageResult.getTotalElements(); // Total items from query
        int totalPages = (int) Math.ceil((double) totalItems / pageResult.getSize()); // Total pages

        return PaginationUtil.createResultPageDTO(
                totalItems, // Total items
                dtos, // Data displayed
                currentPage, // Current page
                currentPage > 1 ? currentPage - 1 : 1, // Prev page
                currentPage < totalPages ? currentPage + 1 : totalPages, // Next page
                1, // First page
                totalPages, // Last page
                pageResult.getSize() // Per page
        );
    }


    @Override
    public ReportUserDetailResponse findDataById(String id) {
        // set date
        LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        Page<ReportUserIndexProjection> pageResult = reportRepository.getDataReportUserIndex(id, null, null, start, end);
        if (pageResult.isEmpty()) {
            throw new EntityNotFoundException("Report Comment not found for ID: " + id);
        }

        ReportUserIndexProjection data = pageResult.getContent().get(0);
        List<Long> reportedIds = pageResult.stream()
                .map(ReportUserIndexProjection::getReportedId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, String> userNames = userProjectionService.findUserNameMaps(reportedIds);
        Map<Long, String> userPhones = userProjectionService.findUserPhoneMaps(reportedIds);
        Long reportedId = data.getReportedId();
        String name = userNames.get(reportedId) != null ? userNames.get(reportedId) : null;
        String phone = userPhones.get(reportedId) != null ? userPhones.get(reportedId) : null;
        return new ReportUserDetailResponse(
                data.getId(),
                name,
                phone,
                data.getReportedEmail()
        );
    }

    @Override
    public String updateStatusReportUser(ReportUserStatusRequest dto) {
        Report report = HandlerRepository.getEntityBySecureId(dto.getId(), reportRepository, "Report not found");
        AppUser user = report.getReportedUser();
        String message;
        switch (dto.getStatus()){
            case "SUSPENDED":
                report.setStatus("END");
                user.getAppUserAttribute().setIsSuspended(true);
                userRepository.save(user);
                message = "User has been suspended";
                break;
            case "WARNING":
                report.setStatus("WARNING");
                message = "User has been warned";
                break;
            default:
                throw new BadRequestException("Invalid status");
        }

        reportRepository.save(report);
        return message;
    }
}
