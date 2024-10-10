package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.BranchDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Branch;
import com.bca.byc.entity.Kanwil;
import com.bca.byc.entity.Location;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BranchCreateUpdateRequest;
import com.bca.byc.model.BranchDetailResponse;
import com.bca.byc.repository.BranchRepository;
import com.bca.byc.repository.KanwilRepository;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.security.util.ContextPrincipal;
import com.bca.byc.service.cms.BranchService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BranchServiceImpl implements BranchService {

    static String notFound = "not found";
    private final AppAdminRepository adminRepository;
    private final BranchRepository repository;
    private final BranchDTOConverter converter;
    private final LocationRepository locationRepository;
    private final KanwilRepository kanwilRepository;

    @Override
    public ResultPageResponseDTO<BranchDetailResponse> listDataBranch(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Branch> pageResult = repository.findDataByKeyword(keyword, pageable);
        List<BranchDetailResponse> dtos = pageResult.stream().map((c) -> {
            BranchDetailResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }


    @Override
    public BranchDetailResponse findDataBySecureId(String id) throws BadRequestException {
        Branch data = HandlerRepository.getEntityBySecureId(id, repository, "Branch" + notFound);

        return converter.convertToListResponse(data);
    }

    @Override
    public void saveData(@Valid BranchCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Location location = HandlerRepository.getEntityBySecureId(dto.locationId(), locationRepository, "Location not found");
        Kanwil kanwil = HandlerRepository.getEntityBySecureId(dto.kanwilId(), kanwilRepository, "Kanwil not found");

        Branch data = converter.convertToCreateRequest(dto);
        data.setLocation(location);
        data.setKanwil(kanwil);

        GlobalConverter.CmsAdminCreateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    public void updateData(String id, BranchCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Branch data = HandlerRepository.getEntityBySecureId(id, repository, "Branch" + notFound);
        Location location = HandlerRepository.getEntityBySecureId(dto.locationId(), locationRepository, "Location not found");
        Kanwil kanwil = HandlerRepository.getEntityBySecureId(dto.kanwilId(), kanwilRepository, "Kanwil not found");

        converter.convertToUpdateRequest(data, dto);
        data.setLocation(location);
        data.setKanwil(kanwil);

        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        Branch user = HandlerRepository.getEntityBySecureId(id, repository, "Branch" + notFound);
        Long dataId = user.getId();
        // delete data
        if (!repository.existsById(dataId)) {
            throw new BadRequestException("Branch not found");
        } else {
            repository.deleteById(dataId);
        }
    }
}
