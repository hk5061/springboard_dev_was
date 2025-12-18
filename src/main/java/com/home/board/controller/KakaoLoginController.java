package com.home.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.board.dto.kakao.KakaoUserInfoResponseDto;
import com.home.board.service.KakaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(KakaoLoginController.class);
	
	
	private final KakaoService kakaoService;
	
	@Value("${kakao.secret_key}")
    private String kakao_secret_key;
	
	
	@GetMapping("/kakao/callback")
    public String callback(@RequestParam("code") String code) {
    	logger.info("[KakaoLoginPageController][callback] " + code );
    	
    	
    	String accessToken = kakaoService.getAccessTokenFromKakao(code, kakao_secret_key);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        
        // 로그인 처리
        
        return "redirect:/";
    }

}
