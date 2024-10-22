package com.bca.byc.converter;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Account;
import com.bca.byc.model.AccountIndexResponse;
import com.bca.byc.model.AccountCreateUpdateRequest;
import com.bca.byc.model.AccountDetailResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.bca.byc.converter.parsing.GlobalConverter.CmsIDTimeStampResponseAndId;

@Component
@AllArgsConstructor
public class AccountDTOConverter {

    private ModelMapper modelMapper;

    // for create data
    public Account convertToCreateRequest(@Valid AccountCreateUpdateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        Account data = modelMapper.map(dto, Account.class);
        data.setName(dto.getName());

        data.setCreatedAt(LocalDateTime.now());
        data.setUpdatedAt(LocalDateTime.now());
        data.setCreatedBy(admin);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Account data, @Valid AccountCreateUpdateRequest dto, AppAdmin admin) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        data.setName(dto.getName());

        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
        data.setUpdatedBy(admin);
    }


}
