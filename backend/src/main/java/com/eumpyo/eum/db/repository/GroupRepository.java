package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.db.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> findByGroupId(Integer groupId);
}
