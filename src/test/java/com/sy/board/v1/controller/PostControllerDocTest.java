package com.sy.board.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.board.domain.Post;
import com.sy.board.dto.request.PostCreateDTO;
import com.sy.board.v1.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.sendkite.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("단건 조회")
    void hello() throws Exception {
        // given
        Post post = Post.builder()
                .title("hello~~~")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("index", RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("postId").description("게시글 ID")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("게시글 ID"),
                                PayloadDocumentation.fieldWithPath("title").description("글 제목"),
                                PayloadDocumentation.fieldWithPath("content").description("글 내용")
                        )
                ));
    }


    @Test
    @DisplayName("글 등록")
    void save() throws Exception {
        // given
        PostCreateDTO request = PostCreateDTO.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("index", PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("title").description("글 제목"),
                        PayloadDocumentation.fieldWithPath("content").description("글 내용")
                        )
                ));
    }
}
