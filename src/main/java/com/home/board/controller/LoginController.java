package com.home.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	

    @GetMapping("/login")
    public String login() {
    	
    	logger.info("[LoginController][login] login 화면 실행");
    	
        return "login"; // resources:templates/home.html 반환
    }
    
}
