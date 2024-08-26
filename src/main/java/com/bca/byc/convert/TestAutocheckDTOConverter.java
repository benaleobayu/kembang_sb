package com.bca.byc.convert;

import com.bca.byc.entity.TestAutocheck;
import com.bca.byc.model.test.AutocheckCreateRequest;
import com.bca.byc.model.test.AutocheckDetailResponse;
import com.bca.byc.model.test.AutocheckUpdateRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class TestAutocheckDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public AutocheckDetailResponse convertToListResponse(TestAutocheck data) {
        // mapping Entity with DTO Entity
        AutocheckDetailResponse dto = modelMapper.map(data, AutocheckDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public TestAutocheck convertToCreateRequest(@Valid AutocheckCreateRequest dto) {
        // mapping DTO Entity with Entity
        TestAutocheck data = modelMapper.map(dto, TestAutocheck.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(TestAutocheck data, @Valid AutocheckUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
