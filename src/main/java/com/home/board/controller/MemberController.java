package com.home.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.home.board.dto.member.MemberForm;
import com.home.board.dto.member.MemberInfoResponse;
import com.home.board.security.PrincipalDetails;
import com.home.board.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
    private final MemberService memberService;

    // 회원가입 페이지
    @GetMapping("/members/new")
    public String createForm(Model model) {
    	logger.info("========== 회원가입 화면이동 ==========");
        model.addAttribute("memberForm", new MemberForm());
        return "member/member-form";
    }

    // 회원 가입
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
    	logger.info("========== 회원가입 ==========");
    	
        if (result.hasErrors()) {
            return "member/member-form";
        }
        
        memberService.join(form);
        return "redirect:/";
    }

    // 회원 상세조회 페이지
    @GetMapping("/members")
    public String info(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(principalDetails.getMember());
        model.addAttribute("member", memberInfoResponse);
        return "member/member-info";
    }
}
