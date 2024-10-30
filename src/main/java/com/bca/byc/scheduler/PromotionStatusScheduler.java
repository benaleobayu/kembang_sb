package com.bca.byc.scheduler;

import com.bca.byc.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PromotionStatusScheduler {

    private final PostRepository postRepository;

    @Scheduled(cron = "0 * * * * *") // running every minute
    public void updatePromotionStatus() {
        LocalDateTime now = LocalDateTime.now();
        postRepository.findAll().forEach(post -> {
            if (post.getPromotedStatus().equals("SCHEDULED")
                    && now.isAfter(post.getPromotedAt())
                    && now.isBefore(post.getPromotedUntil())) {
                post.setPromotedActive(true);
                post.setPromotedStatus("RUNNING");
            } else if (now.isAfter(post.getPromotedUntil())) {
                post.setPromotedActive(false);
                post.setPromotedStatus("DONE");
            }
            postRepository.save(post);
        });
    }
}