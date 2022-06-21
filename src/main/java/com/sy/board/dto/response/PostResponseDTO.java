package com.sy.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponseDTO {

    private Long id;
    private String title;
    private String content;

    @Builder
    public PostResponseDTO(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}
