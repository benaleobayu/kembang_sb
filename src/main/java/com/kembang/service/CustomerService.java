package com.kembang.service;

import com.kembang.model.CustomerCreateUpdateRequest;
import com.kembang.model.CustomerDetailResponse;
import com.kembang.model.CustomerIndexResponse;
import com.kembang.response.ResultPageResponseDTO;

public interface CustomerService {

    ResultPageResponseDTO<CustomerIndexResponse> ListCustomerIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, String location, Boolean isSubscriber, Boolean export);

    CustomerDetailResponse FindCustomerById(String id);

    void CreateCustomer(CustomerCreateUpdateRequest dto);

    void UpdateCustomer(String id, CustomerCreateUpdateRequest dto);

    void DeleteCustomer(String id);
}
