package com.eumpyo.eum.db.entity;

import com.eumpyo.eum.api.response.UserRes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "gender", columnDefinition="TINYINT(1)")
    private int gender;

    @Column(name = "email", length = 50)
    private String email;

    @Builder
    public User(Long userId, String name, int birthYear, int gender, String email) {
        this.userId = userId;
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
        this.email = email;
    }

    public UserRes UserToDto(){
        return UserRes
                .builder()
                .name(name)
                .birthYear(birthYear)
                .gender(gender)
                .email(email)
                .build();
    }
}
