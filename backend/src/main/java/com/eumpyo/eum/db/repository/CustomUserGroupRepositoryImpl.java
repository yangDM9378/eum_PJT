package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.db.entity.QUser;
import com.eumpyo.eum.db.entity.UserGroup;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.function.Function;

import static com.eumpyo.eum.db.entity.QGroup.group;
import static com.eumpyo.eum.db.entity.QUser.user;
import static com.eumpyo.eum.db.entity.QUserGroup.userGroup;

public class CustomUserGroupRepositoryImpl implements CustomUserGroupRepository {
    private final JPAQueryFactory queryFactory;

    public CustomUserGroupRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private <T> BooleanExpression condition(T value, Function<T, BooleanExpression> function) {
        return Optional.ofNullable(value).map(function).orElse(null);
    }

    @Override
    public void deleteByGroup_GroupId(Long groupId) {
        queryFactory
                .delete(userGroup)
                .where(condition(groupId, userGroup.group.groupId::eq))
                .execute();
    }
}
