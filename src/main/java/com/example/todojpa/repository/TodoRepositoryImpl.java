package com.example.todojpa.repository;

import com.example.todojpa.entity.QTodo;
import com.example.todojpa.entity.Todo;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

//구현체이름 중요 **Impl 로해야함
@Repository
public class TodoRepositoryImpl implements CustomTodoRepository {

    private final int DEFAULT_PAGE_SIZE = 10;
    private final int DEFAULT_PAGE_NUMBER = 0;
    private final JPAQueryFactory queryFactory;

    public TodoRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Todo> search(String userName, LocalDate updatedAt, Pageable pageable) {
        QTodo todo = QTodo.todo;
        JPAQuery<Todo> query = queryFactory.selectFrom(todo)
                .join(todo.user).fetchJoin();
        //카운트 쿼리? 일단 해보기
        JPAQuery<Long> countQuery = queryFactory.select(todo.count()).from(todo);

        //검색조건 : 작성자 이름
        if (userName != null) {
            query.where(todo.user.name.eq(userName));
            countQuery.where(todo.user.name.eq(userName));
        }

        //검색조건 : 수정일(작성일)
        if (updatedAt != null) {
            query.where(todo.updatedAt.between(updatedAt.atStartOfDay(), updatedAt.atTime(23, 59, 59)));
            countQuery.where(todo.updatedAt.between(updatedAt.atStartOfDay(), updatedAt.atTime(23, 59, 59)));
        }

        query.orderBy(todo.updatedAt.desc());

        if (pageable != null) {
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        } else query.offset(DEFAULT_PAGE_NUMBER).limit(DEFAULT_PAGE_SIZE);

        List<Todo> fetch = query.fetch();

        Long total = countQuery.fetchOne();

        //나중에 Pageable null체크 따로 빼주기
        return new PageImpl<>(fetch, pageable == null ? PageRequest.of(DEFAULT_PAGE_NUMBER,DEFAULT_PAGE_SIZE) : pageable, total == null ? 0 : total);

    }
}
