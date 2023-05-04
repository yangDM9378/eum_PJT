package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.UserRoleReq;
import com.eumpyo.eum.db.entity.User;

public interface UserService {
    void updateUserRole(UserRoleReq userRoleReq, User user);
}
