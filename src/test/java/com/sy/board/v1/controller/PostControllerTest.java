package com.sy.board.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.board.domain.Post;
import com.sy.board.dto.request.PostDTO;
import com.sy.board.v1.repository.PostRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 동작")
    void testPostCtr() throws Exception {
        // given
        PostDTO req = PostDTO.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"postId\":1}"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 제목은 필수")
    void savePostWithoutTitle() throws Exception {
        // given
        PostDTO req = PostDTO.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("제목은 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB 값 저장")
    void savePostWithDB() throws Exception {
        // given
        PostDTO req = PostDTO.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(req);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
        // then
        assertThat(postRepository.count()).isEqualTo(1L);
        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 1건 조회")
    void getPost() throws Exception {
        // given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        // when + then
        mockMvc.perform(get("/posts/{postId}", post.getId())
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 목록 1페이지 조회")
    void getPosts() throws Exception {
        // given
        List<Post> reqPosts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목 " + i)
                                .content("내용 " + i)
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(reqPosts);


        // when + then
        mockMvc.perform(get("/posts?page=0&sort=id,desc&size=5")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("제목 30"))
                .andExpect(jsonPath("$[0].content").value("내용 30"))
                .andExpect(jsonPath("$[4].id").value(26))
                .andExpect(jsonPath("$[4].title").value("제목 26"))
                .andExpect(jsonPath("$[4].content").value("내용 26"))
                .andDo(print());
    }

    @Test
    @DisplayName("쿼리 DSL 글 목록 1페이지 조회")
    void getPostsV2() throws Exception {
        // given
        List<Post> reqPosts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("제목 " + i)
                                .content("내용 " + i)
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(reqPosts);


        // when + then
        mockMvc.perform(get("/v2/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("제목 30"))
                .andExpect(jsonPath("$[0].content").value("내용 30"))
                .andExpect(jsonPath("$[4].id").value(26))
                .andExpect(jsonPath("$[4].title").value("제목 26"))
                .andExpect(jsonPath("$[4].content").value("내용 26"))
                .andDo(print());
    }
}