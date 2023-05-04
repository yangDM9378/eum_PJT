package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.PinAddReq;
import com.eumpyo.eum.api.response.PinListRes;
import com.eumpyo.eum.db.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PinService {
    void addPin(User user, PinAddReq pinAddReq, MultipartFile image);

    List<PinListRes> findAllPin();

    List<PinListRes> findGroupPin(Long groupId);

    List<PinListRes> findUserPin(User user);
}
