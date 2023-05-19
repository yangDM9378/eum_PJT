package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.api.response.PictureGroupRes;
import com.eumpyo.eum.api.response.PicturePinRes;
import com.eumpyo.eum.db.entity.Picture;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.eumpyo.eum.db.entity.QPicture.picture;
import static com.eumpyo.eum.db.entity.QPin.pin;
import static com.eumpyo.eum.db.entity.QGroup.group;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomPictureRepositoryImpl implements CustomPictureRepository{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<PicturePinRes> findByPinIdOrderByCreatedDateDesc(Long pinId) {

        List<PicturePinRes> pictures = jpaQueryFactory
                .select(Projections.constructor(PicturePinRes.class,picture.user.name, picture.createdDate, picture.picture_id, picture.image))
                .from(picture)
                .join(picture.pin, pin)
                .where(pin.pinId.eq(pinId))
                .orderBy(picture.createdDate.desc())
                .fetch();

        return pictures;
    }

    @Override
    public List<PictureGroupRes> findByGroupId(Long groupId) {

        List<PictureGroupRes> pictures = jpaQueryFactory
                .select(Projections.constructor(PictureGroupRes.class, picture.picture_id, picture.image))
                .from(picture)
                .join(picture.group, group)
                .where(group.groupId.eq(groupId))
                .fetch();

        return pictures;
    }

}
