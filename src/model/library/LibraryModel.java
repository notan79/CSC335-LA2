package model.library;

import java.util.ArrayList;
import java.util.Collections;

import model.Playlist;
import model.Rating;
import model.Song;

/*
 * 	Authors:	Nathan Crutchfield and Cameron Liu
 * 
 * 	Purpose: 	Sub class of StoreFront to represent the user library to
 * 				store songs, create playlists, rate, etc.
 * 
 * 	Instance Variables: 
 * 			- songList: 	protected ArrayList inherited from StoreFront
 * 			- allPlaylists: private ArrayList of Playlist objects
 * 
 * 	Methods: 
 * 			- public void setRating(Song, Rating)
 * 			- public void setFavorite(Song)
 * 			- public ArrayList<String> getPlaylists()
 * 			- public ArrayList<String> getPlaylistsFormatted()
 * 			- public ArrayList<String> getSongsFromPlaylist(String)
 * 			- public boolean createPlaylist(String)
 * 			- public boolean addSongToPlaylist(String, String, String)
 * 			- public boolean removeSongFromPlaylist(String, String, String)
 * 			- public ArrayList<String> getFavorites()
 * 	
 */
public class LibraryModel extends StoreFront {

	private ArrayList<Playlist> allPlaylists = new ArrayList<Playlist>();
	
	public LibraryModel() {
		super();
	}

	// Setters
	public void setRating(Song song, Rating rate) {
		for(Song s : this.songList) {
			if(song.equals(s)){
				if(rate == Rating.FIVE)
					s.setFavorite();
				s.setRating(rate);
			}
		}
	}
	
	public void setFavorite(Song song) {
		for(Song s : this.songList) {
			if(s.equals(song))
				s.setFavorite();
		}
	}

	// Getters
	public ArrayList<String> getPlaylists() {
		ArrayList<String> temp = new ArrayList<>();
		for(Playlist playlist : this.allPlaylists)
			temp.add(playlist.getName());
		
		Collections.sort(temp);
		return temp;
	}
	
	public ArrayList<String> getPlaylistsFormatted() {
		ArrayList<String> temp = new ArrayList<>();
		for(Playlist playlist : this.allPlaylists)
			temp.add(playlist.toString());
		
		Collections.sort(temp);
		return temp;
	}

	public ArrayList<String> getSongsFromPlaylist(String playlistName) {
		ArrayList<String> temp = new ArrayList<>();
		Playlist tempPlaylist = null;
		ArrayList<Song> songs;
		
		for (int i = 0; i < allPlaylists.size(); i++) {
			if (allPlaylists.get(i).getName().equals(playlistName)) {
				tempPlaylist = allPlaylists.get(i);
			}
		}
		
		if (tempPlaylist == null) {
			return new ArrayList<String>();
		}
	
		songs = tempPlaylist.getPlaylist();
		
		for (int i = 0; i < songs.size(); i++) {
			temp.add(songs.get(i).toString());
		}
	
		return temp;
	}

	// Creates a new Playlist
	public boolean createPlaylist(String playlistName) {
		for(Playlist playlist : this.allPlaylists) {
			if(playlist.getName().equals(playlistName))
				return false;
		}
		Playlist temp = new Playlist(playlistName);
		allPlaylists.add(temp);
		return true;
	}

	// Adds a song to a specified playlist
	public boolean addSongToPlaylist(String playlistName, String title, String artist) {
		ArrayList<Song> songs = this.songList;
		Playlist tempPlaylist = null;
		boolean flag = false;
		
		// gets the proper playlist
		for (int j = 0; j < allPlaylists.size(); j++) {
			if (allPlaylists.get(j).getName().equals(playlistName)) {
				tempPlaylist = allPlaylists.get(j);
			}
		}
		if (tempPlaylist == null) {
			return false;
		}
		
		// adds the proper playlist
		for (int i = 0; i < songs.size(); i++) {
			if (songs.get(i).getTitle().equals(title) && songs.get(i).getArtist().equals(artist)) {
				tempPlaylist.addSong(new Song(songs.get(i)));
				flag = true;
			}
		}	
		return flag;
	}

	// Removes a song from a specified playlist
	public boolean removeSongFromPlaylist(String playlistName, String title, String artist) {
		ArrayList<Song> songs = this.songList;
		Playlist tempPlaylist = null;
		
		// gets the proper playlist
		for (int j = 0; j < allPlaylists.size(); j++) {
			if (allPlaylists.get(j).getName().equals(playlistName)) {
				tempPlaylist = allPlaylists.get(j);
			}
		}
		if (tempPlaylist == null) {
			return false;
		}
		
		// removes the song from the playlist
		for (int i = 0; i < songs.size(); i++) {
			if (songs.get(i).getTitle().equals(title) && songs.get(i).getArtist().equals(artist)) {
				tempPlaylist.removeSong(new Song(songs.get(i)));
			}
		}	
		return true;
	}

	// Getter
	public ArrayList<String> getFavorites() {
		ArrayList<String> temp = new ArrayList<>();
		ArrayList<Song> arr = this.songList;
		for (Song song : arr) {
			if (song.isFavorite()) {
				temp.add(song.toString());
			}
		}

		// Alphabetical
		Collections.sort(temp);
		return temp;
	}
}
