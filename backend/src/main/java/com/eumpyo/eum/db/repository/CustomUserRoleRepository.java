package com.eumpyo.eum.db.repository;

public interface CustomUserRoleRepository {
    /**
     * @param baseUserId 현재 접속 유저의 id
     * @param targetUserId pin 작성자의 id
     * @return base유저에 대한 target유저의 호칭
     */
    String getUserRole(Long baseUserId, Long targetUserId);
}
