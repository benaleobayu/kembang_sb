package com.bca.byc.model.user;

import com.bca.byc.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetailResponse implements Serializable {

    private Long id;
    private String parentName;
    private String name;
    private String email;
    private String phone;
    private String bankAccount;
    private String education;
    private String businessName;
    private String cin;
    private String biodata;
    private String status;
    private String createdAt;
    private String updateAt;

    public UserDetailResponse(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.createdAt = user.getCreatedAt().format(formatter);
        this.updateAt = user.getUpdatedAt().format(formatter);
    }


}
