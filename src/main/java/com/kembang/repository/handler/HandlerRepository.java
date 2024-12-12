package com.kembang.repository.handler;

import com.kembang.entity.AppAdmin;
import com.kembang.entity.AppUser;
import com.kembang.entity.impl.SecureIdentifiable;
import com.kembang.exception.ResourceNotFoundException;
import com.kembang.repository.AppUserRepository;
import com.kembang.repository.auth.AppAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.function.Function;

public class HandlerRepository {

    public static <T, ID extends Long> T getEntityById(ID id, JpaRepository<T, ID> repository, String notFoundMessage) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

    // get user by email
    public static <Email extends String> AppUser getUserByEmail(Email email, AppUserRepository repository, String notFoundMessage) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

    // get admin by email
    public static <Email extends String> AppAdmin getAdminByEmail(Email email, AppAdminRepository repository, String notFoundMessage) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }


    public static <T extends SecureIdentifiable> T getEntityBySecureId(String secureId, JpaRepository<T, Long> repository, String notFoundMessage) {
        return repository.findAll()
                .stream()
                .filter(entity -> entity.getSecureId().equals(secureId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

    public static <T, U> U getIdBySecureId(
            String secureId,
            Function<String, Optional<T>> findBySecureIdFunction,
            Function<T, Optional<U>> findByIdFunction,
            String errorMessage) {

        return findBySecureIdFunction.apply(secureId)
                .map(projection -> findByIdFunction.apply(projection)
                        .orElseThrow(() -> new EntityNotFoundException(errorMessage)))
                .orElseThrow(() -> new EntityNotFoundException(errorMessage));
    }


}
