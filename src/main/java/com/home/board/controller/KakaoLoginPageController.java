package com.home.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class KakaoLoginPageController {

	
	
	private static final Logger logger = LoggerFactory.getLogger(KakaoLoginPageController.class);
	
	
	@Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;


    @GetMapping("/kakao/oauth")
    public String loginPage(Model model) {
    	
    	
    	logger.info("[KakaoLoginPageController][loginPage] " + redirect_uri );
    	
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        model.addAttribute("location", location);

        return "kakaoLogin";
    }
    
    
    
	
}
