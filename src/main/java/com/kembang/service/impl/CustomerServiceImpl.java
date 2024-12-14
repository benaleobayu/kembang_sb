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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.kembang.converter.parsing.TreeGetEntityProjection.getParsingLocationByProjection;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final PasswordEncoder passwordEncoder;
    private final AppAdminRepository adminRepository;

    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;

    @Override
    public ResultPageResponseDTO<CustomerIndexResponse> ListCustomerIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword, String location, Boolean isSubscriber, Boolean export) {
        Location dataLocation = getParsingLocationByProjection(location, locationRepository);

        Long locationId = dataLocation != null ? dataLocation.getId() : null;

        Page<AppUser> firstResult = customerRepository.getCustomerIndex(null, null, locationId, isSubscriber);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, firstResult);

        Page<AppUser> pageResult = customerRepository.getCustomerIndex(set.keyword(), set.pageable(), locationId, isSubscriber);
        List<CustomerIndexResponse> dtos = pageResult.stream().map((c) -> {
            CustomerIndexResponse dto = new CustomerIndexResponse();
            dto.setName(c.getName());
            dto.setPhone(c.getPhone());
            dto.setAddress(c.getAddress());
            dto.setLocation(c.getLocation());
            dto.setDistance(c.getDistance());
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
                user.getDistance(),
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
        String password = "janganpassword";
        BigDecimal distance = new BigDecimal(dto.distance());

        String prefixEmail = dto.name().replaceAll("[^a-zA-Z0-9]+", "_").replaceAll("\\s", "_").toLowerCase();
        newUser.setName(dto.name() != null ? dto.name() : newUser.getName());
        newUser.setEmail(prefixEmail + "@apps.net");
        newUser.setPassword(password != null ? passwordEncoder.encode(password) : newUser.getPassword());
        newUser.setPhone(dto.phone() != null ? dto.phone() : newUser.getPhone());
        newUser.setAddress(dto.address() != null ? dto.address() : newUser.getAddress());
        newUser.setDistance(dto.distance() != null ? distance : newUser.getDistance());
        newUser.setLocation(dto.location() != null ? dto.location() : newUser.getLocation());
        newUser.setDaySubscribed(dto.daySubscribed() != null ? String.join(", ", dto.daySubscribed()) : newUser.getDaySubscribed());
        newUser.setIsSubscribed(dto.isSubscribed() != null ? dto.isSubscribed() : newUser.getIsSubscribed());
        newUser.setIsActive(dto.isActive() != null ? dto.isActive() : newUser.getIsActive());

        if (type.equals("create")) {
            GlobalConverter.CmsAdminCreateAtBy(newUser, adminLogin.getId());
        }
        if (type.equals("update")) {
            GlobalConverter.CmsAdminUpdateAtBy(newUser, adminLogin.getId());
        }
        customerRepository.save(newUser);
    }
}
