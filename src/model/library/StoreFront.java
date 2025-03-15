package model.library;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import model.Album;
import model.Compare;
import model.Song;



/*
 * 	Author:		Nathan Crutchfield
 * 
 * 	Purpose: 	Parent class for MusicStore and LibraryModel
 * 
 * 	Instance Variables: 
 * 			- songList: protected ArrayList of Songs for the Songs in the instance
 * 
 * 	Methods: 
 * 			- public void addSong(Song)
 * 			- public ArrayList<Song> getSongList()
 * 			- public ArrayList<String> getTitles()
 * 			- public ArrayList<String> getArtists()
 * 			- public ArrayList<String> getAlbums()
 * 			- public ArrayList<Song> findSongByTitle(String)
 * 			- public ArrayList<Song> findSongByArtist(String)
 * 			- public ArrayList<Album> findAlbumByTitle(String)
 * 			- public ArrayList<Album> findAlbumByArtist(String)
 * 			- private ArrayList<Song> findSongsComparator(Song, Comparator<Song>)
 * 			- private ArrayList<Album> findAlbumsComparator(Album, Comparator<Album>)
 * 	
 */
public class StoreFront {

	protected final ArrayList<Song> songList = new ArrayList<>();

	// Add a song to songList
	public void addSong(Song song) {
		if(!songList.contains(song))
			songList.add(new Song(song));
	}

	// Returns a deep copy of songList
	public ArrayList<Song> getSongList() {
		ArrayList<Song> temp = new ArrayList<>();
		for (Song song : this.songList) {
			temp.add(new Song(song));
		}
		return temp;
	}

	// Returns a list of the song titles of songList
	public ArrayList<String> getTitles() {
		ArrayList<String> temp = new ArrayList<>();
		for (Song song : this.songList) {
			temp.add(song.getTitle());
		}
		return temp;
	}

	// Returns a list of the artists of songList
	public ArrayList<String> getArtists() {
		HashSet<String> temp = new HashSet<>();
		for (Song song : this.songList) {
			temp.add(song.getArtist());
		}
		return new ArrayList<>(temp);
	}

	// Returns a list of the albums of songList
	public ArrayList<String> getAlbums() {
		HashSet<String> temp = new HashSet<>();
		for (Song song : this.songList) {
			temp.add(song.getAlbum().getAlbumName());
		}
		return new ArrayList<>(temp);
	}
	
	/*
	 * 	These return a list of Songs using the Compare utility class
	 * 
	 * 	Utilizes a temporary static "constructor" to get a new song with the given title or artist
	 */
	public ArrayList<Song> findSongByTitle(String title) {
		return this.findSongsComparator(Song.fromTitle(title), new Compare.CompareSongByTitle());
	}

	public ArrayList<Song> findSongByArtist(String artist) {
		return this.findSongsComparator(Song.fromAlbum(Album.fromArtist(artist)), new Compare.CompareSongByArtist());
	}

	/*
	 * 	These return a list of Albums using the Compare utility class
	 * 
	 * 	Utilizes a temporary static "constructor" to get a new album with the given title or artist
	 */
	public ArrayList<Album> findAlbumByTitle(String title) {
		return this.findAlbumsComparator(Album.fromAlbumName(title), new Compare.CompareAlbumByTitle());
	}

	public ArrayList<Album> findAlbumByArtist(String artist) {
		return this.findAlbumsComparator(Album.fromArtist(artist), new Compare.CompareAlbumByArtist());
	}

	// Helper methods that return an ArrayList of Songs or albums given the input and the comparator
	private ArrayList<Song> findSongsComparator(Song inp, Comparator<Song> c) {
		ArrayList<Song> temp = new ArrayList<>();
		for (Song song : songList) {
			if (c.compare(song, inp) == 0)
				temp.add(new Song(song));
		}
		return temp;
	}

	private ArrayList<Album> findAlbumsComparator(Album inp, Comparator<Album> c) {
		ArrayList<Album> temp = new ArrayList<>();
		for (Song song : songList) {
			if (c.compare(song.getAlbum(), inp) == 0 && !temp.contains(song.getAlbum()))
				temp.add(new Album(song.getAlbum()));
		}
		return temp;
	}
}
