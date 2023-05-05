package com.eumpyo.eum.api.controller;

import com.eumpyo.eum.api.request.PictureAddReq;
import com.eumpyo.eum.api.response.PictureDetailRes;
import com.eumpyo.eum.api.response.PictureGroupRes;
import com.eumpyo.eum.api.response.PicturePinRes;
import com.eumpyo.eum.api.service.PictureService;
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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/pictures")
public class PictureController {

    private final PictureService pictureService;
    @GetMapping("/pin/{pin_id}")
    ResponseEntity<ApiResponse> picturePinListFind(@PathVariable("pin_id") Long pinId) {
        List<PicturePinRes> picturePinResList = pictureService.findPicturePinList(pinId);

        ApiResponse apiResponse = ApiResponse
                .builder()
                .result(picturePinResList)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @GetMapping("/{picture_id}")
    ResponseEntity<ApiResponse> pictureDetailFind(@PathVariable("picture_id") Long pictureId) {
        PictureDetailRes pictureDetailRes = pictureService.findPictureDetail(pictureId);

        ApiResponse apiResponse = ApiResponse
                .builder()
                .result(pictureDetailRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @GetMapping("/group/{group_id}")
    ResponseEntity<ApiResponse> pictureGroupListFind(@PathVariable("group_id") Long groupId) {
        List<PictureGroupRes> pictureGroupRes = pictureService.findPictureGroupList(groupId);

        ApiResponse apiResponse = ApiResponse
                .builder()
                .result(pictureGroupRes)
                .resultCode(SuccessCode.SELECT.getCode())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.SELECT.getStatus()));
    }

    @PostMapping()
    ResponseEntity<ApiResponse> pictureAdd(
            Authentication authentication,
            @RequestPart PictureAddReq pictureAddReq,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        User user = (User)authentication.getPrincipal();

        pictureService.addPicture(user, pictureAddReq, image);

        ApiResponse apiResponse = ApiResponse
                .builder()
                .result(null)
                .resultCode(SuccessCode.INSERT.getCode())
                .resultMsg(SuccessCode.INSERT.getMessage())
                .build();

        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(SuccessCode.INSERT.getStatus()));
    }

    @DeleteMapping("/{picture_id}")
    ResponseEntity<ApiResponse> pictureRemove(@PathVariable("picture_id") Long pictureId ) {
        log.info(String.valueOf(pictureId));
        return null;
    }
}
