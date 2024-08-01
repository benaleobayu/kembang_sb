package com.bca.byc.service;

import com.bca.byc.entity.Otp;
import com.bca.byc.entity.User;
import com.bca.byc.entity.UserType;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.repository.OtpRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void saveUser(RegisterRequest registerRequest) throws Exception {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setType(UserType.MEMBER); // Default value
        user.setStatus(false); // Default value

        userRepository.save(user);

        // Generate and send OTP
        generateAndSendOtp(user);
    }

    private void generateAndSendOtp(User user) throws MessagingException {
        String otpCode = OtpUtil.generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // OTP valid for 10 minutes

        Otp otp = new Otp(user, otpCode, expiryDate, true);
        otpRepository.save(otp);

        // Send OTP to user's email
        emailService.sendOtpMessage(user.getEmail(), "Your OTP Code", user.getName(), otpCode);
    }

    public boolean validateOtp(String email, String otpCode) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Otp> otpOptional = otpRepository.findByUserAndOtpAndValidIsTrue(user, otpCode);
            if (otpOptional.isPresent()) {
                Otp otp = otpOptional.get();
                if (otp.getExpiryDate().isAfter(LocalDateTime.now())) {
                    otp.setValid(false); // Mark OTP as used
                    otpRepository.save(otp);
                    user.setStatus(true); // Enable the user
                    userRepository.save(user);
                    return true;
                }
            }
        }
        return false;
    }

    public void resendOtp(String email) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
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