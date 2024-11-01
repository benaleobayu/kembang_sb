package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.enums.StatusType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AccountDetailResponse;
import com.bca.byc.model.AccountIndexResponse;
import com.bca.byc.model.ChannelChecklistResponse;
import com.bca.byc.repository.*;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AccountService;
import com.bca.byc.util.FileUploadHelper;
import com.bca.byc.util.PaginationUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bca.byc.util.FileUploadHelper.saveFile;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AppAdminRepository adminRepository;
    private final AccountRepository accountRepository;
    private final ChannelRepository channelRepository;
    private final AccountHasChannelsRepository accountHasChannelsRepository;

    private final AppUserRepository userRepository;
    private final AppUserDetailRepository userDetailRepository;
    private final AppUserAttributeRepository userAttributeRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Override
    public ResultPageResponseDTO<AccountIndexResponse> listDataAccountIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Account> pageResult = accountRepository.findByKeywordAccountIndex(keyword, pageable);
        List<AccountIndexResponse> dtos = pageResult.stream().map((c) -> {
            AccountIndexResponse dto = new AccountIndexResponse();
            dto.setName(c.getName());
            dto.setStatus(c.getIsActive());
            Set<String> channelNames = c.getAccountHasChannels().stream().map(data -> data.getChannel().getName()).collect(Collectors.toSet());
            dto.setChannelNames(channelNames);
            dto.setAvatar(GlobalConverter.getAvatarImage(c.getAvatar(), baseUrl));
            dto.setCover(GlobalConverter.getAvatarImage(c.getCover(), baseUrl));

            GlobalConverter.CmsIDTimeStampResponseAndId(dto, c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public AccountDetailResponse findDataById(String id) throws BadRequestException {
        Account data = getEntity(id);
        AccountDetailResponse dto = new AccountDetailResponse();
        dto.setName(data.getName());
        dto.setEmail(data.getUser().getEmail());
        dto.setStatus(data.getIsActive());
        dto.setAvatar(GlobalConverter.getAvatarImage(data.getAvatar(), baseUrl));
        dto.setCover(GlobalConverter.getAvatarImage(data.getCover(), baseUrl));

        // get all
        List<Channel> allChannels = channelRepository.findAll();
        List<ChannelChecklistResponse> listChannels = new ArrayList<>();
        List<Long> connectedChannels = accountHasChannelsRepository.findChannelIdsByAccountId(data.getId());

        // define on child model
        for (Channel channel : allChannels) {
            ChannelChecklistResponse response = new ChannelChecklistResponse();
            response.setId(channel.getSecureId());
            response.setName(channel.getName());

            if (connectedChannels.contains(channel.getId())) {
                response.setIsChecked(true);
            } else {
                response.setIsChecked(false);
            }
            listChannels.add(response);
        }

        dto.setChannels(listChannels);

        return dto;
    }

    @Override
    @Transactional
    public void saveData(MultipartFile avatar, MultipartFile cover, String name, Boolean status, Set<String> channelIds, String email, String password) throws IOException {
        FileUploadHelper.validateFileTypeImage(avatar);
        FileUploadHelper.validateFileTypeImage(cover);

        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Account data = new Account();
        data.setName(name);
        data.setIsActive(status);

        // save avatar
        String avatarUrl = FileUploadHelper.saveFile(avatar, UPLOAD_DIR + "/account/avatar");
        data.setAvatar(GlobalConverter.getParseImage(avatarUrl, baseUrl));
        // save cover
        String coverUrl = FileUploadHelper.saveFile(cover, UPLOAD_DIR + "/account/cover");
        data.setCover(GlobalConverter.getParseImage(coverUrl, baseUrl));

        AppUser newUser = saveNewUser(avatarUrl, coverUrl, name, status, email, password);
        data.setUser(newUser);

        GlobalConverter.CmsAdminCreateAtBy(data, admin);
        Account savedData = accountRepository.save(data);

        // save channel in every new account

        saveChannelInAccount(savedData, channelIds);

    }

    @Override
    @Transactional
    public void updateData(String id, MultipartFile avatar, MultipartFile cover, String name, Set<String> channelIds, Boolean status, String email, String oldPassword, String newPassword) throws IOException {

        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Account data = HandlerRepository.getEntityBySecureId(id, accountRepository, "Account not found.");
        data.setName(name);
        data.setIsActive(status);

        if (oldPassword != null && !passwordEncoder.matches(oldPassword, data.getUser().getPassword())) {
            throw new BadRequestException("Old password is incorrect.");
        }
        if (!email.equals(data.getUser().getEmail())) {
            data.getUser().setEmail(email);
        }

        data.getUser().setPassword(passwordEncoder.encode(newPassword));

        if (avatar != null) {
            FileUploadHelper.validateFileTypeImage(avatar);
            String oldAvatar = data.getAvatar();
            String newAvatar = saveFile(avatar, UPLOAD_DIR + "/account/avatar");
            data.setAvatar(GlobalConverter.replaceImagePath(newAvatar));
            if (!oldAvatar.equals(newAvatar)) {
                FileUploadHelper.deleteFile(oldAvatar, UPLOAD_DIR);
            }
        }

        if (cover != null) {
            FileUploadHelper.validateFileTypeImage(cover);
            String oldCover = data.getCover();
            String newCover = saveFile(cover, UPLOAD_DIR + "/account/cover");
            data.setCover(GlobalConverter.replaceImagePath(newCover));
            if (!oldCover.equals(newCover)) {
                FileUploadHelper.deleteFile(oldCover, UPLOAD_DIR);
            }
        }
        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        Account savedData = accountRepository.save(data);

        accountHasChannelsRepository.deleteByAccountId(savedData.getId());
        // save channel in every new account
        saveChannelInAccount(savedData, channelIds);
    }

    @Override
    @Transactional
    public void deleteData(String id) throws BadRequestException {
        Account data = getEntity(id);
        // delete data
        if (!accountRepository.existsById(data.getId())) {
            throw new BadRequestException("Account not found");
        } else {
            accountRepository.deleteById(data.getId());
        }
    }

    // -- Helper --
    private Account getEntity(String id) {
        return HandlerRepository.getEntityBySecureId(id, accountRepository, "Account not found.");
    }

    private void saveChannelInAccount(Account savedAccount, Set<String> channelIds) {
        if (channelIds != null) {
            Set<Channel> channels = new HashSet<>();
            for (String channelId : channelIds) {
                Channel channel = HandlerRepository.getIdBySecureId(
                        channelId,
                        channelRepository::findByIdAndSecureId,
                        projection -> channelRepository.findById(projection.getId()),
                        "Channel not found for ID: " + channelId
                );
                channels.add(channel);
            }
            for (Channel channel : channels) {
                AccountHasChannels accountHasChannels = new AccountHasChannels();
                accountHasChannels.setAccount(savedAccount);
                accountHasChannels.setChannel(channel);
                accountHasChannelsRepository.save(accountHasChannels);
            }
        }
    }

    // -- helper --
    private AppUser saveNewUser(String avatar, String cover, String name, Boolean status, String email, String password) {
        AppUserDetail newUserDetail = new AppUserDetail();
        newUserDetail.setName(name);
        newUserDetail.setStatus(StatusType.ACTIVATED);
        newUserDetail.setAvatar(avatar);
        newUserDetail.setCover(cover);
        newUserDetail.setIsActive(status);
        newUserDetail.setCreatedAt(LocalDateTime.now());
        newUserDetail.setUpdatedAt(LocalDateTime.now());
        AppUserDetail savedUserDetail = userDetailRepository.save(newUserDetail);

        AppUserAttribute newUserAttribute = new AppUserAttribute();
        newUserAttribute.setIsApproved(true);
        newUserAttribute.setApprovedAt(LocalDateTime.now());
        newUserAttribute.setIsVerified(true);
        newUserAttribute.setVerifiedAt(LocalDateTime.now());
        newUserAttribute.setIsSuspended(false);
        newUserAttribute.setIsDeleted(false);
        newUserAttribute.setIsOfficial(true);
        AppUserAttribute savedUserAttribute = userAttributeRepository.save(newUserAttribute);

        AppUser newUser = new AppUser();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));

        newUser.setAppUserDetail(savedUserDetail);
        newUser.setAppUserAttribute(savedUserAttribute);

        return userRepository.save(newUser);
    }
}





