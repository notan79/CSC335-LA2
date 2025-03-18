package model;

import java.util.ArrayList;
import java.util.Collections;

/* Author(s): Cameron Liu and Nathan Crutchfield
 * 
 * Purpose: The purpose of this class is to represent a playlist. It does this by having a name, and a 
 * playlist of songs that are created in order to show the playlist itself. 
 * 
 * Instance Variables: 
 * 	name: the name of the playlist
 * 	playlistSongs: the ArrayList of songs that represents a playlist.
 * 
 * Methods:
 * 	public void addSong(Song): adds a song to the ArrayList of songs (playlistSongs)
 * 	public void removeSong(Song): removes a song from the ArrayList of songs (playlistSongs)
 * 	public String getName(): returns the name of the ArrayList of songs (playlistSongs)
 * 	public ArrayList<Song> getPlaylist(): returns the ArrayList of songs (playlistSongs)  
 * 
 */


public class Playlist {

	private final String name;
	private boolean removable;
	private ArrayList<Song> playlistSongs = new ArrayList<>();

	/*
	 * 	Basic playlist constructor
	 * 	@pre: name != null
	 */
	public Playlist(String name) {
		this.name = name;
		this.removable = true;
	}
	public Playlist(String name, boolean isRemovable) {
		this.name = name;
		this.removable = isRemovable;
	}

	public void addSong(Song song) {
		// adds a song to the playlist
		Song tempSong = new Song(song);
		playlistSongs.add(tempSong);
	}
	
	public boolean isRemovable() {
		return this.removable;
	}
	
	public void setRemovableFalse() {
		this.removable = false;
	}

	public void removeSong(Song song) {
		// removes a song from the playlist
		for (int i = 0; i < playlistSongs.size(); i++) {
			if (playlistSongs.get(i).equals(song)) {
				// if song in the constructor has the same title and artist, remove it
				playlistSongs.remove(i);
			}
		}
	}
	
	public void removeAll() {
		this.playlistSongs = new ArrayList<>();
	}

	// getters
	public String getName() {
		// gets the name of the playlist
		return this.name;
	}
	
	public void shuffle() {
		Collections.shuffle(this.playlistSongs);
	}

	public ArrayList<Song> getPlaylist() {
		// gets the playlist
		ArrayList<Song> tempPlaylist = new ArrayList<Song>();
		for (int i = 0; i < playlistSongs.size(); i++) {
			// iterates through original playlist, creating copy of each song and adding it
			// to a copy of new arraylist.
			Song tempSong = new Song(playlistSongs.get(i));
			tempPlaylist.add(tempSong);
		}
		return tempPlaylist;
	}
	
	// Override Method
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.name);
		for(Song song : this.playlistSongs) {
			stringBuilder.append("\n  -");
			stringBuilder.append(song);
			// stringBuilder.append("\n");
		}
		
		return stringBuilder.toString();
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}
