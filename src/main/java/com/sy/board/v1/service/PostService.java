package com.sy.board.v1.service;

import com.sy.board.domain.Post;
import com.sy.board.domain.PostEditor;
import com.sy.board.dto.request.PostCreateDTO;
import com.sy.board.dto.request.PostEditDTO;
import com.sy.board.dto.request.PostSearch;
import com.sy.board.dto.response.PostResponseDTO;
import com.sy.board.exception.PostNotFound;
import com.sy.board.v1.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long write(PostCreateDTO postDTO) {
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .build();
        postRepository.save(post);
        return post.getId();
    }

    public PostResponseDTO get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

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

    public void edit(Long id, PostEditDTO postEditDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
        PostEditor postEditor = editorBuilder
                .title(postEditDTO.getTitle())
                .content(postEditDTO.getContent())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }
}
