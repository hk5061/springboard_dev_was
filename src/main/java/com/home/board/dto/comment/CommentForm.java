package com.home.board.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CommentForm {

    @NotBlank(message = "댓글 내용을 올바르게 적어주세요.")
    private String content; // 내용
    private String password;
    private Long postId; // 게시글 번호
    private Long parentId; // 댓글 부모번호 (대댓글이 아니면 null)
    private Long commentId;

    public boolean isParentIdEmpty() {
        return parentId == null;
    }
}
