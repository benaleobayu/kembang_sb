package com.bca.byc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user_request_contact")
public class AppUserRequestContact extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_user_id", nullable = false)
    private Long appUserId;  // Store only the user ID

    // Mengganti kolom messages menjadi tipe TEXT
    @Column(name = "messages", columnDefinition = "TEXT", nullable = true)
    private String messages;  // Menggunakan String untuk menyimpan teks panjang
}
