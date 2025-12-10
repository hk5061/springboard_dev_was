package com.demo.home;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import com.home.board.api.MemberApiController;
import com.home.board.dto.response.ResponseData;
import com.home.board.service.MemberService;


@ContextConfiguration(classes = SpringBootBoardApplicationTests.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
class SpringBootBoardApplicationTests {
	
	
	private static final Logger logger = LoggerFactory.getLogger(SpringBootBoardApplicationTests.class);
	
	
	@Value("${spring.datasource.url}")
    private String URL;
    @Value("${spring.datasource.driver-class-name}")
    private String DRIVERNAME;
    
 
    @Test
    void connTest() throws SQLException, ClassNotFoundException {
    	
    	logger.info("URL : " + URL);
    	
        Class.forName(DRIVERNAME);
        try(Connection conn =  DriverManager.getConnection(URL, "root", "board1234")) {
            logger.info("connTest: " + conn);
        } catch(Exception e) {
        	logger.error("DB Connect Fail !!!");
        	
//            e.printStackTrace();
        }
    }
    
    private final MemberService memberService = null;
    
    
    @Test
    void loginIdTest() throws SQLException, ClassNotFoundException {
    	
    	try {
    		ResponseData<String> responseData = memberService.loginIdDuplicate("hk5061");
    	
    	} catch(Exception e) {
            e.printStackTrace();
        }
    	
    }

}
