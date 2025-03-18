package model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.library.MusicStore;
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

	@Test
	void test() {
		try {
			FileWriter fileWriter = new FileWriter(new File("data/user_data"), false);			
			fileWriter.close();
			fileWriter = new FileWriter(new File("data/login"), false);			
			fileWriter.close();
		} catch (IOException e) {
			// This can never happen, the file is already created
			System.exit(1);
		} 
		
		MusicStore ms = new MusicStore("albums/albums.txt");
		User u = new User("user", "pass");
		Album a = ms.findAlbumByTitle("21").get(0);
		u.addAlbum(a);
		u.setFavorite(a.getSongs().get(0));
		u.setFavorite(a.getSongs().get(1));

		u.saveData();
		
		User u0 = u;
		
		u = new User("user2", "pass2");
		a = ms.findAlbumByTitle("19").get(0);
		u.addAlbum(a);
		u.setFavorite(a.getSongs().get(0));
		u.setFavorite(a.getSongs().get(1));

		u.saveData();
		
		ArrayList<User> arr = User.loadAllData("data/user_data", ms);
		User userTemp = arr.get(0);
		
		assertEquals(u0, userTemp);
		
		userTemp = arr.get(1);
		assertEquals(u, userTemp);
		
	}

}
