package com.home.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.board.service.KakaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(KakaoLoginController.class);
	
	
	private final KakaoService kakaoService;
	
	
	@GetMapping("/kakao/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
    	logger.info("[KakaoLoginPageController][callback] " + code );
    	
    	String accessToken = kakaoService.getAccessTokenFromKakao(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
