package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.api.response.PinAlarmRes;
import com.eumpyo.eum.api.response.PinListRes;

import java.util.List;

public interface CustomPinRepository {
    List<PinListRes> findByUser_UserId(Long userId);
}
