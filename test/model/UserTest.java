package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.FileWriter;
import java.io.File;
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
		assertEquals(User.validateLogin("username", "password"), true);
		assertEquals(User.validateLogin("user2", "thisisnotmypassword"), false);
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
	void testSaveLoad() {
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
		Album a = (Album) ms.findAlbumByTitle("21").toArray()[0];
		u.addAlbum(a);
		u.setFavorite(a.getSongs().get(0));
		u.setRating(a.getSongs().get(1), Rating.ONE);
		u.setRating(a.getSongs().get(2), Rating.TWO);
		u.setRating(a.getSongs().get(3), Rating.THREE);
		u.setRating(a.getSongs().get(4), Rating.FOUR);
		u.setRating(a.getSongs().get(5), Rating.FIVE);

		u.playSong(a.getSongs().get(0));

		u.saveData();
		User u0 = u;

		u = new User("user2", "pass2");
		a = (Album) ms.findAlbumByTitle("19").toArray()[0];
		u.addAlbum(a);
		u.setFavorite(a.getSongs().get(0));
		u.setFavorite(a.getSongs().get(1));
		u.createPlaylist("New Playlist");
		u.addSongToPlaylist("New Playlist", a.getSongs().get(0).getTitle(), a.getSongs().get(0).getArtist());

		u.saveData();

		ArrayList<User> arr = User.loadAllData("data/user_data", ms);
		User userTemp = arr.get(0);

		assertEquals(u0, userTemp);

		userTemp = arr.get(1);
		assertEquals(u, userTemp);
		
	}
	
	@Test
	void testEquals() {
		User u0 = new User("user", "pass");
		User u1 = new User("user2", "pass");

		assertFalse(u0.equals(null));
		assertFalse(u0.equals(u1));
		assertFalse(u0.equals(""));
	}

}
