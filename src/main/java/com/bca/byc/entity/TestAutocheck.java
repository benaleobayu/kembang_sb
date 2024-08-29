package com.bca.byc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "pre_registration")
public class TestAutocheck extends AbstractBaseEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "member")
    private String memberType;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    // member data
    @Column(name = "member_bank_account", length = 20)
    private String memberBankAccount;

    @Column(name = "member_cin", length = 20)
    private String memberCin;

    @Column(name = "member_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate memberBirthdate;
    // ---

    // child data
    @Column(name = "child_bank_account", length = 20, columnDefinition = "varchar(20) default '0'")
    private String childBankAccount;

    @Column(name = "child_cin", length = 20)
    private String childCin;

    @Column(name = "child_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate childBirthdate;
    // ---

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

}
