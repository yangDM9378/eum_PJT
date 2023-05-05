package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.api.response.PinAlarmRes;
import com.eumpyo.eum.api.response.PinListRes;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.eumpyo.eum.db.entity.QPin.pin;
import static com.eumpyo.eum.db.entity.QUser.user;
import static com.eumpyo.eum.db.entity.QUserGroup.userGroup;
import static com.eumpyo.eum.db.entity.QUserRole.userRole;

public class CustomPinRepositoryImpl implements CustomPinRepository {
    private JPAQueryFactory queryFactory;

    public CustomPinRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private <T> BooleanExpression condition(T value, Function<T, BooleanExpression> function) {
        return Optional.ofNullable(value).map(function).orElse(null);
    }

    @Override
    public List<PinListRes> findByUser_UserId(Long userId) {
        return queryFactory
                .select(Projections.constructor(PinListRes.class, pin.pinId, pin.latitude, pin.longitude))
                .from(pin)
                .where(pin.group.groupId.in(
                        JPAExpressions
                                .select(userGroup.group.groupId)
                                .from(user)
                                .join(userGroup)
                                .on(condition(user.userId, userGroup.user.userId::eq))
                                .where(condition(userId, userGroup.user.userId::eq))))
                .fetch();
    }
}
