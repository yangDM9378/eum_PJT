package com.eumpyo.eum.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private int birthYear;

    @Column
    private Boolean gender;

    @Column
    private String email;

    @Builder
    public User(String name, int birthYear, Boolean gender, String email) {
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
        this.email = email;
    }
}
