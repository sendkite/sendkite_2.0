package com.sy.board.v1.service;

import com.sy.board.domain.Post;
import com.sy.board.dto.request.PostDTO;
import com.sy.board.dto.response.PostResponseDTO;
import com.sy.board.v1.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void writePost() {
        // given
        PostDTO post = PostDTO.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        // when
        postService.write(post);

        // then
        assertThat(postRepository.count()).isEqualTo(1L);
        Post dbPost = postRepository.findAll().get(0);
        assertThat(dbPost.getTitle()).isEqualTo("제목입니다.");
        assertThat(dbPost.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 1건 조회")
    void getPost() {
        // given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        // when
        PostResponseDTO response = postService.get(post.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("제목입니다.");
        assertThat(response.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 목록 조회")
    void getPosts() {
        // given
        Post requestPost1 = Post.builder()
                .title("foo1")
                .content("bar1")
                .build();
        postRepository.save(requestPost1);

        Post requestPost2 = Post.builder()
                .title("foo2")
                .content("bar2")
                .build();
        postRepository.save(requestPost2);

        // when
        List<PostResponseDTO> posts = postService.getList();

        // then
        assertThat(postRepository.count()).isEqualTo(2L);
        assertThat(posts.size()).isEqualTo(2L);
    }
}