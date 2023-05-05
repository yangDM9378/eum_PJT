package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.api.response.PicturePinRes;
import com.eumpyo.eum.db.entity.Picture;

import java.util.List;

public interface CustomPictureRepository {
    List<PicturePinRes> findByPinId(Long pinId);
}
