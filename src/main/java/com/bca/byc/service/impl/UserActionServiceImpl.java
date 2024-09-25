package com.bca.byc.service.impl;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Post;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.repository.PostRepository;
import com.bca.byc.repository.UserActionRepository;
import com.bca.byc.service.UserActionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserActionServiceImpl implements UserActionService {

    private final UserActionRepository userActionRepository;
    private final PostRepository postRepository;

    @Override
    public void followUser(Long userId, String email) {
        AppUser user = userActionRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        AppUser userToFollow = userActionRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getFollows().contains(userToFollow)) {
            user.getFollows().add(userToFollow);
            userActionRepository.save(user);
        }

    }

    @Override
    public void unfollowUser(Long userId, String email) {
        AppUser user = userActionRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        AppUser userToUnfollow = userActionRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (user.getFollows().contains(userToUnfollow)) {
            user.getFollows().remove(userToUnfollow);
            userActionRepository.save(user);
        }
    }

    @Override
    public void likeDislikePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow( () -> new ResourceNotFoundException("Post not found"));

        AppUser user = userActionRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));

    }
}
