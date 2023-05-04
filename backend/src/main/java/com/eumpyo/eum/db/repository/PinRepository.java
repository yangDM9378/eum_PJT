package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.db.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PinRepository extends JpaRepository<Pin,Long>, CustomPinRepository {
    List<Pin> findByGroup_GroupId(Long groupId);
}
