package com.bca.byc.service.impl;

import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private UserRepository userRepository;

    @Override
    public void followUser(Long userId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        User userToFollow = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getFollows().contains(userToFollow)) {
            user.getFollows().add(userToFollow);
            userRepository.save(user);
        }

    }

    @Override
    public void unfollowUser(Long userId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found in email: " + email));
        User userToUnfollow = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (user.getFollows().contains(userToUnfollow)) {
            user.getFollows().remove(userToUnfollow);
            userRepository.save(user);
        }
    }
}
