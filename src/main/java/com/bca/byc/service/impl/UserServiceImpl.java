package com.bca.byc.service.impl;

import com.bca.byc.entity.Otp;
import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.entity.UserType;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.model.user.UserDetail;
import com.bca.byc.model.user.UserUpdateRequest;
import com.bca.byc.repository.OtpRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.EmailService;
import com.bca.byc.service.UserService;
import com.bca.byc.util.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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




    @Override
    public boolean existsById(Long Id) {
        return repository.existsById(Id);
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

    public void generateAndSendOtp(User user) throws MessagingException {
        String otpCode = OtpUtil.generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // OTP valid for 10 minutes

        Otp otp = new Otp(user, otpCode, expiryDate, true);
        otpRepository.save(otp);

        // Send OTP to user's email
        emailService.sendOtpMessage(user.getEmail(), "Your OTP Code", user.getName(), otpCode);
    }

    @Override
    public UserDetail getUserDetail(Long userId) {
        Optional<User> userdata = repository.findById(userId);
        UserDetail dto = new UserDetail();

        if (userdata.isPresent()){
            User user = userdata.get();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setBankAccount(user.getBankAccount());
            dto.setEducation(user.getEducation());
            dto.setBusinessName(user.getBusinessName());
            dto.setCin(user.getCin());
            dto.setBiodata(user.getBiodata());
            return dto;
        }

        return null;
    }

    @Override
    public void updateUser(Long userId, UserUpdateRequest dto) {
        // get repository
        Optional<User> userdata = repository.findById(userId);
        // update
        if (userdata.isPresent()) {
            User user = userdata.get();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setBankAccount(dto.getBankAccount());
            user.setEducation(dto.getEducation());
            user.setBusinessName(dto.getBusinessName());
            user.setCin(dto.getCin());
            user.setBiodata(dto.getBiodata());

            // save
            repository.save(user);
        }

    }
}
