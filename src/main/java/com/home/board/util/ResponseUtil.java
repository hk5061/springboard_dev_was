package com.home.board.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.home.board.dto.response.ResponseData;

@Component
public class ResponseUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);
	
	

    public ResponseEntity<ResponseData<?>> createResponseEntity(ResponseData<?> responseData, HttpHeaders headers) {
        String status = responseData.getHeader().getStatus();
        
        logger.info("[ResponseUtil] ResponseEntity > " + status);
        
        
        HttpStatus httpStatus;
        switch (status) {
            case "301":
                httpStatus = HttpStatus.MOVED_PERMANENTLY;
                break;
            case "400":
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            default:
                httpStatus = HttpStatus.OK;
                break;
        }
        return new ResponseEntity<>(responseData, headers, httpStatus);
    }
}
