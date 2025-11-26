package com.home.board.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class DeleteCommentForm {

    @NotNull(message = "댓글 번호가 들어오지 않았습니다.")
    private Long commentId;
    private String password;
}
