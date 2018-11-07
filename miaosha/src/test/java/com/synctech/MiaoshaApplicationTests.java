package com.synctech;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.synctech.domian.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MiaoshaApplicationTests {

	@Test
	public void contextLoads() {
		User user = User.builder().id(1).name("jim").build();
		System.out.println(user);
	}

}
