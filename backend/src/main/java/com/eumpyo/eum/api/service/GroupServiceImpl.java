package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.GroupAddReq;
import com.eumpyo.eum.api.response.GroupDetailsRes;
import com.eumpyo.eum.db.entity.Group;
import com.eumpyo.eum.db.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Override
    public void addGroup(GroupAddReq groupAddReq) {
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
        // 그룹 없을 때 어떤 예외 발생
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
    public void removeGroup(Integer groupId) {
        groupRepository.deleteById(groupId);
    }
}
