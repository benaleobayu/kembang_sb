package com.bca.byc.service.impl;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserSetPasswordRequest;
import com.bca.byc.model.api.UserUpdatePasswordRequest;
import com.bca.byc.model.api.UserUpdateRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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
    public UserDetailResponse findUserById(Long id) {
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
    public void setNewPassword(Long userId, UserSetPasswordRequest dto) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new BadRequestException("invalid.userId"));

        if (!dto.isSetPasswordMatch()) {
            throw new BadRequestException("Password does not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

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
    public void updateUser(Long id, @Valid UserUpdateRequest dto) {
        // check exist and get
        User data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID User ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // save
        repository.save(data);
    }

}
