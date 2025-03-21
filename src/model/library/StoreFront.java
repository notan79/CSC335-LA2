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
 * 			- songList: protected HashSet of Songs for the Songs in the instance
 *
 * 	Methods:
 * 			- public void addSong(Song)
 * 			- public HashSet<Song> getSongList()
 * 			- public HashSet<String> getTitles()
 * 			- public HashSet<String> getArtists()
 * 			- public HashSet<String> getAlbums()
 * 			- public HashSet<Song> findSongByTitle(String)
 * 			- public HashSet<Song> findSongByArtist(String)
 * 			- public HashSet<Album> findAlbumByTitle(String)
 * 			- public HashSet<Album> findAlbumByArtist(String)
 * 			- private HashSet<Song> findSongsComparator(Song, Comparator<Song>)
 * 			- private HashSet<Album> findAlbumsComparator(Album, Comparator<Album>)
 *
 */
public class StoreFront {

	protected final ArrayList<Song> songList = new ArrayList<>();

	// Add a song to songList
	public void addSong(Song song) {
		if(!songList.contains(song)) {
			songList.add(new Song(song));
		}
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
	public HashSet<String> getTitles() {
		HashSet<String> temp = new HashSet<>();
		for (Song song : this.songList) {
			temp.add(song.getTitle());
		}
		return temp;
	}

	// Returns a list of the artists of songList
	public HashSet<String> getArtists() {
		HashSet<String> temp = new HashSet<>();
		for (Song song : this.songList) {
			temp.add(song.getArtist());
		}
		return new HashSet<>(temp);
	}

	// Returns a list of the albums of songList
	public HashSet<String> getAlbums() {
		HashSet<String> temp = new HashSet<>();
		for (Song song : this.songList) {
			temp.add(song.getAlbum().getAlbumName());
		}
		return new HashSet<>(temp);
	}

	/*
	 * 	These return a list of Songs using the Compare utility class
	 *
	 * 	Utilizes a temporary static "constructor" to get a new song with the given title or artist
	 */
	public HashSet<Song> findSongByTitle(String title) {
		return this.findSongsComparator(Song.fromTitle(title), new Compare.CompareSongByTitle());
	}

	public HashSet<Song> findSongByArtist(String artist) {
		return this.findSongsComparator(Song.fromAlbum(Album.fromArtist(artist)), new Compare.CompareSongByArtist());
	}

	public HashSet<Song> findSongByGenre(String genre) {
		HashSet<Song> temp = new HashSet<>();
		for(Song song : this.songList) {
			if(song.getGenre().equals(genre)) {
				temp.add(song);
			}
		}
		return temp;
	}

	/*
	 * 	These return a list of Albums using the Compare utility class
	 *
	 * 	Utilizes a temporary static "constructor" to get a new album with the given title or artist
	 */
	public HashSet<Album> findAlbumByTitle(String title) {
		return this.findAlbumsComparator(Album.fromAlbumName(title), new Compare.CompareAlbumByTitle());
	}

	public HashSet<Album> findAlbumByArtist(String artist) {
		return this.findAlbumsComparator(Album.fromArtist(artist), new Compare.CompareAlbumByArtist());
	}

	// Helper methods that return an HashSet of Songs or albums given the input and the comparator
	private HashSet<Song> findSongsComparator(Song inp, Comparator<Song> c) {
		HashSet<Song> temp = new HashSet<>();
		for (Song song : songList) {
			if (c.compare(song, inp) == 0) {
				temp.add(new Song(song));
			}
		}
		return temp;
	}

	private HashSet<Album> findAlbumsComparator(Album inp, Comparator<Album> c) {
		HashSet<Album> temp = new HashSet<>();
		for (Song song : songList) {
			if (c.compare(song.getAlbum(), inp) == 0 && !temp.contains(song.getAlbum())) {
				temp.add(new Album(song.getAlbum()));
			}
		}
		return temp;
	}
}
