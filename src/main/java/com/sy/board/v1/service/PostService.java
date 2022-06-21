package com.sy.board.v1.service;

import com.sy.board.domain.Post;
import com.sy.board.dto.request.PostDTO;
import com.sy.board.v1.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long write(PostDTO postDTO) {
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .build();
        postRepository.save(post);
        return post.getId();
    }

    public Post get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        return post;
    }
}
