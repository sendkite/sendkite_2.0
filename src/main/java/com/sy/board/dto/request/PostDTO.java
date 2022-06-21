package com.sy.board.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostDTO {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Builder
    public PostDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
    // 빌더의 장정
    // - 객체의 불변성 보장
    // - 가독성이 좋다. (값 생성의 유연함)
    // - 필요한 값만 받을 수 있다. (오버로딩 조건 검색)
}
