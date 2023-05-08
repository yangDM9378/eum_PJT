package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.api.response.GroupListRes;
import com.eumpyo.eum.db.entity.Group;

import java.util.List;

public interface CustomGroupRepository {
    /**
     * @param userId
     * @return user가 속한 모든 그룹 리스트
     */
    List<GroupListRes> findGroupListByUser_UserId(Long userId);


    /**
     *
     * @param groupCode
     * @return groupCode에 해당하는 그룹id
     */
    Group findGroupByGroupCode(String groupCode);
}
