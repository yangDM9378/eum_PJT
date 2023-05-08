package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.UserRoleAddReq;
import com.eumpyo.eum.db.entity.User;

public interface UserService {
    void updateUserRole(UserRoleAddReq userRoleReq, User user);
}
