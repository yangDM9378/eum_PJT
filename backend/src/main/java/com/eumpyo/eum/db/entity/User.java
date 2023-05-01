package com.eumpyo.eum.db.entity;

import com.eumpyo.eum.api.response.UserResponse;
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

    @Column(columnDefinition="TINYINT(1)")
    private int gender;

    @Column
    private String email;

    @Builder
    public User(String name, int birthYear, int gender, String email) {
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
        this.email = email;
    }

    public UserResponse UserToDto(){
        return UserResponse
                .builder()
                .name(name)
                .birthYear(birthYear)
                .gender(gender)
                .email(email)
                .build();
    }
}
