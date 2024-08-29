package com.bca.byc.service.impl;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.response.ResultPageResponse;
import com.bca.byc.service.UserService;
import com.bca.byc.util.PaginationUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
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
    public UserDetailResponse findDataById(Long id) {
        User data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<UserDetailResponse> findAllUsers() {
        List<User> datas = repository.findAll();

        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDetailResponse> findUserPendingAndActive() {
        List<User> datas = repository.findUserPendingAndActive(StatusType.APPROVED, StatusType.REJECTED);

        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDetailResponse> findUserActive() {
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
    public ResultPageResponse<UserDetailResponse> findDataList(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : userName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<User> pageResult = repository.findByNameLikeIgnoreCase(userName,pageable);
        List<UserDetailResponse> dtos = pageResult.stream().map(converter::convertToListResponse).collect(Collectors.toList());
        return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
    }

    @Override
    public ResultPageResponse<UserDetailResponse> listFollowUser(Integer pages, Integer limit, String sortBy, String direction, String userName) {
        userName = StringUtils.isEmpty(userName) ? "%" : userName + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        Page<User> pageResult = repository.findByNameLikeIgnoreCaseAndStatusAndUserAttributesIsRecommendedTrue(userName,StatusType.ACTIVATED,pageable);

        List<UserDetailResponse> dtos = pageResult.stream().map(converter::convertToListResponse).collect(Collectors.toList());
        return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
    }

    @Override
    public void followUser(Long userId, String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        User userToFollow = repository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getFollows().contains(userToFollow)) {
            user.getFollows().add(userToFollow);
            repository.save(user);
        }

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
