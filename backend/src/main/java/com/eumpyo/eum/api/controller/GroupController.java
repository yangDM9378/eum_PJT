package com.eumpyo.eum.api.controller;

import com.eumpyo.eum.api.request.GroupAddReq;
import com.eumpyo.eum.api.response.GroupDetailsRes;
import com.eumpyo.eum.api.service.GroupService;
import com.eumpyo.eum.common.code.SuccessCode;
import com.eumpyo.eum.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping()
    ResponseEntity<ApiResponse> groupAdd(@RequestBody GroupAddReq groupAddReq) {
        groupService.addGroup(groupAddReq);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.INSERT.getCode())
                .resultMsg(SuccessCode.INSERT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.INSERT.getStatus()));
    }

    @GetMapping("/{groupId}")
    ResponseEntity<ApiResponse> groupDetails(@PathVariable("groupId") Integer groupId) {
        GroupDetailsRes groupDetailsRes = groupService.findGroup(groupId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(groupDetailsRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @DeleteMapping("/{groupId}")
    ResponseEntity<ApiResponse> groupRemove(@PathVariable("groupId") Integer groupId) {
        groupService.removeGroup(groupId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.DELETE.getCode())
                .resultMsg(SuccessCode.DELETE.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.DELETE.getStatus()));
    }
}
