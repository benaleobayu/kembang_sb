package com.bca.byc.service.impl;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserSetPasswordRequest;
import com.bca.byc.model.api.UserUpdatePasswordRequest;
import com.bca.byc.model.api.UserUpdateRequest;
import com.bca.byc.repository.OtpRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.email.EmailService;
import com.bca.byc.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    private OtpRepository otpRepository;

    private PasswordEncoder passwordEncoder;

    private EmailService emailService;

    private UserDTOConverter converter;


    @Override
    public boolean existsById(Long userId) {
        return repository.existsById(userId);
    }

    @Override
    public UserDetailResponse findUserById(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new BadRequestException("invalid.userId"));
        return converter.convertToListResponse(user);
    }

    @Override
    public List<UserDetailResponse> findAllUsers() {

        List<User> dtos = repository.findAll();
        return dtos.stream()
                .map((user -> converter.convertToListResponse(user)))
                .collect(Collectors.toList());
    }

    @Override
    public void setNewPassword(Long userId, UserSetPasswordRequest dto) {
        User user = repository.findById(userId)
                .orElseThrow(()-> new BadRequestException("invalid.userId"));

        if(!dto.isSetPasswordMatch()){
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
    public void updateUser(Long userId, UserUpdateRequest dto) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new BadRequestException("invalid.userId"));

        // update
        converter.convertToUpdateRequest(dto);

        // save
        repository.save(user);
    }

}
