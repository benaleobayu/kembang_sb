package com.bca.byc.repository.handler;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;
import com.bca.byc.entity.SecureIdentifiable;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.projection.FollowsUserProjection;
import com.bca.byc.repository.AppUserProjectionRepository;
import com.bca.byc.repository.CommentRepository;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public class HandlerRepository {

    public static <T, ID extends Long> T getEntityById(ID id, JpaRepository<T, ID> repository, String notFoundMessage) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

    // get user by email
    public static <Email extends String> AppUser getEntityByEmail(Email email, AppUserRepository repository, String notFoundMessage) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

     // get user by email
    public static <Email extends String> FollowsUserProjection getUserProjectionByEmail(Email email, AppUserProjectionRepository repository, String notFoundMessage) {
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


}
