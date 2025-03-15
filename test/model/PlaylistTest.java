package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Year;

import org.junit.jupiter.api.Test;

class PlaylistTest {
	private Playlist playlist = new Playlist("P0");
	private static Album a0 = new Album("Title", "Artist", "Genre", Year.parse("2021"));
	private static Song s0 = new Song("Title", a0);

	@Test
	void testName() {
		assertEquals(playlist.getName(), "P0");
	}
	
	@Test
	void testAdd1() {
		playlist.addSong(s0);
		assertEquals(playlist.getPlaylist().toString(), "[Title by Artist]");
		assertEquals(playlist.toString(), "P0\n  -Title by Artist");
	}
	
	@Test
	void testAdd2() {
		Song song = new Song("T2", a0);
		playlist.addSong(s0);
		playlist.addSong(song);
		assertEquals(playlist.getPlaylist().toString(), "[Title by Artist, T2 by Artist]");
		assertEquals(playlist.toString(), "P0\n  -Title by Artist\n  -T2 by Artist");
	}
	
	@Test
	void testAddSame() {
		playlist.addSong(s0);
		playlist.addSong(s0);
		assertEquals(playlist.getPlaylist().toString(), "[Title by Artist, Title by Artist]");
		assertEquals(playlist.toString(), "P0\n  -Title by Artist\n  -Title by Artist");
	}
	
	@Test
	void testRemove() {
		playlist.addSong(s0);
		assertEquals(playlist.getPlaylist().toString(), "[Title by Artist]");
		playlist.removeSong(s0);
		assertEquals(playlist.getPlaylist().toString(), "[]");
		assertEquals(playlist.toString(), "P0");
	}
	
	

}
