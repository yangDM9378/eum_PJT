package com.eumpyo.eum.api.controller;

import com.eumpyo.eum.api.response.UserResponse;
import com.eumpyo.eum.api.service.UserService;
import com.eumpyo.eum.common.code.SuccessCode;
import com.eumpyo.eum.common.response.ApiResponse;
import com.eumpyo.eum.db.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

//    @Autowired
//    private UserService userService;

    @GetMapping()
    ResponseEntity<ApiResponse> userFind(Authentication authentication){
        User user = (User) authentication.getPrincipal();

        UserResponse userResponse = UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .birthYear(user.getBirthYear())
                .gender(user.getGender())
                .build();

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(userResponse)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }
}
