package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturnApps;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Location;
import com.bca.byc.model.CustomerCreateUpdateRequest;
import com.bca.byc.model.CustomerDetailResponse;
import com.bca.byc.model.CustomerIndexResponse;
import com.bca.byc.model.projection.CastIdBySecureIdProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.CustomerRepository;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.CustomerService;
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
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword,
                location,
                isSubscriber
        );
        CastIdBySecureIdProjection locProjection = locationRepository.findIdBySecureId(location);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Page<AppUser> pageResult = customerRepository.getCustomerIndex(set.keyword(), set.pageable(), locProjection.getId(), isSubscriber);
        List<CustomerIndexResponse> dtos = pageResult.stream().map((c) -> {
            CustomerIndexResponse dto = new CustomerIndexResponse();
            dto.setName(c.getName());
            dto.setPhone(c.getPhone());
            dto.setAddress(c.getAddress());

            Location loc = HandlerRepository.getEntityById(c.getLocation(), locationRepository, "Location not found");
            dto.setLocation(loc.getName());

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
        Location loc = HandlerRepository.getEntityById(user.getLocation(), locationRepository, "Location not found");
        return new CustomerDetailResponse(
                user.getSecureId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getLocation(),
                loc.getName(),
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
