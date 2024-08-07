package com.bca.byc.service.impl;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.Otp;
import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.entity.UserType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserSetPasswordRequest;
import com.bca.byc.model.api.UserUpdatePasswordRequest;
import com.bca.byc.model.api.UserUpdateRequest;
import com.bca.byc.repository.OtpRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.EmailService;
import com.bca.byc.service.UserService;
import com.bca.byc.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return null;
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
    public void saveUser(RegisterRequest registerRequest) throws Exception {
        User user = converter.convertToCreateRequest(registerRequest);

        user.setType(UserType.MEMBER); // Default value
        user.setStatus(StatusType.OTP); // Default value

        repository.save(user);

        // Generate and send OTP
        generateAndSendOtp(user);
    }

    @Override
    public void updateUser(Long userId, UserUpdateRequest dto) {

    }

    @Override
    public void generateAndSendOtp(User user) throws MessagingException {
        String otpCode = OtpUtil.generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // OTP valid for 10 minutes

        Otp otp = new Otp(user, otpCode, expiryDate, true);
        otpRepository.save(otp);

        // Send OTP to user's email
        emailService.sendOtpMessage(user.getEmail(), "Your OTP Code", user.getName(), otpCode);
    }

    @Override
    public boolean validateOtp(String email, String otpCode) {
        Optional<User> userOptional = repository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Otp> otpOptional = otpRepository.findByUserAndOtpAndValidIsTrue(user, otpCode);
            if (otpOptional.isPresent()) {
                Otp otp = otpOptional.get();
                if (otp.getExpiryDate().isAfter(LocalDateTime.now())) {
                    otp.setValid(false); // Mark OTP as used
                    otpRepository.save(otp);
                    user.setStatus(StatusType.PENDING); // Enable the user
                    repository.save(user);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void resendOtp(String email) throws MessagingException {
        Optional<User> userOptional = repository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Invalidate previous OTPs
            otpRepository.invalidateOtpsForUser(user);

            // Generate and send new OTP
            generateAndSendOtp(user);
        } else {
            throw new MessagingException("User not found with email: " + email);
        }
    }
}
