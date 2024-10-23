package com.bca.byc.service.impl;

import com.bca.byc.converter.AccountDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AccountCreateUpdateRequest;
import com.bca.byc.model.AccountDetailResponse;
import com.bca.byc.model.AccountIndexResponse;
import com.bca.byc.model.ChannelChecklistResponse;
import com.bca.byc.repository.AccountHasChannelsRepository;
import com.bca.byc.repository.AccountRepository;
import com.bca.byc.repository.ChannelRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.AccountService;
import com.bca.byc.util.PaginationUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AppAdminRepository adminRepository;
    private final AccountRepository accountRepository;
    private final ChannelRepository channelRepository;
    private final AccountHasChannelsRepository accountHasChannelsRepository;

    private AccountDTOConverter converter;

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
        dto.setStatus(data.getIsActive());

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
    public void saveData(@Valid AccountCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Account data = new Account();
        data.setName(dto.getName());
        data.setIsActive(dto.getStatus());

        GlobalConverter.CmsAdminCreateAtBy(data, admin);
        Account savedData = accountRepository.save(data);

        // save channel in every new account
        saveChannelInAccount(savedData, dto.getChannelIds());
    }

    @Override
    @Transactional
    public void updateData(String id, AccountCreateUpdateRequest dto) throws BadRequestException {
        // check exist and get
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Account data = getEntity(id);

        // update
        data.setName(dto.getName());
        data.setIsActive(dto.getStatus());

        // update the updated_at
        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        Account savedData = accountRepository.save(data);

        // remove old channel
        accountHasChannelsRepository.deleteByAccountId(savedData.getId());
        // save channel in every new account
        saveChannelInAccount(savedData, dto.getChannelIds());
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
}





