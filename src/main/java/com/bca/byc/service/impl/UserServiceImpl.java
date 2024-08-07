package com.bca.byc.service.impl;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.Otp;
import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.entity.UserType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.model.user.UserDetailResponse;
import com.bca.byc.model.user.UserUpdatePasswordRequest;
import com.bca.byc.model.user.UserUpdateRequest;
import com.bca.byc.repository.OtpRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.EmailService;
import com.bca.byc.service.UserService;
import com.bca.byc.util.OtpUtil;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository repository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDTOConverter userDTOConverter;


    @Override
    public boolean existsById(Long Id) {
        return repository.existsById(Id);
    }

    public void generateAndSendOtp(User user) throws MessagingException {
        String otpCode = OtpUtil.generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // OTP valid for 30 minutes

        Otp otp = new Otp();
        otp.setExpiryDate(expiryDate);
        otp.setOtp(otpCode);
        otp.setUser(user);
        otpRepository.save(otp);

        // Send OTP to user's email
        emailService.sendOtpMessage(user.getEmail(), "Your OTP Code", user.getName(), otpCode);
    }

    @Override
    public void saveUser(RegisterRequest registerRequest) throws Exception {
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setType(UserType.MEMBER); // Default value
        user.setStatus(StatusType.PENDING); // Default value

        repository.save(user);

        // Generate and send OTP
        generateAndSendOtp(user);
    }

    @Transactional
    @Override
    public boolean validateOtp(String email, String otpCode) {
        log.debug("Validating OTP for email: {} and code: {}", email, otpCode);

        // Find user by email
        Optional<User> userOptional = repository.findByEmail(email);

        // Check if user exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.debug("User found: {}", user);

            // Find OTP associated with the user and check if it's valid
            Optional<Otp> otpOptional = otpRepository.findByUserAndOtpAndValidIsTrue(user, otpCode);

            // Check if the OTP exists and is valid
            if (otpOptional.isPresent()) {
                Otp otp = otpOptional.get();
                log.debug("OTP found: {}", otp);

                // Verify if the OTP has not expired
                if (otp.getExpiryDate().isAfter(LocalDateTime.now())) {
                    log.debug("OTP is valid and has not expired");

                    // Mark OTP as used
                    otp.setValid(false); // Mark OTP as used
                    otpRepository.save(otp);
                    entityManager.flush(); // Ensure changes are flushed to the database
                    entityManager.clear(); // Clear persistence context

                    // Update user status to ACTIVE (or any other desired status)
                    user.setStatus(StatusType.PENDING); // Pending the user
                    repository.save(user);
                    return true; // OTP is valid
                } else {
                    log.debug("OTP has expired");
                }
            } else {
                log.debug("OTP not found or not valid");
            }
        } else {
            log.debug("User not found");
        }

        return false; // Invalid OTP
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


    @Override
    public List<User> findAllUsers(int pageable) {
        return repository.findAll(Pageable.ofSize(pageable)).getContent();
    }


    @Override
    public UserDetailResponse getUserDetail(Long userId) {
        // fetch or throw
        User user = repository.findById(userId)
                .orElseThrow(() -> new BadRequestException("invalid.userId"));

        // convert
        UserDetailResponse dto = userDTOConverter.convertToDTO(user);

        return dto;

    }

    @Override
    public void updateUser(Long userId, UserUpdateRequest dto) {
        // get repository
        // TODO make all fetch like this
        User user = repository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found bro"));
        // update

        userDTOConverter.convertToUpdateUser(dto);


        // save
        repository.save(user);

    }

    @Override
    public void updateUserPassword(Long userId, UserUpdatePasswordRequest dto) {
        User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        if (!dto.isPasswordMatch()) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        repository.save(user);
    }
}
