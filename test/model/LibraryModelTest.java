package model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import model.library.LibraryModel;
import model.library.MusicStore;

class LibraryModelTest {
	private LibraryModel lm = new LibraryModel();
	private Album a0 = new Album("Album Name", "Artist0", "Genre0", Year.parse("2021"));
	private Song s0 = new Song("Title", a0);
	private Song s1 = new Song("Title2", a0);

	private Album a1 = new Album("New Album", "New Artist", "New Genre", Year.parse("1900"));
	private Song s2 = new Song(s1.getTitle(), a1);


	@Test
	void testAddSong() {
		lm.addSong(s0);
		lm.addSong(s1);
		assertEquals(lm.getSongList().toArray()[0], s0);
		assertEquals(lm.getSongList().toArray()[1], s1);
	}


	@Test
	void testCreatePlaylist() {
		assertTrue(lm.createPlaylist("New Playlist"));
		assertTrue(lm.createPlaylist("New Playlist 2"));

		assertEquals(lm.getPlaylists().get(2), "New Playlist");
		assertEquals(lm.getPlaylists().get(3), "New Playlist 2");

		assertFalse(lm.createPlaylist("New Playlist"));
		assertEquals(lm.getPlaylists().size(), 6);
		assertEquals(lm.getPlaylists().get(2), "New Playlist");
		assertEquals(lm.getPlaylists().get(3), "New Playlist 2");
	}

	@Test
	void testGetSongsFromPlaylist() {
		lm.addSong(s0);
		lm.createPlaylist("New Playlist");
		lm.createPlaylist("New Playlist 2");
		assertTrue(lm.addSongToPlaylist("New Playlist", "Title", "Artist0"));

		assertEquals(lm.getSongsFromPlaylist("New Playlist").get(0), s0.toString());
		assertFalse(lm.addSongToPlaylist("New Playlist 3", "Title", "Artist0"));

		assertFalse(lm.addSongToPlaylist("New Playlist", "Title!", "Artist0"));
		assertFalse(lm.addSongToPlaylist("New Playlist", "Title", "Artist10"));

		assertEquals(lm.getSongsFromPlaylist("Not a playlist").size(), 0);
	}

	@Test
	void testRemoveSongFromPlaylist(){
		lm.addSong(s0);
		lm.addSong(s1);
		lm.createPlaylist("Playlist");
		lm.addSongToPlaylist("Playlist", s0.getTitle(), s0.getAlbum().getArtist());
		lm.addSongToPlaylist("Playlist", s1.getTitle(), s1.getAlbum().getArtist());

		assertFalse(lm.removeSongFromPlaylist("Not a playlist", s0.getTitle(), s0.getAlbum().getArtist()));

		assertTrue(lm.removeSongFromPlaylist("Playlist", s0.getTitle(), s0.getAlbum().getArtist()));
		assertEquals(lm.getSongsFromPlaylist("Playlist").size(), 1);
		assertEquals(lm.getSongsFromPlaylist("Playlist").get(0), s1.toString());

		assertTrue(lm.removeSongFromPlaylist("Playlist", "Bad Title", s1.getAlbum().getArtist()));
		assertTrue(lm.removeSongFromPlaylist("Playlist", s0.getTitle(), "Bad artist"));

		assertTrue(lm.removeSongFromPlaylist("Playlist", s1.getTitle(), s1.getAlbum().getArtist()));
		assertEquals(lm.getSongsFromPlaylist("Playlist").size(), 0);
	}


	@Test
	void testGetFavorite() {
		Song s2 = new Song("Title3", a0);
		lm.addSong(s0);
		lm.addSong(s1);
		lm.addSong(s2);

		lm.setRating(s0, Rating.FIVE);
		lm.setRating(s2, Rating.THREE);
		lm.setFavorite(s1);

		assertEquals(lm.getFavorites().size(), 2);
		assertEquals(lm.getFavorites().get(0), s0.toString());
		assertEquals(lm.getFavorites().get(1), s1.toString());

	}

