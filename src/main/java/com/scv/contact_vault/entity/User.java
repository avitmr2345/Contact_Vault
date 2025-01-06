package com.scv.contact_vault.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Column(length = 1000)
    private String about;

    @Column(length = 1000)
    private String profilePic;

    private String phoneNumber;

    @Builder.Default
    private boolean enabled = false;

    @Builder.Default
    private boolean emailVerified = false;

    @Builder.Default
    private boolean phoneVerified = false;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private Providers provider = Providers.SELF;

    private String providerUserId;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

}