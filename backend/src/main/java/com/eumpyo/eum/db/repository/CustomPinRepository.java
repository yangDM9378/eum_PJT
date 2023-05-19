package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.api.response.PinListRes;

import java.util.List;

public interface CustomPinRepository {
    List<PinListRes> findPinList(Long userId);
}
