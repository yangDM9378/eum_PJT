package com.eumpyo.eum.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserResponse {
    private String name;
    private int birthYear;
    private int gender;
    private String email;
}
