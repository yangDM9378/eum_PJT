package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.db.entity.UserGroup;

public interface CustomUserGroupRepository {
    void deleteByGroup_GroupId(Long groupId);
}
