package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.db.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup,Long>, CustomUserGroupRepository {
}
