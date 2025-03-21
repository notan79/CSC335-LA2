package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;


class SongTest {
	private static Album a0 = new Album("Title", "Artist", "Genre", Year.parse("2021"));
	private static Song s0 = new Song("Title", a0);
	
	@Test 
	void test() {
		
		Song s1 = new Song("FOUR", a0);
		Song s2 = new Song("THREE", a0);
		Song s3 = new Song("TWO", a0);
		Song s4 = new Song("ONE", a0);
		Song s5 = new Song("NONE", a0);
		
		s0.setRating(Rating.FIVE);
		s1.setRating(Rating.FOUR);
		s2.setRating(Rating.THREE);
		s3.setRating(Rating.TWO);
		s4.setRating(Rating.ONE);

		ArrayList<Song> temp = new ArrayList<>();
		temp.add(s0);
		temp.add(s1);
		temp.add(s2);
		temp.add(s3);
		temp.add(s4);
		temp.add(s5);
		Collections.shuffle(temp);

		
		Collections.sort(temp, new Compare.CompareSongByRating());
		
		assertEquals(temp.get(0).getTitle(), "Title");
		assertEquals(temp.get(1).getTitle(), "FOUR");
		assertEquals(temp.get(2).getTitle(), "THREE");
		assertEquals(temp.get(3).getTitle(), "TWO");
		assertEquals(temp.get(4).getTitle(), "ONE");
		assertEquals(temp.get(5).getTitle(), "NONE");

	}

	@Test
	void testConstructors() {
		Song s1 = new Song("Title1", a0);
		Song s2 = new Song(s1);
		assertEquals(s0.toString(), "Title by Artist");
		assertEquals(s1.toString(), "Title1 by Artist");
		assertEquals(s2.toString(), "Title1 by Artist");

		assertNotEquals(s0, s1);
		assertNotEquals(s0, s2);
		assertFalse(s0 == s1);
		assertFalse(s0 == s2);

		assertEquals(s1, s2);
		assertFalse(s1 == s2);
	}

	@Test
	void testStaticMethods() {
		Song s1 = Song.fromTitle("Title");
		assertEquals(s1.getTitle(), "Title");
		assertNull(s1.getAlbum());
		assertNotEquals(s0, s1);

		Song s2 = Song.fromAlbum(a0);
		assertEquals(s2.getArtist(), "Artist");
		assertEquals(s2.getTitle(), "");
		assertNotEquals(s0, s2);
	}

	@Test
	void testRating() {
		assertEquals(s0.getRating(), Rating.FIVE);

		s0.setRating(Rating.THREE);
		assertEquals(s0.getRating(), Rating.THREE);

		s0.setRating(Rating.TWO);
		assertEquals(s0.getRating(), Rating.TWO);

	}

	@Test
	void testGetAlbum() {
		Song s1 = new Song("Title1", a0);
		assertEquals(s1.getAlbum().toString(), "Title by Artist. Genre: Genre. Year: 2021.\n");
		assertEquals(s1.getAlbum(), s0.getAlbum());
		assertFalse(s0.getAlbum() == s1.getAlbum());
	}

	@Test
	void testFavorite() {
		Song song = new Song("RandomTitle", a0);
		assertFalse(song.isFavorite());
		song.setFavorite();
		assertTrue(song.isFavorite());

		Song s1 = new Song("Title1", a0);
		assertFalse(s1.isFavorite());
		s1.setRating(Rating.ONE);
		assertFalse(s1.isFavorite());
		s1.setRating(Rating.TWO);
		assertFalse(s1.isFavorite());
		s1.setRating(Rating.THREE);
		assertFalse(s1.isFavorite());
		s1.setRating(Rating.FOUR);
		assertFalse(s1.isFavorite());
		s1.setRating(Rating.NONE);
		assertFalse(s1.isFavorite());
		s1.setRating(Rating.FIVE);
		assertTrue(s1.isFavorite());
		s1.setRating(Rating.NONE);
		assertTrue(s1.isFavorite());
	}

	@Test
	void testEquals() {
		assertNotEquals(s0, null);

		Song s1 = Song.fromTitle("");
		assertNotEquals(s0, s1);
		assertNotEquals(s0, a0);
		assertNotEquals(s1, s0);

		s1 = new Song(s0);
		Song s2 = new Song("Title1", a0);
		Song s3 = new Song("Title1", Album.fromArtist("Other Artist"));

		assertEquals(s0, s1);
		assertNotEquals(s0, s2);
		assertNotEquals(s0, s3);
	}

}
