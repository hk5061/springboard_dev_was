package com.home.board.repository;

import com.home.board.domain.post.File;
import com.home.board.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    
    List<File> findByPost(Post post);
}
