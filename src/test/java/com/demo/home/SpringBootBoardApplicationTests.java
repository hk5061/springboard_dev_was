package com.demo.home;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;



@ContextConfiguration(classes = SpringBootBoardApplicationTests.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
class SpringBootBoardApplicationTests {
	
	
	private static final Logger logger = LoggerFactory.getLogger(SpringBootBoardApplicationTests.class);
	
	
	@Value("${spring.datasource.url}")
    private String URL;
	
	@Value("${spring.datasource.username}")
    private String userName;
	
	@Value("${spring.datasource.password}")
    private String password;
	
	
    @Value("${spring.datasource.driver-class-name}")
    private String DRIVERNAME;
    
 
    @Test
    void connTest() throws SQLException, ClassNotFoundException {
    	
    	logger.info("URL : " + URL);
    	logger.info("userName : " + userName);
    	logger.info("password : " + password);
    	
    	userName = "boardAdmin";
    	password = "board1234!@";
    	
        Class.forName(DRIVERNAME);
        try(Connection conn =  DriverManager.getConnection(URL, userName, password)) {
            logger.info("connTest: " + conn);
        } catch(Exception e) {
        	logger.error("DB Connect Fail !!!");
        	
//            e.printStackTrace();
        }
    } 
    
   
    
    
    
    @Test
    @DisplayName("패스워드를 jasypt로 암호화")
    public void jasyptEncryptorPassword() {
        String key = "hk5061";

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(8);   // 코어 수
        encryptor.setPassword(key);
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");  // 암호화 알고리즘

        String str = "mmBctUNSr7IaNRExTmSTFBiBoUBjXYy4";
        String encryptStr = encryptor.encrypt(str);
        String decryptStr = encryptor.decrypt(encryptStr);

//        System.out.println("암호화 된 문자열 : " + encryptStr);
//        System.out.println("복호화 된 문자열 : " + decryptStr);
    }
    
    
    @Test
    @DisplayName("HMAC Test")
    public void hmacTest() {
    	
    	String key = "hk5061";

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(8);   // 코어 수
        encryptor.setPassword(key);
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");  // 암호화 알고리즘

        String str = "fWLZBBjfFeZwhtw8rzVO";
        String encryptStr = encryptor.encrypt(str);
        String decryptStr = encryptor.decrypt(encryptStr);

        System.out.println("암호화 된 문자열 : " + encryptStr);
        System.out.println("복호화 된 문자열 : " + decryptStr);
    	
    	
    }
    
    
    

}
