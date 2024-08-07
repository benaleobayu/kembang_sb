package com.bca.byc;

import com.bca.byc.entity.Otp;
import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.repository.OtpRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.impl.UserServiceImpl;
import com.bca.byc.util.OtpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    @Transactional
    public void testOtpUpdate() {
        // Create and save a user
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setPassword("password");
        user.setStatus(StatusType.PENDING);
        userRepository.save(user);

        // Generate and save an OTP
        String otpCode = OtpUtil.generateOtp();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);

        Otp otp = new Otp();
        otp.setExpiryDate(expiryDate);
        otp.setOtp(otpCode);
        otp.setUser(user);
        otp.setValid(true);
        otpRepository.save(otp);

        // Validate OTP
        boolean result = userService.validateOtp("test@example.com", otpCode);
        assertTrue(result, "OTP validation should succeed");

        // Verify OTP status
        Optional<Otp> updatedOtp = otpRepository.findById(otp.getId());
        assertTrue(updatedOtp.isPresent(), "OTP should be present in the repository");
        assertFalse(updatedOtp.get().getValid(), "OTP should be invalid after validation");
    }
}