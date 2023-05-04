package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.GroupAddReq;
import com.eumpyo.eum.api.response.GroupDetailsRes;
import com.eumpyo.eum.api.response.GroupListRes;
import com.eumpyo.eum.db.entity.Group;
import com.eumpyo.eum.db.entity.User;
import com.eumpyo.eum.db.entity.UserGroup;
import com.eumpyo.eum.db.repository.GroupRepository;
import com.eumpyo.eum.db.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    @Override
    @Transactional
    public void addGroup(User user, GroupAddReq groupAddReq) {
        String groupCode = UUID.randomUUID().toString().replaceAll("-", "");

        Group group = Group.builder()
                .name(groupAddReq.getName())
                .description(groupAddReq.getDescription())
                .image(groupAddReq.getImage())
                .groupCode(groupCode)
                .build();

        group = groupRepository.save(group);



        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .role("admin")
                .build();

        userGroupRepository.save(userGroup);
    }

    @Override
    public List<GroupListRes> findGroupList(User user) {
        List<GroupListRes> groupList = groupRepository.findGroupListByUser_UserId(user.getUserId());

        return groupList;
    }

    @Override
    @Transactional
    public void joinGroup(User user, String groupCode) {
        Group group = groupRepository.findGroupByGroupCode(groupCode);

        Optional<UserGroup> existUserGroup = userGroupRepository.findByUser_UserIdAndGroup_GroupId(user.getUserId(), group.getGroupId());

        if(existUserGroup.isPresent()) {
            throw new IllegalStateException("이미 해당 그룹에 존재합니다.");
        }

        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .role("user")
                .build();

        userGroupRepository.save(userGroup);
    }

    @Override
    public GroupDetailsRes findGroup(Long groupId) {
        // 그룹 없을 때 어떤 예외 발생
        Group group = groupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new IllegalStateException("해당하는 그룹이 존재하지 않습니다."));

        GroupDetailsRes groupDetailsRes = GroupDetailsRes.builder()
                .name(group.getName())
                .createdDate(group.getCreatedDate())
                .description(group.getDescription())
                .image(group.getImage())
                .groupCode(group.getGroupCode())
                .build();

        return groupDetailsRes;
    }

    @Override
    @Transactional
    public void removeGroup(Long groupId) {
        groupRepository.deleteById(groupId);
        userGroupRepository.deleteByGroup_GroupId(groupId);
    }

}
