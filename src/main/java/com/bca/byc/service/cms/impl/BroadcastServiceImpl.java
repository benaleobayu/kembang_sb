package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.BroadcastManagement;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BroadcastCreateUpdateRequest;
import com.bca.byc.model.BroadcastDetailResponse;
import com.bca.byc.model.BroadcastIndexResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.BroadcastRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.BroadcastService;
import com.bca.byc.util.helper.Formatter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BroadcastServiceImpl implements BroadcastService {

    private final AppAdminRepository adminRepository;

    private final BroadcastRepository broadcastRepository;

    @Override
    public ResultPageResponseDTO<BroadcastIndexResponse> listDataBroadcast(Integer pages, Integer limit, String sortBy, String direction, String keyword, String status, LocalDate postAt) {
        ListOfFilterPagination filter = new ListOfFilterPagination(keyword, status, postAt);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        LocalDateTime start = (postAt == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : postAt.atStartOfDay();
        LocalDateTime end = (postAt == null) ? LocalDateTime.now() : postAt.atTime(23, 59, 59);

        Page<BroadcastManagement> pageResult = broadcastRepository.findDataByKeyword(set.keyword(), status, start, end, set.pageable());
        List<BroadcastIndexResponse> dtos = pageResult.stream().map((c) -> {
            BroadcastIndexResponse dto = new BroadcastIndexResponse();
            dto.setTitle(c.getTitle());
            dto.setMessage(c.getMessage());
            dto.setStatus(c.getStatus());
            dto.setPostAt(Formatter.formatLocalDateTime(c.getPostAt()));

            GlobalConverter.CmsIDTimeStampResponseAndId(dto, c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public BroadcastDetailResponse findDataBySecureId(String id) {
        return null;
    }

    @Override
    public void saveData(BroadcastCreateUpdateRequest item) {
        AppAdmin adminLogin = GlobalConverter.getAdminEntity(adminRepository);

        BroadcastManagement data = new BroadcastManagement();
        saveData(data, item, adminLogin, "create");
        broadcastRepository.save(data);
    }

    @Override
    public void updateData(String id, BroadcastCreateUpdateRequest item) {
        AppAdmin adminLogin = GlobalConverter.getAdminEntity(adminRepository);

        BroadcastManagement data = HandlerRepository.getEntityBySecureId(id, broadcastRepository, "Broadcast not found");
        saveData(data, item, adminLogin, "update");
        broadcastRepository.save(data);
    }

    @Override
    public void deleteData(String id) {
        BroadcastManagement data = HandlerRepository.getEntityBySecureId(id, broadcastRepository, "Broadcast not found");

        if (!broadcastRepository.existsById(data.getId())) {
            throw new BadRequestException("Broadcast not found");
        } else {
            broadcastRepository.delete(data);
        }

    }

    // -- helper --

    private void saveData(BroadcastManagement data, BroadcastCreateUpdateRequest item, AppAdmin adminLogin, String type) {
        data.setTitle(item.getTitle());
        data.setMessage(item.getMessage());
        data.setStatus(item.getStatus());
        data.setPostAt(item.getPostAt());

        if (type.equals("create")) {
            GlobalConverter.CmsAdminCreateAtBy(data, adminLogin);
        }
        if (type.equals("update")) {
            GlobalConverter.CmsAdminUpdateAtBy(data, adminLogin);
        }

        data.setIsScheduled(item.getStatus().equals("SCHEDULED"));
        data.setIsSent(item.getStatus().equals("SENT"));
    }
}
