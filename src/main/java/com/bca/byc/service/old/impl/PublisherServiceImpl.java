package com.bca.byc.service.old.impl;

import com.bca.byc.entity.old.Publisher;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.old.PublisherCreateRequestDTO;
import com.bca.byc.model.old.PublisherListResponseDTO;
import com.bca.byc.model.old.PublisherResponseDTO;
import com.bca.byc.model.old.PublisherUpdateRequestDTO;
import com.bca.byc.repository.old.PublisherRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.old.PublisherService;
import com.bca.byc.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    @Override
    public void createPublisher(PublisherCreateRequestDTO dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.getPublisherName());
        publisher.setCompanyName(dto.getCompanyName());
        publisher.setAddress(dto.getAddress());

        publisherRepository.save(publisher);

    }

    @Override
    public void updatePublisher(String publisherId, PublisherUpdateRequestDTO dto) {
        Publisher publisher = publisherRepository.findBySecureId(publisherId)
                .orElseThrow(() -> new BadRequestException("invalid.publisher_id"));
        publisher.setName(dto.getPublisherName() == null || dto.getPublisherName().isBlank() ? publisher.getName()
                : dto.getPublisherName());
        publisher.setCompanyName(
                dto.getCompanyName() == null || dto.getCompanyName().isBlank() ? publisher.getCompanyName()
                        : dto.getCompanyName());
        publisher.setAddress(dto.getAddress());

        publisherRepository.save(publisher);

    }

    @Override
    public ResultPageResponseDTO<PublisherListResponseDTO> findPublisherList(Integer pages, Integer limit,
                                                                             String sortBy, String direction, String publisherName) {
        publisherName = StringUtils.isBlank(publisherName) ? "%" : publisherName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Publisher> pageResult = publisherRepository.findByNameLikeIgnoreCase(publisherName, pageable);
        List<PublisherListResponseDTO> dtos = pageResult.stream().map((p) -> {
            PublisherListResponseDTO dto = new PublisherListResponseDTO();
            dto.setPublisherId(p.getSecureId());
            dto.setPublisherName(p.getName());
            dto.setCompanyName(p.getCompanyName());

            return dto;
        }).collect(Collectors.toList());

        int currentPage = pageResult.getNumber();
        int totalPages = pageResult.getTotalPages();

        return PaginationUtil.createResultPageDTO(
                pageResult.getTotalElements(), // total items
                dtos,
                currentPage, // current page
                currentPage > 0 ? currentPage - 1 : 0, // prev page
                currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
                0, // first page
                totalPages - 1, // last page
                pageResult.getSize() // per page
        );
    }

    @Override
    public Publisher findPublisher(String publisherId) {
        return publisherRepository.findBySecureId(publisherId)
                .orElseThrow(() -> new BadRequestException("invalid.publisher_id"));
    }

    @Override
    public PublisherResponseDTO constructDTO(Publisher publisher) {
        PublisherResponseDTO dto = new PublisherResponseDTO();
        dto.setPublisherId(publisher.getSecureId());
        dto.setPublisherName(publisher.getName());
        return dto;
    }

}
