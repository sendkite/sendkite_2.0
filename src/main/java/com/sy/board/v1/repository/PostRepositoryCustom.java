package com.sy.board.v1.repository;

import com.sy.board.domain.Post;
import com.sy.board.dto.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
