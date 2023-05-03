package com.eumpyo.eum.db.repository;

import com.eumpyo.eum.api.response.GroupDetailsRes;
import com.eumpyo.eum.api.response.GroupListRes;
import com.eumpyo.eum.db.entity.Group;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.eumpyo.eum.db.entity.QGroup.group;
import static com.eumpyo.eum.db.entity.QUserGroup.userGroup;

public class CustomGroupRepositoryImpl implements CustomGroupRepository {
    private final JPAQueryFactory queryFactory;

    public CustomGroupRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private <T> BooleanExpression condition(T value, Function<T, BooleanExpression> function) {
        return Optional.ofNullable(value).map(function).orElse(null);
    }

    @Override
    public List<GroupListRes> findGroupListByUser_UserId(Long userId) {
        return queryFactory
                .select(Projections.constructor(GroupListRes.class, group.name, group.createdDate, group.description, group.image))
                .from(group)
                .join(userGroup)
                .on(condition(group.groupId, userGroup.group.groupId::eq))
                .where(condition(userId, userGroup.user.userId::eq))
                .fetch();
    }

    @Override
    public Group findGroupByGroupCode(String groupCode) {
        return queryFactory
                .select(Projections.constructor(Group.class, group.groupId, group.name, group.createdDate, group.description, group.image, group.groupCode))
                .from(group)
                .where(condition(groupCode, group.groupCode::eq))
                .fetchOne();

    }
}
