package com.home.board.api;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.board.dto.post.PostForm;
import com.home.board.dto.response.ResponseData;
import com.home.board.service.PostService;
import com.home.board.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostApiController {

    private final PostService postService;
    public final ResponseUtil responseUtil;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<?>> validatePost(@PathVariable Long postId) {
        ResponseData<String> responseData = postService.validatePost(postId);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<?>> updatePost(@ModelAttribute PostForm form,
                                                      @PathVariable("postId") Long postId) throws IOException, URISyntaxException {
        ResponseData<String> responseData = postService.updatePost(form, postId);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<?>> deletePost(@PathVariable("postId") Long postId) {
        ResponseData<String> responseData = postService.deletePost(postId);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }
}
