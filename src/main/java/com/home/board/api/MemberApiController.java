package com.home.board.api;

import com.home.board.dto.response.ResponseData;
import com.home.board.service.MemberService;
import com.home.board.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final ResponseUtil responseUtil;
    private final MemberService memberService;

    @GetMapping("/members/duplicate")
    public ResponseEntity<ResponseData<?>> loginIdDuplicate(@RequestParam("loginId") String loginId) {
        ResponseData<String> responseData = memberService.loginIdDuplicate(loginId);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }
}
