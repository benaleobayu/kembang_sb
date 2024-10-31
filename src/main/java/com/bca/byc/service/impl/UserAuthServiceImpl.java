package com.bca.byc.service.impl;

import com.bca.byc.entity.*;
import com.bca.byc.entity.auth.Otp;
import com.bca.byc.enums.ActionType;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.LogStatus;
import com.bca.byc.enums.StatusType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AppRegisterRequest;
import com.bca.byc.model.LogUserManagementRequest;
import com.bca.byc.model.LoginRequestDTO;
import com.bca.byc.model.UserSetPasswordRequest;
import com.bca.byc.repository.*;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ApiDataResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.DataAccessResponse;
import com.bca.byc.security.util.JWTTokenFactory;
import com.bca.byc.service.AppUserService;
import com.bca.byc.service.UserAuthService;
import com.bca.byc.service.email.EmailService;
import com.bca.byc.service.util.ClientInfoService;
import com.bca.byc.util.OtpUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.bca.byc.converter.parsing.TreeLogUserManagement.logUserManagement;

@Service
@AllArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final EmailService emailService;

    private final AppUserRepository userRepository;
    private final AppUserDetailRepository userDetailRepository;
    private final OtpRepository otpRepository;
    private final LogUserManagementRepository logUserManagementRepository;

    private final AuthCheckPreRegister authCheckPreRegister;
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final JWTTokenFactory jwtUtil;
    private final LogDeviceRepository logDeviceRepository;
    private final ClientInfoService clientInfoService;
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> authenticate(String deviceId, String version, LoginRequestDTO dto, HttpServletRequest request) {
        AppUser user = HandlerRepository.getUserByEmail(dto.email(), userRepository, "Your email is not registered. Please create an account to continue.");

        if (user.getAppUserAttribute().getIsSuspended()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Your account has been suspended. Please contact the administrator."));
        }

        if (user.getAppUserAttribute().getIsDeleted()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Your email is not registered. Please create an account to continue."));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(StringUtils.lowerCase(dto.email()), dto.password())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "The email address and password you entered do not match. Please try again."));
        }

        final UserDetails userDetails = appUserService.loadUserByUsername(dto.email());
        final String tokens = jwtUtil.createAccessJWTToken(userDetails.getUsername(), null).getToken();
        final DataAccessResponse dataAccess = new DataAccessResponse(tokens, "Bearer", jwtUtil.getExpirationTime());

        final String ipAddress = clientInfoService.getClientIp(request);

        logDeviceActivity(dto.email(), deviceId, version, ipAddress);

        return ResponseEntity.ok().body(new ApiDataResponse<>(true, "success", dataAccess));
    }

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
            user.setEmail(StringUtils.lowerCase(dto.email()));

            AppUserDetail userDetail = new AppUserDetail();

            userDetail.setCountryCode("+62");
            userDetail.setPhone(dto.phone());
            userDetail.setMemberBankAccount(dto.member_bank_account());
            userDetail.setParentBankAccount(dto.parent_bank_account());
            userDetail.setMemberBirthdate(dto.member_birthdate());
            userDetail.setStatus(StatusType.PRE_REGISTER);
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
            userDetail.setParentBankAccount(dto.parent_bank_account());
            userDetail.setMemberBirthdate(dto.member_birthdate());
            userDetail.setStatus(StatusType.PRE_REGISTER);
            user.setAppUserDetail(userDetail);
        }

        // Check the PreRegister data
        PreRegister dataCheck = authCheckPreRegister.findByMemberBankAccountAndStatusApproval(dto.member_bank_account(), AdminApprovalStatus.APPROVED);
        boolean member = dataCheck != null && authCheckPreRegister.existsByMemberBankAccount(dto.member_bank_account());
        PreRegister dataMemberPreRegister = authCheckPreRegister.findByMemberBankAccountAndStatusApproval(dto.member_bank_account(), AdminApprovalStatus.APPROVED);
        boolean child = dataCheck != null && dataCheck.getParentBankAccount().equals(dto.parent_bank_account());
        AppUserDetail userDetail = user.getAppUserDetail();
        AppUserAttribute userAttribute = user.getAppUserAttribute();

        boolean checkMemberBirthdate = dataMemberPreRegister != null && dataMemberPreRegister.getMemberBirthdate() != null && dto.member_birthdate().equals(dataMemberPreRegister.getMemberBirthdate());
        if (dto.parent_bank_account() == null && member && checkMemberBirthdate ||
                dto.parent_bank_account().isEmpty() && member && checkMemberBirthdate ||
                member && child && checkMemberBirthdate) {
            user.setAppUserDetail(userDetail);
            userDetail.setStatus(StatusType.APPROVED);
            userDetail.setMemberType(dataCheck.getMemberType() != null ? dataCheck.getMemberType() : null); // set type
            userDetail.setMemberCin(dataCheck.getMemberCin()); // set cin member
            userDetail.setAccountType(dataCheck.getAccountType() != null ? dataCheck.getAccountType() : null); // set member type
            if (dto.parent_bank_account() == null) {
                userDetail.setUserAs("member");
            } else {
                userDetail.setUserAs("child");
            }
            userDetail.setParentCin(dataCheck.getParentCin()); // set child cin
            userDetail.setParentType(dataCheck.getParentType()); // set parent type
            userDetail.setParentBankAccount(dataCheck.getParentBankAccount()); // set child bank account
            user.setName(dataCheck.getName());
            userDetail.setName(dataCheck.getName());
            userDetail.setAccountType(dataCheck.getAccountType()); // set member type soli / prio
            userDetail.setApprovedBy("SYSTEM");
            user.setAppUserDetail(userDetail);

            userAttribute.setIsApproved(true);
            userAttribute.setApprovedAt(LocalDateTime.now());
            user.setAppUserAttribute(userAttribute);

            AppUser savedUser = userRepository.save(user);
            String identity = "get"; // identity send registration otp
            sendRegistrationOtp(identity, savedUser.getEmail());
        } else {
            userDetail.setStatus(StatusType.PRE_REGISTER);
            int newRejectCount = user.getCountReject() + 1; // increment reject count
            user.setCountReject(newRejectCount);
            user.setAppUserDetail(userDetail);
            userRepository.save(user);

            if (newRejectCount >= 3) {
                userAttribute.setIsRejected(true);
                user.getAppUserDetail().setStatus(StatusType.REJECTED);
                user.setAppUserAttribute(userAttribute);
                userRepository.save(user);
                throw new BadRequestException("Your account can't be created anymore. Please contact admin.");
            } else {
                throw new BadRequestException("Your account is rejected. Please contact admin.");
            }
        }

        setCreatedUpdatedAt(user);
        setCreatedUpdatedAtDetail(user.getAppUserDetail());
        setCreatedUpdatedAtAttr(user.getAppUserAttribute());
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
                    if (userDetail.getStatus().equals(StatusType.PRE_ACTIVATED)) {
                        userDetail.setStatus(StatusType.PRE_ACTIVATED);
                    } else if (userDetail.getStatus().equals(StatusType.ACTIVATED)) {
                        userDetail.setStatus(StatusType.ACTIVATED);
                    } else {
                        userDetail.setStatus(StatusType.VERIFIED); // Enable the user TODO if forgot password, set status to pending
                    }

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
            StatusType status = user.getAppUserDetail().getStatus();

            if (identity.equals("reset") && (status.equals(StatusType.PRE_ACTIVATED) || status.equals(StatusType.ACTIVATED))) {

                // Invalidate previous OTPs
                otpRepository.invalidateOtpsForUser(user);

                // Generate and send new OTP
                generateAndSendOtp(identity, user);
            } else if (identity.equals("resend") && status.equals(StatusType.APPROVED) || status.equals(StatusType.VERIFIED)) {
                // Invalidate previous OTPs
                otpRepository.invalidateOtpsForUser(user);

                // Generate and send new OTP
                generateAndSendOtp(identity, user);
            } else {
                throw new MessagingException("User not found or not eligible for OTP.");
            }
        } else {
            throw new MessagingException("User not found");
        }
    }


    @Override
    public void sendRegistrationOtp(String identity, String email) throws MessagingException {
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        AppUser user = userOptional.get();
        // Invalidate previous OTPs
        otpRepository.invalidateOtpsForUser(user);

        // Generate and send new OTP
        generateAndSendOtp(identity, user);
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
    @Transactional
    public void setNewPassword(String email, UserSetPasswordRequest dto) {
        AppUser user = HandlerRepository.getUserByEmail(email, userRepository, "User not found in email: " + email);

        if (!dto.isSetPasswordMatch()) {
            throw new BadRequestException("Password does not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        AppUserDetail userDetail = user.getAppUserDetail();

        if (userDetail.getStatus().equals(StatusType.VERIFIED)) {
            userDetail.setStatus(StatusType.PRE_ACTIVATED);
        } else if (userDetail.getStatus().equals(StatusType.ACTIVATED)) {
            userDetail.setStatus(StatusType.ACTIVATED);
        } else {
            userDetail.setStatus(userDetail.getStatus());
        }
        user.setAppUserDetail(userDetail);

        // save
        AppUser savedUser = userRepository.save(user);

        PreRegister dataCheck = authCheckPreRegister.findByMemberBankAccountAndStatusApproval(savedUser.getAppUserDetail().getMemberBankAccount(), AdminApprovalStatus.APPROVED);

        if (dataCheck != null) {
            dataCheck.setStatusApproval(AdminApprovalStatus.DELETED);
            dataCheck.setIsActive(false);
            dataCheck.setIsDeleted(true);
            LogUserManagementRequest newLog = new LogUserManagementRequest(
                    "USED",
                    "Data used on new user"
            );
            logUserManagement(
                    dataCheck,
                    user,
                    null,
                    LogStatus.USED,
                    newLog,
                    logUserManagementRepository
            );
            authCheckPreRegister.save(dataCheck);
        }
    }


    // --------------------------------------------------------------------------
    private void logDeviceActivity(String email, String deviceId, String version, String ipAddress) {
        AppUser appUser = appUserService.findByEmail(email);
        LogDevice logDevice = new LogDevice();
        logDevice.setUser(appUser);
        logDevice.setDeviceId(deviceId);
        logDevice.setVersion(version);
        logDevice.setIpAddress(ipAddress);
        logDevice.setActionType(ActionType.LOGIN);
        logDeviceRepository.save(logDevice);
    }

    private void setCreatedUpdatedAt(AppUser dto) {
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
    }

    private void setCreatedUpdatedAtDetail(AppUserDetail dto) {
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
    }

    private void setCreatedUpdatedAtAttr(AppUserAttribute dto) {
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
    }

}
