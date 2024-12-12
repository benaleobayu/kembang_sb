package com.kembang.service.email;

import lombok.Data;

@Data
public class EmailDTORequest {

    private String email;
    private String name;
    private String Subject;
    private String cardNumber;

}
