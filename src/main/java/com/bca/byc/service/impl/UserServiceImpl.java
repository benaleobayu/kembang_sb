package com.bca.byc.service.impl;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    private UserDTOConverter converter;


    @Override
    public boolean existsById(Long userId) {
        return repository.existsById(userId);
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new BadRequestException("User not found"));
    }

    @Override
    public User findInfoByEmail(String email) {
        return repository.findInfoByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
    }

    @Override
    public UserCmsDetailResponse findDataById(Long id) {
        User data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<UserCmsDetailResponse> findAllUsers() {
        List<User> datas = repository.findAll();

        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserCmsDetailResponse> findUserPendingAndActive() {
        List<User> datas = repository.findUserPendingAndActive(StatusType.APPROVED, StatusType.REJECTED);

        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserCmsDetailResponse> findUserActive() {
        List<User> datas = repository.findByStatus(StatusType.APPROVED);

        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void setNewPassword(String email, UserSetPasswordRequest dto) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("invalid.email"));

        if (!dto.isSetPasswordMatch()) {
            throw new BadRequestException("Password does not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(StatusType.PRE_ACTIVATED);

        // save
        repository.save(user);
    }


    @Override
    public void changePassword(Long userId, UserUpdatePasswordRequest dto) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new BadRequestException("invalid.userId"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        if (!dto.isPasswordMatch()) {
            throw new BadRequestException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        // save
        repository.save(user);
    }

    @Override
    public void saveData(AuthRegisterRequest dto) {
        User data = converter.convertToCreateGroupRequest(dto);

        repository.save(data);
    }

    @Override
    public ResultPageResponseDTO<UserAppDetailResponse> findDataList(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : userName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<User> pageResult = repository.findByNameLikeIgnoreCase(userName, pageable);
        List<UserAppDetailResponse> dtos = pageResult.stream().map((c) -> {
            UserAppDetailResponse dto = converter.convertToInfoResponse(c);
            return dto;
        }).collect(Collectors.toList());

        int currentPage = pageResult.getNumber() + 1;
        int totalPages = pageResult.getTotalPages();

        return PaginationUtil.createResultPageDTO(
                pageResult.getTotalElements(), // total items
                dtos,
                currentPage, // current page
                currentPage > 1 ? currentPage - 1 : 1, // prev page
                currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
                1, // first page
                totalPages - 1, // last page
                pageResult.getSize() // per page
        );
    }


    @Override
    public ResultPageResponseDTO<UserAppDetailResponse> listFollowUser(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : userName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<User> pageResult = repository.findByNameLikeIgnoreCase(userName, pageable);
        List<UserAppDetailResponse> dtos = pageResult.stream().map((c) -> {
            UserAppDetailResponse dto = converter.convertToInfoResponse(c);
            return dto;
        }).collect(Collectors.toList());

        int currentPage = pageResult.getNumber() + 1;
        int totalPages = pageResult.getTotalPages();

        return PaginationUtil.createResultPageDTO(
                pageResult.getTotalElements(), // total items
                dtos,
                currentPage, // current page
                currentPage > 1 ? currentPage - 1 : 1, // prev page
                currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
                1, // first page
                totalPages - 1, // last page
                pageResult.getSize() // per page
        );
    }

    @Override
    public UserAppDetailResponse getUserDetails(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email not found"));

        User data = repository.findById(user.getId())
                .orElseThrow(() -> new BadRequestException("User not found"));

        return converter.convertToInfoResponse(data);
    }

    @Override
    public void updateData(Long id, @Valid UserUpdateRequest dto) {
        // check exist and get
        User data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID User ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // save
        repository.save(data);
    }

}
