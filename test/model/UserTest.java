package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.library.User;

class UserTest {
	private User user = new User("username", "password");
	private User user2 = new User("user2", "thisismypassword!");

	
	@Test
	void testValidateLogin() {
		assertEquals(user.validateLogin("uname", "password"), true);
		assertEquals(user2.validateLogin("user2", "thisisnotmypassword"), false);
	}
	
	@Test
	void testGetUsername() { 
		assertEquals(user.getUsername(), "username");
		assertFalse(user2.getUsername().equals("username2")); 
	}
	
	@Test
	void testGetPassword() { 
		assertEquals(user.getPassword().substring(0, user.getPassword().length() - 64), "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");		
		assertFalse(user2.getPassword().substring(0, user2.getPassword().length() - 64).equals("definitely not the right password"));
		
	}
	
	
	
}

	
