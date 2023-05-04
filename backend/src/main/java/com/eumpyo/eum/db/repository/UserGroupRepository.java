package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.db.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup,Long>, CustomUserGroupRepository {
    Optional<UserGroup> findByUser_UserIdAndGroup_GroupId(Long userId, Long groupId);
}
