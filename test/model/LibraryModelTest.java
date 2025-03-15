package model;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Year;

import org.junit.jupiter.api.Test;

import model.library.LibraryModel;

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
		assertEquals(lm.getSongList().get(0), s0);
		assertEquals(lm.getSongList().get(1), s1);
	}
	

	@Test
	void testCreatePlaylist() {
		assertTrue(lm.createPlaylist("New Playlist"));
		assertTrue(lm.createPlaylist("New Playlist 2"));
		assertEquals(lm.getPlaylists().get(0), "New Playlist");
		assertEquals(lm.getPlaylists().get(1), "New Playlist 2");
		
		assertFalse(lm.createPlaylist("New Playlist"));
		assertEquals(lm.getPlaylists().size(), 2);
		assertEquals(lm.getPlaylists().get(0), "New Playlist");
		assertEquals(lm.getPlaylists().get(1), "New Playlist 2");
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
		assertEquals(lm.getTitles().get(0), s0.getTitle());
		assertEquals(lm.getTitles().get(1), s1.getTitle());
	}
	
	@Test 
	void testGetArtists() {
		assertEquals(lm.getArtists().size(), 0);
		lm.addSong(s0);
		lm.addSong(s1);
		assertEquals(lm.getArtists().size(), 1);
		assertEquals(lm.getArtists().get(0), s0.getAlbum().getArtist());
	}
	
	@Test 
	void testGetAlbums() {
		assertEquals(lm.getAlbums().size(), 0);
		lm.addSong(s0);
		lm.addSong(s1);
		assertEquals(lm.getAlbums().size(), 1);
		assertEquals(lm.getAlbums().get(0), s0.getAlbum().getAlbumName());
	}
	
	@Test 
	void testGetSongByTitle() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.addSong(s2);
		
		assertEquals(lm.findSongByTitle(s0.getTitle()).size(), 1);
		assertEquals(lm.findSongByTitle(s0.getTitle()).get(0), s0);
		
		assertEquals(lm.findSongByTitle(s1.getTitle()).size(), 2);
		assertEquals(lm.findSongByTitle(s1.getTitle()).get(0), s1);
		assertEquals(lm.findSongByTitle(s1.getTitle()).get(1), s2);
	}
	
	@Test 
	void testGetSongByArtist() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.addSong(s2);
		
		assertEquals(lm.findSongByArtist(s2.getArtist()).size(), 1);
		assertEquals(lm.findSongByArtist(s2.getArtist()).get(0), s2);
		
		assertEquals(lm.findSongByArtist(s0.getArtist()).size(), 2);
		assertEquals(lm.findSongByArtist(s1.getArtist()).get(0), s0);
		assertEquals(lm.findSongByArtist(s1.getArtist()).get(1), s1);
	}
	
	@Test 
	void testGetAlbumByTitle() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.addSong(s2);
		
		assertEquals(lm.findAlbumByTitle(s2.getAlbum().getAlbumName()).size(), 1);
		assertEquals(lm.findAlbumByTitle(s2.getAlbum().getAlbumName()).get(0), s2.getAlbum());
		
		assertEquals(lm.findAlbumByTitle(s1.getAlbum().getAlbumName()).size(), 1);
		assertEquals(lm.findAlbumByTitle(s1.getAlbum().getAlbumName()).get(0), s1.getAlbum());
	}
	
	@Test 
	void testGetAlbumByArtist() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.addSong(s2);
		
		assertEquals(lm.findAlbumByArtist(s2.getAlbum().getArtist()).size(), 1);
		assertEquals(lm.findAlbumByArtist(s2.getAlbum().getArtist()).get(0), s2.getAlbum());
		
		assertEquals(lm.findAlbumByArtist(s1.getAlbum().getArtist()).size(), 1);
		assertEquals(lm.findAlbumByArtist(s1.getAlbum().getArtist()).get(0), s1.getAlbum());
	}
	
	@Test 
	void testFormattedPlaylist() {
		lm.addSong(s0);
		lm.addSong(s1);
		lm.createPlaylist("Playlist");
		lm.addSongToPlaylist("Playlist", s0.getTitle(), s0.getAlbum().getArtist());
		lm.addSongToPlaylist("Playlist", s1.getTitle(), s1.getAlbum().getArtist());
		assertEquals(lm.getPlaylistsFormatted().toString(), "[Playlist\n  -Title by Artist0\n  -Title2 by Artist0]");	
	}

}
