package com.home.board.service;

import com.home.board.domain.member.Member;
import com.home.board.dto.member.MemberForm;
import com.home.board.dto.member.MemberInfoResponse;
import com.home.board.dto.response.Header;
import com.home.board.dto.response.ResponseData;
import com.home.board.repository.CommentRepository;
import com.home.board.repository.MemberRepository;
import com.home.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public void join(MemberForm memberForm) {
        Member member = Member.builder()
                .loginId(memberForm.getLoginId())
                .password(bCryptPasswordEncoder.encode(memberForm.getPassword()))
                .name(memberForm.getName())
                .role(memberForm.getRole())
                .build();
        memberRepository.save(member);
    }

    public ResponseData<String> loginIdDuplicate(String loginId) {
    	
    	logger.info("[MemberService] loginIdDuplicate > " + loginId);
    	
        if (memberRepository.findByLoginId(loginId).isPresent()) {
            return new ResponseData<>(Header.badRequest("존재하는 아이디입니다."), "");
        }
        return new ResponseData<>(Header.ok("사용가능한 아이디입니다."), "");
    }

    // 회원 상세정보
    public MemberInfoResponse getMemberInfo(Member member) {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(member);
        // 작성한 게시글 전체 조회
        List<MemberInfoResponse.PostDto> postDtos = postRepository.findByMember(member).stream()
                .map(MemberInfoResponse.PostDto::new)
                .collect(Collectors.toList());
        memberInfoResponse.setPostDtos(postDtos);
        memberInfoResponse.setPostCount(postDtos.size());

        // 작성한 댓글 전체 조회
        List<MemberInfoResponse.CommentDto> commentDtos = commentRepository.findByMember(member).stream()
                .map(MemberInfoResponse.CommentDto::new)
                .collect(Collectors.toList());
        memberInfoResponse.setCommentDtos(commentDtos);
        memberInfoResponse.setCommentCount(commentDtos.size());

        return memberInfoResponse;
    }
}
