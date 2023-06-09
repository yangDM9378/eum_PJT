package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.PictureAddReq;
import com.eumpyo.eum.api.response.PictureDetailRes;
import com.eumpyo.eum.api.response.PictureGroupRes;
import com.eumpyo.eum.api.response.PicturePinRes;
import com.eumpyo.eum.db.entity.Picture;
import com.eumpyo.eum.db.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureService {

    List<PicturePinRes> findPicturePinList(Long pinId);
    PictureDetailRes findPictureDetail(Long pictureId);
    List<PictureGroupRes> findPictureGroupList(Long groupId);
    void addPicture(User user, PictureAddReq pictureAddReq, MultipartFile image);
    boolean removePicture(User user, Long pictureId);
}
