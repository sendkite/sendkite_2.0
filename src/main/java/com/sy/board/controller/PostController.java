package com.sy.board.controller;

import com.sy.board.dto.request.PostDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public String get(@RequestBody @Valid PostDTO postDTO) {
        log.info("postDTO={}", postDTO.toString());
        return "hello world";
    }
}
