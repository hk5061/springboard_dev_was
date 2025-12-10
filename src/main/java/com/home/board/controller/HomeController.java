package com.home.board.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	

    @GetMapping("/")
    public String index() {
    	
    	logger.info("home 화면 실행");
    	
        return "home"; // resources:templates/home.html 반환
    }
    
}
