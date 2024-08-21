package com.bca.byc.convert;

import com.bca.byc.entity.TestAutocheck;
import com.bca.byc.model.test.TestAutocheckModelDTO;
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
    public TestAutocheckModelDTO.TestAutocheckDetailResponse convertToListResponse(TestAutocheck data) {
        // mapping Entity with DTO Entity
        TestAutocheckModelDTO.TestAutocheckDetailResponse dto = modelMapper.map(data, TestAutocheckModelDTO.TestAutocheckDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public TestAutocheck convertToCreateRequest(@Valid TestAutocheckModelDTO.TestAutocheckCreateRequest dto) {
        // mapping DTO Entity with Entity
        TestAutocheck data = modelMapper.map(dto, TestAutocheck.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(TestAutocheck data, @Valid TestAutocheckModelDTO.TestAutocheckUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
