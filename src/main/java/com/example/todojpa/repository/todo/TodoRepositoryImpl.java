package com.example.todojpa.repository.todo;

import com.example.todojpa.dto.response.todo.TodoDetail;
import com.example.todojpa.entity.QComment;
import com.example.todojpa.entity.QTodo;
import com.example.todojpa.entity.Todo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.todojpa.entity.QTodo.todo;
import static com.example.todojpa.entity.QUser.user;
import static com.example.todojpa.entity.QComment.comment1;

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
    public Page<TodoDetail> search(String userName, LocalDate updatedAt, Pageable pageable) {

        List<TodoDetail> todoDetails = queryFactory.select(Projections.constructor(TodoDetail.class, todo, comment1.id.count()))
                .from(todo)
                .join(todo.user).fetchJoin()
                .leftJoin(todo, comment1.todo)
                .where(eqUserName(userName), inDate(updatedAt))
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .groupBy(todo.id)
                .orderBy(todo.updatedAt.desc())
                .fetch();

        Long total = queryFactory.select(todo.count())
                .from(todo)
                .where(eqUserName(userName), inDate(updatedAt))
                .fetchOne();


        //나중에 Pageable null체크 따로 빼주기
        return new PageImpl<>(todoDetails, pageable == null ? PageRequest.of(DEFAULT_PAGE_NUMBER,DEFAULT_PAGE_SIZE) : pageable, total == null ? 0 : total);
    }

    private BooleanExpression eqUserName(String userName) {
        if(userName == null) return null;
        return todo.user.name.eq(userName);
    }

    private BooleanExpression inDate(LocalDate date) {
        if(date == null) return null;
        return todo.updatedAt.between(date.atStartOfDay(), date.atTime(23, 59, 59));
    }
}
