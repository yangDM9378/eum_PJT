package com.eumpyo.eum.api.controller;

import com.eumpyo.eum.api.request.GroupAddReq;
import com.eumpyo.eum.api.response.GroupDetailsRes;
import com.eumpyo.eum.api.service.GroupService;
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
        groupService.groupAdd(groupAddReq);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(200)
                .resultMsg("그룹 생성에 성공했습니다.")
                .build();

        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    ResponseEntity<ApiResponse> groupDetails(@PathVariable("groupId") Integer groupId) {
        GroupDetailsRes groupDetailsRes = groupService.findGroup(groupId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(groupDetailsRes)
                .resultCode(200)
                .resultMsg("그룹 조회에 성공했습니다.")
                .build();

        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    ResponseEntity<ApiResponse> groupRemove(@PathVariable("groupId") Integer groupId) {
        groupService.groupRemove(groupId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(200)
                .resultMsg("그룹 삭제에 성공했습니다.")
                .build();

        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }
}
