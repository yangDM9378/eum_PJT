package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.PinAddReq;
import com.eumpyo.eum.api.response.PinAlarmRes;
import com.eumpyo.eum.api.response.PinDetailsRes;
import com.eumpyo.eum.api.response.PinListRes;
import com.eumpyo.eum.db.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PinService {
    void addPin(User user, PinAddReq pinAddReq, MultipartFile image);

    List<PinListRes> findAllPin();

    List<PinListRes> findGroupPin(Long groupId);

    List<PinListRes> findUserPin(User user);

    PinDetailsRes findPin(Long pinId);

    PinAlarmRes findPinAlarm(User user, Long pinId);
}
