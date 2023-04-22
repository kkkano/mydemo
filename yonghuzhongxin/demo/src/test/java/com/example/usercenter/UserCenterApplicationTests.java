package com.example.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class UserCenterApplicationTests {


//
//	@Test
////	void testDigest() throws NoSuchAlgorithmException {
////		MessageDigest md5 = MessageDigest.getInstance("MD5");
////		byte[] bytes = md5.digest("abcd".getBytes(StandardCharsets.UTF_8));
////		String result = new String(bytes);
////		System.out.println(result);
////
////	}

@Test
void testDigest() throws NoSuchAlgorithmException {
	final String SALT = "wangxian";
	String newPassword = DigestUtils.md5DigestAsHex((SALT + "abcd" + "mypassword").getBytes());
	System.out.println(newPassword);

}


	@Test
	void contextLoads() {
	}

}
