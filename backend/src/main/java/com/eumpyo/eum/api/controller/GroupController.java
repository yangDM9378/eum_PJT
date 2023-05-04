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

    @Value("${cloud.aws.directory}")
    String rootPath;

    private final GroupService groupService;

    private final S3Uploader s3Uploader;

    @PostMapping()
    ResponseEntity<ApiResponse> groupAdd(Authentication authentication, @RequestPart GroupAddReq groupAddReq, @RequestPart(value = "image", required = false) MultipartFile image) {
        User user = (User)authentication.getPrincipal();

        // 파일 S3에 저장
        if (image != null) {
            //make upload folder
            String uploadPath = rootPath + "/" + "group" + "/" + "image" + "/";
            File uploadFilePath = new File(rootPath, uploadPath);

            String fileName = image.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + fileName;

            try {
                log.debug(s3Uploader.upload(image, uploadPath + uploadFileName));
                groupAddReq.setImage(uploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        groupService.addGroup(user, groupAddReq);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.INSERT.getCode())
                .resultMsg(SuccessCode.INSERT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.INSERT.getStatus()));
    }

    @GetMapping("/")
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
}
