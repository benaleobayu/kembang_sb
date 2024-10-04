package com.bca.byc.repository.handler;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.impl.SecureIdentifiable;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.auth.AppUserRepository;
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

    public static AppUser getEntityUserBySecureId(String secureId, AppUserRepository userRepository, String errorMessage) {
        return userRepository.findUserBySecureId(secureId)
                .map(projection -> userRepository.findById(projection.getId())
                        .orElseThrow(() -> new EntityNotFoundException(errorMessage)))
                .orElseThrow(() -> new EntityNotFoundException(errorMessage));
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
