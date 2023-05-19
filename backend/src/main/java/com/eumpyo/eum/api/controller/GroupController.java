package com.eumpyo.eum.api.controller;

import com.eumpyo.eum.api.request.GroupAddReq;
import com.eumpyo.eum.api.response.GroupDetailsRes;
import com.eumpyo.eum.api.response.GroupListRes;
import com.eumpyo.eum.api.service.GroupService;
import com.eumpyo.eum.common.code.SuccessCode;
import com.eumpyo.eum.common.response.ApiResponse;
import com.eumpyo.eum.common.util.S3Uploader;
import com.eumpyo.eum.db.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping()
    ResponseEntity<ApiResponse> groupAdd(Authentication authentication, @RequestPart GroupAddReq groupAddReq, @RequestPart(value = "image", required = false) MultipartFile image) {
        User user = (User)authentication.getPrincipal();

        groupService.addGroup(user, groupAddReq, image);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.INSERT.getCode())
                .resultMsg(SuccessCode.INSERT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.INSERT.getStatus()));
    }

    @GetMapping()
    ResponseEntity<ApiResponse> groupList(Authentication authentication) {
        User user = (User)authentication.getPrincipal();;

        List<GroupListRes> groupListRes = groupService.findGroupList(user);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(groupListRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @GetMapping("/{groupId}")
    ResponseEntity<ApiResponse> groupDetails(@PathVariable("groupId") Long groupId) {
        GroupDetailsRes groupDetailsRes = groupService.findGroup(groupId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(groupDetailsRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @PostMapping("/code")
    ResponseEntity<ApiResponse> groupJoin(Authentication authentication, @RequestBody Map<String, String> groupJoinReq) {
        User user = (User)authentication.getPrincipal();

        groupService.joinGroup(user, groupJoinReq.get("groupCode"));

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.INSERT.getCode())
                .resultMsg(SuccessCode.INSERT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.INSERT.getStatus()));
    }

    @DeleteMapping("/{groupId}")
    ResponseEntity<ApiResponse> groupRemove(@PathVariable("groupId") Long groupId) {
        groupService.removeGroup(groupId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.DELETE.getCode())
                .resultMsg(SuccessCode.DELETE.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.DELETE.getStatus()));
    }

    @DeleteMapping("/exit/{groupId}")
    ResponseEntity<ApiResponse> groupExit(Authentication authentication, @PathVariable("groupId") Long groupId) {
        User user = (User)authentication.getPrincipal();

        groupService.exitGroup(user, groupId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.DELETE.getCode())
                .resultMsg(SuccessCode.DELETE.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.DELETE.getStatus()));
    }
}
