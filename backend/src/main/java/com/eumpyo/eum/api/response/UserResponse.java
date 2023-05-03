package com.eumpyo.eum.api.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private Long userId;
    private String name;
    private int birthYear;
    private int gender;
    private String email;

    @Builder
    public UserResponse(Long userId, String name, int birthYear, int gender, String email) {
        this.userId = userId;
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
        this.email = email;
    }
}
