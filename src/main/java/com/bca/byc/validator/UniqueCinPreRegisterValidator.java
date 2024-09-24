package com.bca.byc.validator;

import com.bca.byc.repository.AppUserDetailRepository;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.validator.annotation.UniqueCinPreRegister;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueCinPreRegisterValidator implements ConstraintValidator<UniqueCinPreRegister, String> {

    @Autowired
    private PreRegisterRepository repository;

    @Autowired
    private AppUserDetailRepository userDetailRepository;

    @Override
    public void initialize(UniqueCinPreRegister constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cin, ConstraintValidatorContext context) {
        if (cin == null || cin.isEmpty() || "00000000000".equals(cin)) {
            return true; // @NotNull/@NotEmpty should handle this
        }
        return !repository.existsByMemberCin(cin) && !userDetailRepository.existsByMemberCinWithStatusApproved(cin);
    }
}