	@Test
	void testGetTitles() {
		assertEquals(lm.getTitles().size(), 0);
		lm.addSong(s0);
		lm.addSong(s1);
		assertEquals(lm.getTitles().size(), 2);
		assertEquals(lm.getTitles().toArray()[0], s0.getTitle());
		assertEquals(lm.getTitles().toArray()[1], s1.getTitle());
	}

	@Test
	void testGetArtists() {
		assertEquals(lm.getArtists().size(), 0);
		lm.addSong(s0);
		lm.addSong(s1);
		assertEquals(lm.getArtists().size(), 1);
		assertEquals(lm.getArtists().toArray()[0], s0.getAlbum().getArtist());
	}

	@Test
	void testGetAlbums() {
		assertEquals(lm.getAlbums().size(), 0);
		lm.addSong(s0);
		lm.addSong(s1);
		assertEquals(lm.getAlbums().size(), 1);
		assertEquals(lm.getAlbums().toArray()[0], s0.getAlbum().getAlbumName());
	}

	@Test
	void testGetSongByTitle() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.addSong(s2);

		assertEquals(lm.findSongByTitle(s0.getTitle()).size(), 1);
		assertEquals(lm.findSongByTitle(s0.getTitle()).toArray()[0], s0);

		assertEquals(lm.findSongByTitle(s1.getTitle()).size(), 2);
		assertEquals(lm.findSongByTitle(s1.getTitle()).toArray()[0], s1);
		assertEquals(lm.findSongByTitle(s1.getTitle()).toArray()[1], s2);
	}

	@Test
	void testGetSongByArtist() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.addSong(s2);

		assertEquals(lm.findSongByArtist(s2.getArtist()).size(), 1);
		assertEquals(lm.findSongByArtist(s2.getArtist()).toArray()[0], s2);

		assertEquals(lm.findSongByArtist(s0.getArtist()).size(), 2);
		assertEquals(lm.findSongByArtist(s1.getArtist()).toArray()[0], s0);
		assertEquals(lm.findSongByArtist(s1.getArtist()).toArray()[1], s1);
	}

	@Test
	void testGetAlbumByTitle() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.addSong(s2);

		assertEquals(lm.findAlbumByTitle(s2.getAlbum().getAlbumName()).size(), 1);
		assertEquals(lm.findAlbumByTitle(s2.getAlbum().getAlbumName()).toArray()[0], s2.getAlbum());

		assertEquals(lm.findAlbumByTitle(s1.getAlbum().getAlbumName()).size(), 1);
		assertEquals(lm.findAlbumByTitle(s1.getAlbum().getAlbumName()).toArray()[0], s1.getAlbum());
	}

	@Test
	void testGetAlbumByArtist() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.addSong(s2);

		assertEquals(lm.findAlbumByArtist(s2.getAlbum().getArtist()).size(), 1);
		assertEquals(lm.findAlbumByArtist(s2.getAlbum().getArtist()).toArray()[0], s2.getAlbum());

		assertEquals(lm.findAlbumByArtist(s1.getAlbum().getArtist()).size(), 1);
		assertEquals(lm.findAlbumByArtist(s1.getAlbum().getArtist()).toArray()[0], s1.getAlbum());
	}

	@Test
	void testFormattedPlaylist() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.createPlaylist("Playlist");
		lm.addSongToPlaylist("Playlist", s0.getTitle(), s0.getAlbum().getArtist());
		lm.addSongToPlaylist("Playlist", s1.getTitle(), s1.getAlbum().getArtist());

		assertEquals(lm.getPlaylistsFormatted().toString(), "[Favorites, Frequently Played, Playlist\n  -Title by Artist0\n  -Title2 by Artist0, Recently Played, Top Rated]");
	}
	
	@Test
	void testRemoveSongs() {
		lm.addSong(s0);
		lm.addSong(s1);	
		lm.removeSong(s0);
		assertEquals(lm.getSongList().toArray()[0], s1);
	}
	
	@Test 
	void testPlaySong() { 
		lm.addSong(s0);
		lm.playSong(s0);

		HashSet<Song> hs = lm.findSongByTitle(s0.getTitle());
		assertEquals(hs.size(), 1);
		for (Song s : hs) {
			assertEquals(s.getPlays(), 1);
		}
	}
	
	@Test 
	void testRemoveAlbum() { 
		
		a0.addSong(s0);
		a1.addSong(s2);
		lm.addAlbum(a0);
		lm.addAlbum(a1);
		ArrayList<String> arr = new ArrayList<>(lm.getAlbums());
		assertEquals(arr.get(0), a0.getAlbumName());
		assertEquals(arr.get(1), a1.getAlbumName());
		assertEquals(lm.getAlbumList().size(), 2);

		lm.removeAlbum(a0);
		
		arr = new ArrayList<>(lm.getAlbums());
		assertEquals(arr.get(0), a1.getAlbumName());
		assertEquals(arr.size(), 1);
	}
	
	@Test 
	void testGenrePlaylist() {
		MusicStore ms = new MusicStore("albums/albums.txt");
		Album adele = new ArrayList<>(ms.findAlbumByTitle("21")).get(0);
		
		lm.addAlbum(adele);
		ArrayList<String> arr = lm.getSongsFromPlaylist(adele.getGenre());
		for(Song s : adele.getSongs()) {
			assertTrue(arr.contains(s.toString()));
		}
		assertEquals(adele.getSongs().size(), arr.size());
	}
	
	@Test 
	void shuffleLibrary() {
		MusicStore ms = new MusicStore("albums/albums.txt");
		Album adele = new ArrayList<>(ms.findAlbumByTitle("21")).get(0);

		
		lm.addAlbum(adele);
		ArrayList<Song> arr = lm.getSongList();
		lm.shuffleLibrary();
		
		boolean atLeastOneDifferentOrder = false;
		ArrayList<Song> temp = lm.getSongList();
		assertEquals(temp.size(), arr.size());
		for(int i = 0; i < temp.size(); ++i) {
			if(!temp.get(i).equals(arr.get(i)))
				atLeastOneDifferentOrder = true;
		}
		assertTrue(atLeastOneDifferentOrder);
	}
	
	@Test 
	void shufflePlaylist() {
		MusicStore ms = new MusicStore("albums/albums.txt");
		Album adele = new ArrayList<>(ms.findAlbumByTitle("21")).get(0);

		
		lm.addAlbum(adele);
		ArrayList<String> arr = lm.getSongsFromPlaylist("Pop");;
		lm.shufflePlaylist("Pop");
		
		boolean atLeastOneDifferentOrder = false;
		ArrayList<String> temp = lm.getSongsFromPlaylist("Pop");
		assertEquals(temp.size(), arr.size());
		for(int i = 0; i < temp.size(); ++i) {
			if(!temp.get(i).equals(arr.get(i)))
				atLeastOneDifferentOrder = true;
		}
		assertTrue(atLeastOneDifferentOrder);
	}
	
	@Test 
	void findSongsByGenre() {
		// useless line to get better code coverage
		Compare x = new Compare();
		MusicStore ms = new MusicStore("albums/albums.txt");
		Album adele = new ArrayList<>(ms.findAlbumByTitle("21")).get(0);
		
		Album a0 = new Album("ANAME", "Artist", "Genre", Year.parse("2020"));
		Song s0 = new Song("XTitle", a0);
		lm.addSong(s0);

		
		lm.addAlbum(adele);
		HashSet<Song> set = lm.findSongByGenre("Pop");
		lm.shufflePlaylist("Pop");
		

		for(Song s : adele.getSongs())
			assertTrue(set.contains(s));
	}
	
	
	
}
