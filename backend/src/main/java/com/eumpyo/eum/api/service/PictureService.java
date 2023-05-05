package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.PictureAddReq;
import com.eumpyo.eum.db.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService {
    void addPicture(User user, PictureAddReq pictureAddReq, MultipartFile image);
}
