package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.GroupAddReq;
import com.eumpyo.eum.api.response.GroupDetailsRes;
import com.eumpyo.eum.db.entity.Group;
import com.eumpyo.eum.db.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Override
    public void groupAdd(GroupAddReq groupAddReq) {
        Group group = Group.builder()
                .name(groupAddReq.getName())
                .createdDate(groupAddReq.getCreatedDate())
                .description(groupAddReq.getDescription())
                .image(groupAddReq.getImage())
                .build();

        groupRepository.save(group);
    }

    @Override
    public GroupDetailsRes findGroup(Integer groupId) {
        Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() ->new IllegalStateException(""));

        GroupDetailsRes groupDetailsRes = GroupDetailsRes.builder()
                .name(group.getName())
                .createdDate(group.getCreatedDate())
                .description(group.getDescription())
                .image(group.getImage())
                .build();

        return groupDetailsRes;
    }

    @Override
    public void groupRemove(Integer groupId) {
        groupRepository.deleteById(groupId);
    }
}
