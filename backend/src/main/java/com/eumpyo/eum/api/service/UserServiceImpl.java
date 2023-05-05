package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.UserRoleAddReq;
import com.eumpyo.eum.db.entity.User;
import com.eumpyo.eum.db.entity.UserRole;
import com.eumpyo.eum.db.repository.UserRepository;
import com.eumpyo.eum.db.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRoleRepository userRoleRepository;

    private final UserRepository userRepository;

    @Override
    public void updateUserRole(UserRoleAddReq userRoleAddReq, User user) {
        User targetUser = userRepository.findById(Long.valueOf(userRoleAddReq.getUserId()))
                .orElseThrow(() ->  new IllegalStateException("해당 유저가 존재하지 않습니다."));

        UserRole userRole;
        Optional<UserRole> userRoleOptional = userRoleRepository.findByBaseUserAndTargetUser(user, targetUser);

        if (userRoleOptional.isPresent()) {
            userRole = userRoleOptional.get();
            userRole.changeRole(userRoleAddReq.getRole());
        } else {
            userRole = UserRole.builder()
                    .baseUser(user)
                    .targetUser(targetUser)
                    .role(userRoleAddReq.getRole())
                    .build();
        }

        userRoleRepository.save(userRole);
    }
}
