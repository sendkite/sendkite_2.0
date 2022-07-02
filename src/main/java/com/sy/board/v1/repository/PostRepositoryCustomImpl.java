package com.sy.board.v1.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sy.board.domain.Post;
import com.sy.board.dto.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sy.board.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();
    }
}
