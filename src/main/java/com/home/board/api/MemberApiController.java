package com.home.board.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.board.dto.response.ResponseData;
import com.home.board.service.MemberService;
import com.home.board.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

	private static final Logger logger = LoggerFactory.getLogger(MemberApiController.class);
	
	
    private final ResponseUtil responseUtil;
    private final MemberService memberService;

    @GetMapping("/members/duplicate")
    public ResponseEntity<ResponseData<?>> loginIdDuplicate(@RequestParam("loginId") String loginId) {
    	
    	logger.info("[MemberApiController] loginIdDuplicate > 로그인 ID 가입여부 체크");
    	
    	
        ResponseData<String> responseData = memberService.loginIdDuplicate(loginId);
        
        logger.info("[MemberApiController] loginIdDuplicate > 로그인 ID 가입여부 체크 > " + responseData.getBody());
        
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }
    
}
