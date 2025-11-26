package com.home.board.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.home.board.domain.comment.Comment;
import com.home.board.domain.member.Member;
import com.home.board.domain.post.File;
import com.home.board.domain.post.Post;
import com.home.board.dto.UploadFile;
import com.home.board.dto.comment.CommentResponse;
import com.home.board.dto.member.MemberInfoResponse;
import com.home.board.dto.post.FilesResponse;
import com.home.board.dto.post.PostEditResponse;
import com.home.board.dto.post.PostForm;
import com.home.board.dto.post.PostInfoResponse;
import com.home.board.dto.post.PostSearch;
import com.home.board.dto.post.PostsPageResponse;
import com.home.board.dto.response.Header;
import com.home.board.dto.response.ResponseData;
import com.home.board.exception.PostNotExistException;
import com.home.board.repository.CommentRepository;
import com.home.board.repository.FileRepository;
import com.home.board.repository.MemberRepository;
import com.home.board.repository.PostRepository;
import com.home.board.security.PrincipalDetails;
import com.home.board.util.FileStoreUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final FileStoreUtil fileStoreUtil;
    private final FileRepository fileRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public Long savePost(PostForm form, MemberInfoResponse memberInfo) throws IOException {
        Optional<Member> findMember = memberRepository.findById(memberInfo.getId());
        Post post = Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .viewCount(0)
                .category(form.getCategory())
                .member(findMember.get())
                .build();
        Post savePost = postRepository.save(post);
        List<UploadFile> storeImageFiles = fileStoreUtil.storeFiles(form.getImageFiles());
        storeImageFiles.forEach(file -> fileRepository.save(new File(file, savePost)));
        return savePost.getId();
    }

    public PostsPageResponse findPostsPaging(PostSearch postSearch, Pageable pageable) {
        return new PostsPageResponse(getPostsPage(postSearch, pageable));
    }

    public Page<Post> getPostsPage(PostSearch postSearch, Pageable pageable) {

        if (postSearch.isEmpty()) {
            return postRepository.findAllPaging(pageable);
        }

        if (postSearch.getPostTitle() == null || postSearch.getPostTitle().equals("")) {
            return postRepository.findPostsByCategory(postSearch, pageable);
        }

        if (postSearch.getPostCategory() == null) {
            return postRepository.findPostsByTitle(postSearch, pageable);
        }

        return postRepository.findPosts(postSearch, pageable);
    }

    public PostInfoResponse findPost(Long postId, HttpServletRequest request, HttpServletResponse response) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistException("게시글이 존재하지 않습니다."));
        viewCountValidation(post, request, response);
        List<FilesResponse> storeImageNames = fileRepository.findByPost(post).stream()
                .map(FilesResponse::new)
                .collect(Collectors.toList());
        List<CommentResponse> commentResponses = commentRepository.findCommentsForPost(post).stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
        return PostInfoResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .createdName(post.getCreatedBy())
                .createdDate(post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedDate(post.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .storeImageNames(storeImageNames)
                .comments(commentResponses)
                .build();
    }

    private void viewCountValidation(Post post, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElseGet(() -> new Cookie[0]);
        Cookie cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("postView"))
                .findFirst()
                .orElseGet(() -> {
                    post.addViewCount();
                    return new Cookie("postView", "[" + post.getId() + "]");
                });

        if (!cookie.getValue().contains("[" + post.getId() + "]")) {
            post.addViewCount();
            cookie.setValue(cookie.getValue() + "[" + post.getId() + "]");
        }

        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond)); // 오늘 하루 자정까지 남은 시간초 설정
        response.addCookie(cookie);
    }

    public PostEditResponse findUpdatePost(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        if (post == null) return null;
        List<File> files = fileRepository.findByPost(post);
        List<FilesResponse> storeImageNames = files.stream().map(FilesResponse::new).collect(Collectors.toList());
        return new PostEditResponse(post.getId(), post.getTitle(), post.getContent(), storeImageNames);
    }

    public ResponseData<String> updatePost(PostForm form, Long postId) throws IOException {
        Post post = postRepository.findById(postId)
                .map(p -> p.updateForm(form)) // Optional에 값이 있으면 연산 수행
                .orElseThrow(() -> new PostNotExistException("게시글이 존재하지 않습니다."));
        if (!form.getImageFiles().isEmpty()) {
            List<UploadFile> storeImageFiles = fileStoreUtil.storeFiles(form.getImageFiles());
            storeImageFiles.forEach(f -> fileRepository.save(new File(f, post)));
        }
        return new ResponseData<>(Header.ok("수정되었습니다."), "");
    }

    public ResponseData<String> deletePost(Long postId) {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = principalDetails.getMember();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistException("게시글이 존재하지 않습니다."));
        if (!member.getLoginId().equals(post.getMember().getLoginId()))
            return new ResponseData<>(Header.badRequest("등록한 회원이 아닙니다."), "");
        // 파일 삭제
        fileRepository.findByPost(post).forEach(file -> {
            String fullPath = fileStoreUtil.getFullPath(file.getStoreFileName());
            java.io.File imageFile = new java.io.File(fullPath);
            if (imageFile.exists()) imageFile.delete();
            fileRepository.delete(file);
        });
        // 댓글 삭제
        List<Comment> comments = commentRepository.findCommentsForPost(post);
        List<Comment> childs = comments.stream()
                .filter(c -> !c.getChild().isEmpty())
                .flatMap(c -> c.getChild().stream())
                .collect(Collectors.toList());

        commentRepository.deleteAllInBatch(childs);
        commentRepository.deleteAllInBatch(comments);
        postRepository.delete(post);
        return new ResponseData<>(Header.ok("게시글이 삭제되었습니다."), "");
    }

    public ResponseData<String> validatePost(Long postId) {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = principalDetails.getMember();
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        if (post == null) return new ResponseData<>(Header.badRequest("게시글이 존재하지 않습니다."), "");
        if (!member.getLoginId().equals(post.getMember().getLoginId()))
            return new ResponseData<>(Header.badRequest("등록한 회원이 아닙니다."), "");
        return new ResponseData<>(Header.ok("게시글을 등록한 회원입니다."), "");
    }
}
