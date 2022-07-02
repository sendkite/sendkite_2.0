package com.sy.board.v1.service;

import com.sy.board.domain.Post;
import com.sy.board.dto.request.PostDTO;
import com.sy.board.dto.request.PostSearch;
import com.sy.board.dto.response.PostResponseDTO;
import com.sy.board.v1.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public PostResponseDTO get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponseDTO> getList(Pageable pageable) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        return postRepository.findAll(pageable).stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<PostResponseDTO> getListWithQueryDsl(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }
}
