package com.sy.board.v1.controller;

import com.sy.board.domain.Post;
import com.sy.board.dto.request.PostDTO;
import com.sy.board.dto.response.PostResponseDTO;
import com.sy.board.v1.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Map post(@RequestBody @Valid PostDTO postDTO) {
        Long postId = postService.write(postDTO);
        return Map.of("postId", postId);
    }

    @GetMapping("/posts/{postId}")
    public PostResponseDTO getPost(@PathVariable Long postId) {
        return postService.get(postId);
    }
}
