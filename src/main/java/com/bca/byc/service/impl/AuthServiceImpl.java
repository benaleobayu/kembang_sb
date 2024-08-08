package com.bca.byc.service.impl;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.Otp;
import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.repository.OtpRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.AuthService;
import com.bca.byc.service.email.EmailService;
import com.bca.byc.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository repository;

    private OtpRepository otpRepository;

    private PasswordEncoder passwordEncoder;

    private EmailService emailService;

    private UserDTOConverter converter;

    @Override
    public void saveUser(RegisterRequest dto) throws Exception {
        User user = converter.convertToCreateRequest(dto);

        repository.save(user);

        // send waiting approval email
        sendWaitingApproval(user);
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
    public void sendWaitingApproval(User user) throws MessagingException {

        // Send Waiting email
        emailService.sendWaitingApproval(user.getEmail(), "Your account is waiting for approval", user.getName());
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
