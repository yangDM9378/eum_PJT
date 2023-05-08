package com.eumpyo.eum.api.request;

import lombok.Getter;

@Getter
public class UserRoleAddReq {
    Long userId;
    String role;
}
