package com.kembang.service.impl;

import com.kembang.converter.dictionary.PageCreateReturnApps;
import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.AppAdmin;
import com.kembang.entity.AppUser;
import com.kembang.entity.Location;
import com.kembang.model.CustomerCreateUpdateRequest;
import com.kembang.model.CustomerDetailResponse;
import com.kembang.model.CustomerIndexResponse;
import com.kembang.model.projection.CastIdBySecureIdProjection;
import com.kembang.model.search.ListOfFilterPagination;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.CustomerRepository;
import com.kembang.repository.LocationRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final PasswordEncoder passwordEncoder;
    private final AppAdminRepository adminRepository;

    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;

    @Override
    public ResultPageResponseDTO<CustomerIndexResponse> ListCustomerIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, String location, Boolean isSubscriber, Boolean export) {
        ListOfFilterPagination filter = new ListOfFilterPagination(keyword, location, isSubscriber);
        CastIdBySecureIdProjection locProjection = locationRepository.findIdBySecureId(location);

        Long locationId = locProjection != null ? locProjection.getId() : null;

        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Page<AppUser> pageResult = customerRepository.getCustomerIndex(set.keyword(), set.pageable(), locationId, isSubscriber);
        List<CustomerIndexResponse> dtos = pageResult.stream().map((c) -> {
            CustomerIndexResponse dto = new CustomerIndexResponse();
            dto.setName(c.getName());
            dto.setPhone(c.getPhone());
            dto.setAddress(c.getAddress());
            dto.setLocation(c.getLocation());
            List<String> days = c.getDaySubscribed() != null ? Arrays.asList(c.getDaySubscribed().split(", ")) : List.of();
            dto.setDaySubscribed(days);
            dto.setIsSubscribed(c.getIsSubscribed());
            GlobalConverter.CmsIDTimeStampResponseAndId(dto, c, adminRepository);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public CustomerDetailResponse FindCustomerById(String id) {
        AppUser user = HandlerRepository.getEntityBySecureId(id, customerRepository, "Customer not found");
        return new CustomerDetailResponse(
                user.getSecureId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getLocation(),
                user.getDaySubscribed() != null ? Arrays.asList(user.getDaySubscribed().split(", ")) : List.of(),
                user.getIsSubscribed(),
                user.getIsActive()
        );
    }

    @Override
    public void CreateCustomer(CustomerCreateUpdateRequest dto) {
        AppUser newUser = new AppUser();
        saveCustomer(newUser, dto, "create");
    }

    @Override
    public void UpdateCustomer(String id, CustomerCreateUpdateRequest dto) {
        AppUser user = HandlerRepository.getEntityBySecureId(id, customerRepository, "Customer not found");
        saveCustomer(user, dto, "update");
    }

    @Override
    public void DeleteCustomer(String id) {
        AppUser user = HandlerRepository.getEntityBySecureId(id, customerRepository, "Customer not found");
        customerRepository.deleteById(user.getId());
    }

    // -- helper --
    private void saveCustomer(AppUser newUser, CustomerCreateUpdateRequest dto, String type) {
        AppAdmin adminLogin = GlobalConverter.getAdminEntity(adminRepository);
        newUser.setName(dto.name());

        String prefixEmail = dto.name().replaceAll("[^a-zA-Z0-9]+", "_").replaceAll("\\s", "_").toLowerCase();
        newUser.setEmail(prefixEmail + "@apps.net");
        newUser.setPassword(passwordEncoder.encode("janganpassword"));
        newUser.setPhone(dto.phone());
        newUser.setAddress(dto.address());
        newUser.setLocation(dto.location());
        newUser.setDaySubscribed(String.join(", ", dto.daySubscribed()));
        newUser.setIsSubscribed(dto.isSubscribed());
        newUser.setIsActive(dto.isActive());

        if (type.equals("create")) {
            GlobalConverter.CmsAdminCreateAtBy(newUser, adminLogin.getId());
        }
        if (type.equals("update")) {
            GlobalConverter.CmsAdminUpdateAtBy(newUser, adminLogin.getId());
        }
        customerRepository.save(newUser);
    }
}
