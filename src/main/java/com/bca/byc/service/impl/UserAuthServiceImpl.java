package com.bca.byc.service.impl;

import com.bca.byc.entity.*;
import com.bca.byc.entity.auth.Otp;
import com.bca.byc.enums.StatusType;
import com.bca.byc.enums.UserType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AppRegisterRequest;
import com.bca.byc.model.UserSetPasswordRequest;
import com.bca.byc.repository.*;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.service.UserAuthService;
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
public class UserAuthServiceImpl implements UserAuthService {

    private final EmailService emailService;

    private final AppUserRepository userRepository;
    private final AppUserDetailRepository userDetailRepository;
    private final AppUserAttributeRepository userAttributeRepository;
    private final RoleRepository roleRepository;
    private final OtpRepository otpRepository;

    private final TestAutocheckRepository testAutocheckRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(AppRegisterRequest dto) throws MessagingException {
        Optional<AppUser> existingUserOptional = userRepository.findByEmail(dto.email());

        if (existingUserOptional.isPresent() && existingUserOptional.get().getAppUserDetail().getStatus().equals(StatusType.PRE_ACTIVATED) ||
                existingUserOptional.isPresent() && existingUserOptional.get().getAppUserDetail().getStatus().equals(StatusType.ACTIVATED)) {
            throw new BadRequestException("User already exists");
        }



        AppUser user;
        if (existingUserOptional.isEmpty()) {
            // Create a new user if not existing
            user = new AppUser();
            user.setEmail(dto.email());
            Role role = roleRepository.findByName("USER").orElseThrow(
                    () -> new BadRequestException("Role not found")
            );
            user.setRole(role);

            AppUserDetail userDetail = new AppUserDetail();

            userDetail.setType(dto.type());
            userDetail.setPhone(dto.phone());
            userDetail.setMemberBankAccount(dto.member_bank_account());
            userDetail.setChildBankAccount(dto.child_bank_account());
            userDetail.setMemberBirthdate(dto.member_birthdate());
            userDetail.setChildBirthdate(dto.child_birthdate());
            user.setAppUserDetail(userDetail);

            AppUserAttribute userAttribute = new AppUserAttribute();
            userAttribute.setIsRejected(false);
            user.setAppUserAttribute(userAttribute);
        } else {
            // Update the existing user
            user = existingUserOptional.get();

            AppUserDetail userDetail = userDetailRepository.findById(user.getAppUserDetail().getId()).orElseThrow(
                    () -> new BadRequestException("User detail not found")
            );

            userDetail.setPhone(dto.phone());
            userDetail.setMemberBankAccount(dto.member_bank_account());
            userDetail.setChildBankAccount(dto.child_bank_account());
            userDetail.setMemberBirthdate(dto.member_birthdate());
            userDetail.setChildBirthdate(dto.child_birthdate());
            user.setAppUserDetail(userDetail);
        }

        // Check if the user type is not customer
        if (dto.type().equals(UserType.NOT_CUSTOMER)) {
            throw new BadRequestException("Please register as BCA member first. You can provide us with your bank account details. Please contact customer service.");
        }

        // Check the PreRegister data
        PreRegister dataCheck = testAutocheckRepository.findByMemberBankAccount(dto.member_bank_account());
        boolean member = dataCheck != null && testAutocheckRepository.existsByMemberBankAccount(dto.member_bank_account());
        boolean child = dataCheck != null && dataCheck.getChildBankAccount().equals(dto.child_bank_account());
        AppUserDetail userDetail = user.getAppUserDetail();
        AppUserAttribute userAttribute = user.getAppUserAttribute();

        assert dataCheck != null;
        user.setName(dataCheck.getName());
        userDetail.setName(dataCheck.getName());

        if ((dto.type().equals(UserType.MEMBER) && member) || (dto.type().equals(UserType.NOT_MEMBER) && child)) {
            userDetail.setStatus(StatusType.APPROVED);
            user.setAppUserDetail(userDetail);
            if (dto.type().equals(UserType.MEMBER)) {
                userDetail.setMemberCin(dataCheck.getMemberCin());// set cin member
            } else {
                userDetail.setChildCin(dataCheck.getChildCin()); // set child cin
                userDetail.setChildBankAccount(dataCheck.getChildBankAccount()); // set child bank account
            }
            userDetail.setMemberType(dataCheck.getMemberType()); // set member type soli / prio
            userDetail.setApprovedBy("SYSTEM");
            user.setAppUserDetail(userDetail);

            userAttribute.setIsApproved(true);
            user.setAppUserAttribute(userAttribute);

            userRepository.save(user);
            String identity = "get"; // identity send registration otp
            resendOtp(identity, dto.email());
        } else {
            userDetail.setStatus(StatusType.REJECTED);
            int newRejectCount = user.getCountReject() + 1; // increment reject count
            user.setCountReject(newRejectCount);

            userDetail.setCreatedAt(LocalDateTime.now());
            user.setAppUserDetail(userDetail);

            userRepository.save(user);

            if (newRejectCount >= 3) {
                userAttribute.setIsRejected(true);
                user.setAppUserAttribute(userAttribute);
                userRepository.save(user);
                throw new BadRequestException("Your account can't be created anymore. Please contact admin.");
            } else {
                throw new BadRequestException("Your account is rejected. Please contact admin.");
            }
        }

        userRepository.saveAndFlush(user);

    }

    @Override
    public boolean validateOtp(String email, String otpCode) {
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            Optional<Otp> otpOptional = otpRepository.findByAppUserAndOtpAndValidIsTrue(user, otpCode);
            if (otpOptional.isPresent()) {
                Otp otp = otpOptional.get();
                if (otp.getExpiryDate().isAfter(LocalDateTime.now())) {
                    otp.setValid(false); // Mark OTP as used
                    otpRepository.save(otp);

                    AppUserDetail userDetail = user.getAppUserDetail();
                    userDetail.setStatus(StatusType.VERIFIED); // Enable the user TODO if forgot password, set status to pending

                    user.setAppUserDetail(userDetail);
                    userRepository.save(user);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void resendOtp(String identity, String email) throws MessagingException {
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            // Invalidate previous OTPs
            otpRepository.invalidateOtpsForUser(user);

            // Generate and send new OTP
            generateAndSendOtp(identity, user);
        } else {
            throw new MessagingException("User not found with email: " + email);
        }
    }

    @Override
    public void generateAndSendOtp(String identity, AppUser user) throws MessagingException {

        String otpCode = OtpUtil.generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // OTP valid for 10 minutes

        Otp otp = new Otp(null, user, otpCode, expiryDate, true);
        otpRepository.save(otp);

        // Send OTP to user's email
        emailService.sendOtpMessage(identity, user.getEmail(), user.getName(), otpCode);

    }

    @Override
    public void setNewPassword(String email, UserSetPasswordRequest dto) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("invalid.email"));

        if (!dto.isSetPasswordMatch()) {
            throw new BadRequestException("Password does not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        AppUserDetail userDetail = user.getAppUserDetail();

        if (userDetail.getStatus().equals(StatusType.VERIFIED)){
            userDetail.setStatus(StatusType.PRE_ACTIVATED);
        }else {
            userDetail.setStatus(userDetail.getStatus());
        }
        user.setAppUserDetail(userDetail);

        // save
        userRepository.save(user);
    }
}
