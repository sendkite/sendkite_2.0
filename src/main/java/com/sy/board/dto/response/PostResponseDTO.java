package com.sy.board.dto.response;

import com.sy.board.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponseDTO {

    private Long id;
    private String title;
    private String content;

    // 생성자 오버로딩
    public PostResponseDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    @Builder
    public PostResponseDTO(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}
