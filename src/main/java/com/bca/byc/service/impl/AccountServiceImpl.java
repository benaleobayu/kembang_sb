package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Account;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AccountCreateUpdateRequest;
import com.bca.byc.model.AccountDetailResponse;
import com.bca.byc.model.AccountIndexResponse;
import com.bca.byc.repository.AccountRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AppAdminRepository adminRepository;
    private final AccountRepository accountRepository;

    @Override
    public ResultPageResponseDTO<AccountIndexResponse> listDataAccountIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Account> pageResult = accountRepository.findByKeywordAccountIndex(keyword, pageable);
        List<AccountIndexResponse> dtos = pageResult.stream().map((c) -> new AccountIndexResponse(
                // TODO set model and get from data
        )).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public AccountDetailResponse findDataById(String id) throws BadRequestException {
        Account data = getEntity(id);

        return new AccountDetailResponse(
                // TODO set model and get from data
        );
    }


    @Override
    @Transactional
    public void saveData(@Valid AccountCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Account data = new Account();

        // TODO logic to save
        // save data
        GlobalConverter.CmsAdminCreateAtBy(data, admin);
        accountRepository.save(data);
    }

    @Override
    @Transactional
    public void updateData(String id, AccountCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Account data = getEntity(id);

        // TODO logic update

        // update the updated_at
        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        accountRepository.save(data);
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
}





