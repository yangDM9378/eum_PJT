package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.api.response.PinListRes;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.eumpyo.eum.db.entity.QPin.pin;
import static com.eumpyo.eum.db.entity.QUser.user;
import static com.eumpyo.eum.db.entity.QUserGroup.userGroup;

@Slf4j
public class CustomPinRepositoryImpl implements CustomPinRepository {
    private JPAQueryFactory queryFactory;

    public CustomPinRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private <T> BooleanExpression condition(T value, Function<T, BooleanExpression> function) {
        return Optional.ofNullable(value).map(function).orElse(null);
    }

    @Override
    public List<PinListRes> findPinList(Long userId) {
        return queryFactory
                .select(Projections.constructor(PinListRes.class, pin.pinId, pin.latitude, pin.longitude, pin.type))
                .from(user)
                .innerJoin(userGroup)
                .on(condition(user, userGroup.user::eq))
                .innerJoin(pin)
                .on(condition(userGroup.group, pin.group::eq))
                .where(condition(userId, userGroup.user.userId::eq))
                .orderBy(pin.pinId.asc())
                .fetch();
    }
}
