package com.sy.board.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostEditDTO {

    private String title;
    private String content;


    @Builder
    public PostEditDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
