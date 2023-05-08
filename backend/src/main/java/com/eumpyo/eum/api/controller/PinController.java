package com.eumpyo.eum.api.controller;

import com.eumpyo.eum.api.request.GroupAddReq;
import com.eumpyo.eum.api.request.PinAddReq;
import com.eumpyo.eum.api.response.GroupListRes;
import com.eumpyo.eum.api.response.PinAlarmRes;
import com.eumpyo.eum.api.response.PinDetailsRes;
import com.eumpyo.eum.api.response.PinListRes;
import com.eumpyo.eum.api.service.PinService;
import com.eumpyo.eum.common.code.SuccessCode;
import com.eumpyo.eum.common.response.ApiResponse;
import com.eumpyo.eum.db.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pins")
public class PinController {
    private final PinService pinService;

    @PostMapping()
    ResponseEntity<ApiResponse> pinAdd(Authentication authentication, @RequestPart PinAddReq pinAddReq, @RequestPart(value = "image", required = false) MultipartFile image) {
        User user = (User)authentication.getPrincipal();

        pinService.addPin(user, pinAddReq, image);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(null)
                .resultCode(SuccessCode.INSERT.getCode())
                .resultMsg(SuccessCode.INSERT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.INSERT.getStatus()));
    }

    @GetMapping("/")
    ResponseEntity<ApiResponse> allPinList() {
        List<PinListRes> groupListRes = pinService.findAllPin();

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(groupListRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @GetMapping("/group/{groupId}")
    ResponseEntity<ApiResponse> groupPinList(Authentication authentication, @PathVariable Long groupId) {
        List<PinListRes> groupListRes = pinService.findGroupPin(groupId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(groupListRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<ApiResponse> userPinList(Authentication authentication) {
        User user = (User)authentication.getPrincipal();;

        List<PinListRes> groupListRes = pinService.findUserPin(user);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(groupListRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @GetMapping("/{pinId}")
    ResponseEntity<ApiResponse> pinDetails(Authentication authentication, @PathVariable("pinId") Long pinId) {
        User user = (User)authentication.getPrincipal();;

        PinDetailsRes pinDetailsRes = pinService.findPin(pinId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(pinDetailsRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @GetMapping("alarm/{pinId}")
    ResponseEntity<ApiResponse> pinAlarmDetails(Authentication authentication, @PathVariable("pinId") Long pinId) {
        User user = (User)authentication.getPrincipal();;

        PinAlarmRes pinAlarmRes = pinService.findPinAlarm(user, pinId);

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .result(pinAlarmRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }
}
