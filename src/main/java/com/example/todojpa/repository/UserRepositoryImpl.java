package com.example.todojpa.repository;

import com.example.todojpa.entity.QUser;
import com.example.todojpa.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements CustomUserRepository {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<User> search(String name) {
        QUser user = QUser.user;

        JPAQuery<User> query = queryFactory.selectFrom(user);
        if (name != null) {
            query.where(user.name.like("%" + name + "%"));
        }
        query.where(user.useYn.eq(true))
                .orderBy(user.updatedAt.desc());

        List<User> users = query.fetch();

        return users;
    }
}
