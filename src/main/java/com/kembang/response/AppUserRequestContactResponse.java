package com.kembang.response;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUserRequestContactResponse {
    private Long id;
    private String messages;
}