package com.home.board.repository;

import com.home.board.domain.comment.Comment;
import com.home.board.domain.member.Member;
import com.home.board.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query("select c from Comment c where c.parent is null and c.post =:post")
    List<Comment> findCommentsForPost(@Param("post") Post post);

    List<Comment> findByMember(Member member);
}
