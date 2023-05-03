package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.GroupAddReq;
import com.eumpyo.eum.api.response.GroupDetailsRes;
import com.eumpyo.eum.api.response.GroupListRes;
import com.eumpyo.eum.db.entity.User;

import java.util.List;

public interface GroupService {
    void addGroup(User user, GroupAddReq groupAddReq);

    GroupDetailsRes findGroup(Long groupId);

    void removeGroup(Long groupId);

    List<GroupListRes> findGroupList(User user);

    void joinGroup(User user, String groupCode);
}
