package com.sy.board.v1.controller;

import com.sy.board.dto.request.PostDTO;
import com.sy.board.dto.request.PostEditDTO;
import com.sy.board.dto.request.PostSearch;
import com.sy.board.dto.response.PostResponseDTO;
import com.sy.board.v1.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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

    @GetMapping("/posts")
    public List<PostResponseDTO> getPosts(@PageableDefault(size = 5) Pageable pageable) {
        return postService.getList(pageable);
    }

    @GetMapping("/v2/posts")
    public List<PostResponseDTO> getPostsWithQueryDsl(@ModelAttribute PostSearch postSearch) {
        return postService.getListWithQueryDsl(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void editPost(@PathVariable Long postId, @RequestBody @Valid PostEditDTO postEditDTO) {
        postService.edit(postId, postEditDTO);
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
