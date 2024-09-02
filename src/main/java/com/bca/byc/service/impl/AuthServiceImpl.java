package com.bca.byc.service.impl;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.auth.RegisterRequest;
import com.bca.byc.repository.*;
import com.bca.byc.service.AuthService;
import com.bca.byc.service.email.EmailService;
import com.bca.byc.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private TestAutocheckRepository testAutocheckRepository;

    private OtpRepository otpRepository;

    private EmailService emailService;

    private UserDTOConverter converter;

    @Override
    public void saveUser(RegisterRequest dto) throws Exception {
        Optional<User> existingUserOptional = userRepository.findByEmail(dto.getEmail());

        // Check if the user with the given email already exists and has an active status
        if (existingUserOptional.isPresent() && existingUserOptional.get().getStatus().equals(StatusType.ACTIVATED)) {
            throw new BadRequestException("Email already exists");
        }

        // Check if the user type is not customer
        if (dto.getType().equals(UserType.NOT_CUSTOMER)) {
            throw new BadRequestException("Please register as BCA member first. You can provide us with your bank account details. Please contact customer service.");
        }

        User user;
        if (existingUserOptional.isEmpty()) {
            // Create a new user if not existing
            user = converter.convertToCreateRequest(dto);
            user.setStatus(StatusType.PENDING);
        } else {
            // Update the existing user
            user = existingUserOptional.get();
            user.setName(dto.getName());
            user.setType(dto.getType());
            user.setMemberBankAccount(dto.getMemberBankAccount());
            user.setMemberBirthdate(dto.getMemberBirthdate());
            user.setChildBankAccount(dto.getChildBankAccount());
            user.setChildBirthdate(dto.getChildBirthdate());
            user.setPhone(dto.getPhone());
        }

        // Check the TestAutocheck data
        TestAutocheck dataCheck = testAutocheckRepository.findByMemberBankAccount(dto.getMemberBankAccount());
        boolean member = dataCheck != null && testAutocheckRepository.existsByMemberBankAccount(dto.getMemberBankAccount());
        boolean child = dataCheck != null && dataCheck.getChildBankAccount().equals(dto.getChildBankAccount());

        if ((dto.getType().equals(UserType.MEMBER) && member) || (dto.getType().equals(UserType.NOT_MEMBER) && child)) {
            user.setStatus(StatusType.APPROVED);
            if (dto.getType().equals(UserType.MEMBER)) {
                user.setMemberCin(dataCheck.getMemberCin()); // set cin member
            } else {
                user.setChildCin(dataCheck.getChildCin()); // set child cin
                user.setChildBankAccount(dataCheck.getChildBankAccount()); // set child bank account
            }
            user.setMemberType(dataCheck.getMemberType()); // set member type soli / prio

            UserAttributes userAttributes = user.getUserAttributes();
            if (userAttributes == null) {
                userAttributes = new UserAttributes();
                user.setUserAttributes(userAttributes);
            }
            userAttributes.setUser(user);
            userAttributes.setApprovedBy("SYSTEM");
            userAttributes.setIsVerified(true);

//            UserAttributes savedUserAttributes = userRepository.save(user).getUserAttributes();
//            user.setUserAttributes(savedUserAttributes);
            userRepository.save(user);
            String identity = "get"; // identity send registration otp
            resendOtp(identity, dto.getEmail());
        } else {
            user.setStatus(StatusType.REJECTED);
            int newRejectCount = user.getCountReject() + 1; // increment reject count
            user.setCountReject(newRejectCount);

            UserAttributes userAttributes = user.getUserAttributes();
            if (userAttributes == null) {
                userAttributes = new UserAttributes();
                user.setUserAttributes(userAttributes);
            }
            userAttributes.setUser(user);

//            UserAttributes savedUserAttributes = userRepository.save(user).getUserAttributes();
//            user.setUserAttributes(savedUserAttributes);
            userRepository.save(user);

            if (newRejectCount >= 3) {
                throw new BadRequestException("Your account can't be created anymore. Please contact admin.");
            } else {
                throw new BadRequestException("Your account is rejected. Please contact admin.");
            }
        }
    }

    @Override
    public void generateAndSendOtp(String identity, User user) throws MessagingException {

        String otpCode = OtpUtil.generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // OTP valid for 10 minutes

        Otp otp = new Otp(null, user, otpCode, expiryDate, true);
        otpRepository.save(otp);

        // Send OTP to user's email
        emailService.sendOtpMessage(identity, user.getEmail(), user.getName(), otpCode);

    }

    @Override
    public void sendWaitingApproval(User user) throws MessagingException {

        // Send Waiting email
        emailService.sendWaitingApproval(user.getEmail(), "Your account is waiting for approval", user.getName());
    }

    @Override
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
                    user.setStatus(StatusType.VERIFIED); // Enable the user
                    userRepository.save(user);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void resendOtp(String identity, String email) throws MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Invalidate previous OTPs
            otpRepository.invalidateOtpsForUser(user);

            // Generate and send new OTP
            generateAndSendOtp(identity, user);
        } else {
            throw new MessagingException("User not found with email: " + email);
        }
    }


}
