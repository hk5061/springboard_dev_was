package com.home.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {
	
	
	@GetMapping("/")
	public String index() {
		System.out.println("인덱스 페이지 호출");
		return "index";
	}

}

