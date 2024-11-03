package com.bca.byc.service;

import com.bca.byc.model.CustomerCreateUpdateRequest;
import com.bca.byc.model.CustomerDetailResponse;
import com.bca.byc.model.CustomerIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface CustomerService {

    ResultPageResponseDTO<CustomerIndexResponse> ListCustomerIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, String location, Boolean isSubscriber, Boolean export);

    CustomerDetailResponse FindCustomerById(String id);

    void CreateCustomer(CustomerCreateUpdateRequest dto);

    void UpdateCustomer(String id, CustomerCreateUpdateRequest dto);

    void DeleteCustomer(String id);
}
