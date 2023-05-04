package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.db.entity.User;
import com.eumpyo.eum.db.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByBaseUserAndTargetUser(User baseUser, User targetUser);
}
