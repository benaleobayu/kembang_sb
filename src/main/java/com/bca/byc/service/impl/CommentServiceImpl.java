package com.bca.byc.service.impl;

import com.bca.byc.converter.CommentDTOConverter;
import com.bca.byc.entity.Comment;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.apps.CommentCreateRequest;
import com.bca.byc.model.apps.CommentDetailResponse;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository repository;
    private CommentDTOConverter converter;

    @Override
    public CommentDetailResponse findDataById(Long id) throws BadRequestException {
        Comment data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Comment not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<CommentDetailResponse> findAllData() {
        // Get the list
        List<Comment> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid CommentCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Comment data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, CommentCreateRequest dto) throws BadRequestException {
        // check exist and get
        Comment data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Comment ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(Long id) throws BadRequestException {
        // delete data
        if (!repository.existsById(id)) {
            throw new BadRequestException("Comment not found");
        } else {
            repository.deleteById(id);
        }
    }
}
