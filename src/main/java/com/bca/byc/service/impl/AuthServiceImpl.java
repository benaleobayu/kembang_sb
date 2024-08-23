package com.bca.byc.service.impl;

import com.bca.byc.convert.UserDTOConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.OnboardingModelDTO;
import com.bca.byc.model.RegisterUserFeedbackRequest;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.model.auth.RegisterRequest;
import com.bca.byc.repository.*;
import com.bca.byc.service.AuthService;
import com.bca.byc.service.email.EmailService;
import com.bca.byc.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository repository;
    private BusinessRepository businessRepository;
    private BusinessCategoryRepository businessCategoryRepository;
    private BusinessHasCategoryRepository businessHasCategoryRepository;
    private FeedbackCategoryRepository feedbackCategoryRepository;
    private UserHasFeedbackRepository userHasFeedbackRepository;
    private TestAutocheckRepository testAutocheckRepository;

    private OtpRepository otpRepository;

    private PasswordEncoder passwordEncoder;

    private EmailService emailService;

    private UserDTOConverter converter;

    @Override
    public void saveUser(RegisterRequest dto) throws Exception {
        Optional<User> existingUserOptional = repository.findByEmail(dto.getEmail());

        // check user by email and status active
        if (existingUserOptional.isPresent() && existingUserOptional.get().getStatus().equals(StatusType.ACTIVATED)) {
            throw new BadRequestException("Email already exists");
        }
        // if option 3 or not customer
        if (dto.getType().equals(UserType.NOT_CUSTOMER)) {
            throw new BadRequestException("Please register as BCA member first. You can provide us with your bank account details. Please contact customer service.");
        }

        User user;
        if (existingUserOptional.isEmpty()) {
            // Create if not exist
            user = converter.convertToCreateRequest(dto);
            user.setStatus(StatusType.PENDING);
        } else {
            // Update if email is already present
            user = existingUserOptional.get();
            user.setName(dto.getName());
            user.setSolitaireBankAccount(dto.getSolitaireBankAccount());
            user.setPhone(dto.getPhone());
            user.setBirthdate(dto.getBirthdate());
            user.setCountReject(user.getCountReject());
        }

//        TestAutocheck dataCheck = testAutocheckRepository.findBySolitaireBankAccount(dto.getSolitaireBankAccount());


        // TODO validasi more
        boolean soliPrio = testAutocheckRepository.existsBySolitaireBankAccount(dto.getSolitaireBankAccount());
        boolean member = testAutocheckRepository.existsByMemberBankAccount(dto.getCin());
        System.out.println("solitaire found: " + soliPrio);
        if (soliPrio) {
            user.setStatus(StatusType.APPROVED);
//            if (existingUserOptional.isPresent() && existingUserOptional.get().getType().equals(UserType.MEMBER)) {
//                user.setCin(dataCheck.getSolitaireCin());
//            }
//            if (existingUserOptional.isPresent() && existingUserOptional.get().getType().equals(UserType.NOT_MEMBER)) {
//                user.setCin(dataCheck.getMemberCin());
//            }
            repository.save(user);
            resendOtp(dto.getEmail());
        } else {
            user.setStatus(StatusType.REJECTED);
            int newRejectCount = user.getCountReject() + 1;
            user.setCountReject(newRejectCount);

            if (newRejectCount >= 3) {
                throw new BadRequestException("Your account can't be created anymore. Please contact admin.");
            } else if (newRejectCount <= 3) {
                throw new BadRequestException("Your account is rejected. Please contact admin.");
            }

            repository.save(user);

        }
    }

    @Override
    @Transactional
    public void saveUserWithRelations(AuthRegisterRequest dto) throws Exception {
        // Create User
        User user = converter.convertToCreateGroupRequest(dto);
        user = repository.save(user);

        // Create Businesses
        for (OnboardingModelDTO.OnboardingBusinessRequest businessDto : dto.getBusinesses()) {
            Business business = new Business();
            business.setName(businessDto.getBusinessName());
            business.setAddress(businessDto.getBusinessAddress());
            business.setUser(user);
            business = businessRepository.save(business);

            // Create Business Categories
            for (OnboardingModelDTO.OnboardingBusinessCategoryRequest categoryDto : businessDto.getBusinessCategories()) {
                BusinessCategory parentCategory = businessCategoryRepository.findById(categoryDto.getBusinessCategoryId())
                        .orElseThrow(() -> new RuntimeException("Business Category Parent not found"));

                BusinessCategory childCategory = businessCategoryRepository.findById(categoryDto.getBusinessCategoryChildId())
                        .orElseThrow(() -> new RuntimeException("Business Category Child not found"));

                BusinessHasCategory businessHasCategory = new BusinessHasCategory();
                businessHasCategory.setBusiness(business);
                businessHasCategory.setBusinessCategoryParent(parentCategory);
                businessHasCategory.setBusinessCategoryChild(childCategory);
                businessHasCategory.setCreatedAt(LocalDateTime.now());
                businessHasCategory.setUpdatedAt(LocalDateTime.now());

                // Save the BusinessHasCategory entity
                businessHasCategoryRepository.save(businessHasCategory);
//                businessCategoryRepository.save(businessHasCategory);

            }

        }

        // Create Feedbacks
        for (RegisterUserFeedbackRequest feedbackDto : dto.getFeedbackCategories()) {
            FeedbackCategory feedbackCategory = feedbackCategoryRepository.findById(feedbackDto.getFeedbackCategoryId())
                    .orElseThrow(() -> new RuntimeException("Feedback Category not found"));

            UserHasFeedback userHasFeedback = new UserHasFeedback();
            userHasFeedback.setUser(user);
            userHasFeedback.setFeedbackCategory(feedbackCategory);
            userHasFeedback.setQuote(feedbackDto.getQuote());
            userHasFeedbackRepository.save(userHasFeedback);
        }

        // send waiting approval email
        sendWaitingApproval(user);
    }

    @Override
    public void generateAndSendOtp(User user) throws MessagingException {

        String otpCode = OtpUtil.generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); // OTP valid for 10 minutes

        Otp otp = new Otp(null, user, otpCode, expiryDate, true);
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
                    user.setStatus(StatusType.VERIFIED); // Enable the user
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
