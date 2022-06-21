package com.sy.board.v1.controller;

import com.sy.board.dto.request.PostDTO;
import com.sy.board.v1.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Map get(@RequestBody @Valid PostDTO postDTO) {
        Long postId = postService.write(postDTO);
        return Map.of("postId", postId);
    }
}
