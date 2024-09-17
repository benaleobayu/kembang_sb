package com.bca.byc.controller;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.AdminType;
import com.bca.byc.enums.UserType;
import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.service.AppAdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserPreRegisterControllerTest {

    static final Integer min = 18;
    static final Integer max = 35;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    public void setUp() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        this.token = getToken(); // Save the token before running the tests
    }

    public String getToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/cms/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"admin@unictive.net\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("success"))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        // Assuming the token is in the "token" field of the response
        return objectMapper.readTree(responseJson)
                .get("data")
                .get("accessToken")
                .asText();
    }

    @Test
    void listFollowUser() {
    }

    @Test
    void getById() {
    }

    @Test
    public void testCreatePreRegister() throws Exception {
        Faker faker = new Faker();

        UserType[] userTypes = {UserType.MEMBER, UserType.NOT_MEMBER};
        String[] memberTypes = {"SOLITAIRE", "PRIORITY"};
        Long cardNumber = faker.number().randomNumber(16, true);
        Long cinNumber = faker.number().randomNumber(11, true);
        LocalDate birthdate = LocalDate.now().minusDays(faker.number().numberBetween(365 * 18, 365 * 35));
        
        
        // Create a sample PreRegisterCreateRequest object
        PreRegisterCreateRequest request = new PreRegisterCreateRequest();
        request.setName(faker.name().fullName());
        request.setEmail(faker.internet().emailAddress());
        request.setPhone(faker.phoneNumber().phoneNumber());
        request.setType(userTypes[faker.number().numberBetween(0, 2)]); // Assuming UserType is an enum
        request.setMemberType(memberTypes[faker.number().numberBetween(0, 2)]); // Assuming UserType is an enum
        request.setDescription(faker.lorem().sentence(10));
        request.setMemberBankAccount(cardNumber.toString());
        request.setChildBankAccount(cardNumber.toString());
        request.setMemberBirthdate(birthdate);
        request.setChildBirthdate(birthdate);
        request.setMemberCin(cinNumber.toString());
        request.setChildCin(cinNumber.toString());
        request.setStatus(true);
        request.setStatusApproval(AdminApprovalStatus.OPT_APPROVED);

        // Convert request to JSON
        String requestJson = objectMapper.writeValueAsString(request);

        // print
        System.out.println(requestJson);

        mockMvc.perform(post("/cms/v1/um/pre-register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully created pre-register user"));
    }

    @Test
    void update() throws Exception {
        Faker faker = new Faker();

        Long userId = 10L;

        UserType[] userTypes = {UserType.MEMBER, UserType.NOT_MEMBER};
        String[] memberTypes = {"SOLITAIRE", "PRIORITY"};
        Long cardNumber = faker.number().randomNumber(16, true);
        Long cinNumber = faker.number().randomNumber(11, true);
        LocalDate birthdate = LocalDate.now().minusDays(faker.number().numberBetween(365 * 18, 365 * 35));
        // Create a sample PreRegisterCreateRequest object
        PreRegisterUpdateRequest request = new PreRegisterUpdateRequest();
        request.setName(faker.name().fullName());
        request.setEmail(faker.internet().emailAddress());
        request.setPhone(faker.phoneNumber().phoneNumber());
        request.setType(userTypes[faker.number().numberBetween(0, 2)]); // Assuming UserType is an enum
        request.setMemberType(memberTypes[faker.number().numberBetween(0, 2)]); // Assuming UserType is an enum
        request.setDescription(faker.lorem().sentence(10));
        request.setMemberBankAccount(cardNumber.toString());
        request.setChildBankAccount(cardNumber.toString());
        request.setMemberBirthdate(birthdate);
        request.setChildBirthdate(birthdate);
        request.setMemberCin(cinNumber.toString());
        request.setChildCin(cinNumber.toString());

        // Convert request to JSON
        String requestJson = objectMapper.writeValueAsString(request);

        // print
        System.out.println(requestJson);

        mockMvc.perform(put("/cms/v1/um/pre-register/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully updated pre-register user"));
    }

    @Test
    void delete() {
    }
}