package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.api.response.PicturePinRes;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.eumpyo.eum.db.entity.QPicture.picture;
import static com.eumpyo.eum.db.entity.QPin.pin;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomPictureRepositoryImpl implements CustomPictureRepository{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<PicturePinRes> findByPinId(Long pinId) {

        List<PicturePinRes> pictures = jpaQueryFactory
                .select(Projections.constructor(PicturePinRes.class, picture.picture_id, picture.image))
                .from(picture)
                .join(picture.pinId, pin)
                .where(pin.pinId.eq(pinId))
                .fetch();

        return pictures;
    }

}
