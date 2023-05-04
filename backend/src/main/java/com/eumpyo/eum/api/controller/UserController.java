package com.eumpyo.eum.api.controller;

import com.eumpyo.eum.api.request.UserRoleReq;
import com.eumpyo.eum.api.response.UserRes;
import com.eumpyo.eum.api.service.UserService;
import com.eumpyo.eum.common.code.SuccessCode;
import com.eumpyo.eum.common.response.ApiResponse;
import com.eumpyo.eum.db.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    ResponseEntity<ApiResponse> userFind(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        UserRes userRes = UserRes.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .birthYear(user.getBirthYear())
                .gender(user.getGender())
                .build();

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(userRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @PutMapping("/role")
    ResponseEntity<ApiResponse> userUpdate(@RequestBody UserRoleReq userRoleReq, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        userService.updateUserRole(userRoleReq, user);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.UPDATE.getCode())
                .resultMsg(SuccessCode.UPDATE.getCode())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.UPDATE.getStatus()));
    }
}