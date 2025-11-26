package com.demo.home;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class SpringBootBoardApplicationTests {
	
	@Test
	void databaseConnection() {
		
		User user = new User();
		String name = user.getName();
		
	}

}
