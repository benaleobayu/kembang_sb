package com.bca.byc.service;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Notification;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.AppUserRepository;
import com.bca.byc.repository.NotificationRepository;
import com.bca.byc.response.NotificationResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService {

    private final AppUserRepository userRepository;

    private NotificationRepository notificationRepository;

    public Page<Notification> getNotificationsByUserId(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }

    public ResultPageResponseDTO<NotificationResponse> getNotificationsByUserId(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        AppUser userLogin = GlobalConverter.getUserEntity(userRepository);

        ListOfFilterPagination filter = new ListOfFilterPagination(keyword);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Page<Notification> pageResult = notificationRepository.findByUserIdAndKeyword(userLogin.getId(), set.keyword(), set.pageable());
        List<NotificationResponse> dtos = pageResult.stream().map((data) -> new NotificationResponse(
                    data.getSecureId(),
                    data.getType(),
                    data.getNotifiableType(),
                    data.getNotifiableId(),
                    data.getMessage(),
                    data.getReadAt().format(DateTimeFormatter.ISO_DATE_TIME),
                    data.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME),
                    data.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME),
                    data.getIsRead()
            )).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }
}
