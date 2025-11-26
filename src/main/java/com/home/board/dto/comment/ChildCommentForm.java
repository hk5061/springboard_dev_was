package com.home.board.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ChildCommentForm {

    @NotBlank(message = "대댓글 내용을 올바르게 적어주세요.")
    private String content;

    @NotNull(message = "대댓글 번호가 들어오지 않았습니다.")
    private Long childId;

    private String password;

}

