package com.eumpyo.eum.api.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private String name;
    private int birthYear;
    private int gender;
    private String email;

    @Builder
    public UserResponse(String name, int birthYear, int gender, String email) {
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
        this.email = email;
    }
}
