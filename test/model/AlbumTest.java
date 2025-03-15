package model;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import java.time.Year;

import org.junit.jupiter.api.Test;

class AlbumTest {
	private Album a0 = new Album("Title", "Artist", "Genre", Year.parse("2021"));
	private Song s0 = new Song("Song0", a0);
	private Song s1 = new Song("Song1", a0);

	@Test
	void testConstructors() {
		Album a1 = new Album(a0);

		assertFalse(a0 == a1);
		assertTrue(a0.equals(a1));
		assertEquals(a0.toString(), "Title by Artist. Genre: Genre. Year: 2021.\n");
		assertEquals(a1.toString(), "Title by Artist. Genre: Genre. Year: 2021.\n");
	}
	
	@Test
	void testAddSong() {
		a0.addSong(s0);
		a0.addSong(s1);
		
		ArrayList<Song> arr = a0.getSongs();
		assertEquals(s0, arr.get(0));
		assertEquals(s1, arr.get(1));
		assertFalse(s0 == arr.get(0));
		assertFalse(s1 == arr.get(1));
		
		Album a1 = new Album(a0);
		arr = a1.getSongs();
		assertEquals(s0, arr.get(0));
		assertEquals(s1, arr.get(1));
		assertFalse(s0 == arr.get(0));
		assertFalse(s1 == arr.get(1));
		
		assertEquals(a0.toString(), "Title by Artist. Genre: Genre. Year: 2021.\n  - Song0\n  - Song1\n");
	}
	
	@Test
	void testStaticMethods() {
		Album a1 = Album.fromAlbumName("NEW ALBUM");
		assertEquals(a1.getAlbumName(), "NEW ALBUM");
		assertEquals(a1.getArtist(), "");
		assertEquals(a1.getGenre(), "");
		assertEquals(a1.getYear().toString(), "0");
		
		Album a2 = Album.fromArtist("NEW ARTIST");
		assertEquals(a2.getArtist(), "NEW ARTIST");
		assertEquals(a2.getAlbumName(), "");
		assertEquals(a2.getGenre(), "");
		assertEquals(a2.getYear().toString(), "0");
	}
	
	@Test
	void testEquals() {
		Album a1 = new Album(a0);
		Album a2 = new Album("Title", "Artist", "Genre", Year.parse("2021"));
		Album a3 = new Album("Other Title", "Other Artist", "Genre", Year.parse("2021"));
		Album a4 = new Album("Other Title", "Artist", "Genre", Year.parse("2021"));
		Album a5 = new Album("Title", "Artist", "Genre", Year.parse("2024"));

		
		assertFalse(a0.equals(null));
		assertFalse(a0.equals(s0));
		
		assertTrue(a0.equals(a1));
		assertTrue(a0.equals(a2));
		assertNotEquals(a0, a3);
		assertNotEquals(a0, a4);
		assertNotEquals(a0, a5);
	}

}
