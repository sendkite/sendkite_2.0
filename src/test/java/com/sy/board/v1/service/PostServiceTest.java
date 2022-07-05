package com.sy.board.v1.service;

import com.sy.board.domain.Post;
import com.sy.board.dto.request.PostCreateDTO;
import com.sy.board.dto.request.PostEditDTO;
import com.sy.board.dto.request.PostSearch;
import com.sy.board.dto.response.PostResponseDTO;
import com.sy.board.exception.PostNotFound;
import com.sy.board.v1.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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
        PostCreateDTO post = PostCreateDTO.builder()
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
    @DisplayName("글 1페이지 목록 조회")
    void getPosts() {
        // given
        List<Post> reqPosts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목 " + i)
                                .content("내용 " + i)
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(reqPosts);

        // when
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        List<PostResponseDTO> posts = postService.getList(pageable);

        // then
        assertThat(posts.size()).isEqualTo(5);
        assertThat(posts.get(0).getTitle()).isEqualTo("제목 30");
        assertThat(posts.get(4).getTitle()).isEqualTo("제목 26");

    }

    @Test
    @DisplayName("글 1페이지 목록 조회")
    void getPostsV2() {
        // given
        List<Post> reqPosts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목 " + i)
                                .content("내용 " + i)
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(reqPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();
        List<Post> posts = postRepository.getList(postSearch);

        // then
        assertThat(posts.size()).isEqualTo(10);
        assertThat(posts.get(0).getTitle()).isEqualTo("제목 30");
        assertThat(posts.get(4).getTitle()).isEqualTo("제목 26");
    }


    @Test
    @DisplayName("글 제목 수정 테스트")
    void editPost() {
        // given
       Post post = Post.builder()
               .title("스프링 연습")
               .content("스프링 어려워")
               .build();
        postRepository.save(post);

        PostEditDTO postEdit = PostEditDTO.builder()
                .title("연습 끝")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFound::new);
        assertThat(changedPost.getTitle()).isEqualTo("연습 끝");
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
        // given
        Post post = Post.builder()
                .title("스프링 연습")
                .content("스프링 어려워")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertThat(postRepository.count()).isEqualTo(0);

    }

    @Test
    @DisplayName("글 1건 조회 -- 실패")
    void getPost2() {
        // given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        // when + then
        Assertions.assertThrows(PostNotFound.class, () -> postService.get(post.getId() + 1L));
    }

    @Test
    @DisplayName("글 제목 수정 테스트 -- 실패")
    void editPostFail() {
        // given
        Post post = Post.builder()
                .title("스프링 연습")
                .content("스프링 어려워")
                .build();
        postRepository.save(post);

        PostEditDTO postEdit = PostEditDTO.builder()
                .title("연습 끝")
                .build();

        // when + then

        Assertions.assertThrows(PostNotFound.class, () -> postService.edit(post.getId() + 1L , postEdit));
    }

    @Test
    @DisplayName("게시글 삭제 -- 실패")
    void deletePostFail() {
        // given
        Post post = Post.builder()
                .title("스프링 연습")
                .content("스프링 어려워")
                .build();
        postRepository.save(post);

        // when + then
        Assertions.assertThrows(PostNotFound.class, () -> postService.delete(post.getId() + 1L));
    }
}