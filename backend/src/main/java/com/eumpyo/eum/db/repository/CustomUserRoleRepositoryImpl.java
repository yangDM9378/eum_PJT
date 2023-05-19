package com.eumpyo.eum.db.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.Optional;
import java.util.function.Function;

import static com.eumpyo.eum.db.entity.QUser.user;
import static com.eumpyo.eum.db.entity.QUserRole.userRole;

public class CustomUserRoleRepositoryImpl implements CustomUserRoleRepository {
    private final JPAQueryFactory queryFactory;

    public CustomUserRoleRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private <T> BooleanExpression condition(T value, Function<T, BooleanExpression> function) {
        return Optional.ofNullable(value).map(function).orElse(null);
    }

    @Override
    public String getUserRole(Long baseUserId, Long targetUserId) {
        return queryFactory
                .select(userRole.role)
                .from(userRole)
                .where(condition(baseUserId, userRole.baseUser.userId::eq),
                        condition(targetUserId, userRole.targetUser.userId::eq))
                .fetchOne();
    }
}
