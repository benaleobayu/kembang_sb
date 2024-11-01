package com.bca.byc.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantDto {
    private String secureId;
    private String name;
    private String email;
    private boolean isAdmin;
    private boolean isCreator;
}