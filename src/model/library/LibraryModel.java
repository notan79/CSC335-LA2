package model.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import model.Album;
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
	private HashSet<Album> allAlbums = new HashSet<>();
	
	public LibraryModel() {
		super();
		allPlaylists.add(new Playlist("Favorites", false));
		allPlaylists.add(new Playlist("Top Rated", false));
		allPlaylists.add(new Playlist("Recently Played", false));
		allPlaylists.add(new Playlist("Frequently Played", false));
	}
	
	public boolean playSong(Song s) {
		boolean flag = false;
		for(Song temp : this.songList) {
			if(temp.equals(s)) {
				temp.play();
				flag = true;
				this.updateRecentPlays(s);
				this.updateFrequentPlays(s);
			}
		}
		return flag;
	}
	
	private void updateRecentPlays(Song s) {
		Playlist p = null;
		for(Playlist t : this.allPlaylists) {
			if(t.getName().equals("Recently Played"))
				p = t;
		}
		
		// Never happens
		if(p == null)
			return;
		
		ArrayList<Song> temp = p.getPlaylist();
		// Remove the first song if the size limit is reached
		if(temp.size() >= 10)
			p.removeSong(temp.get(0));

		// Always add the most recent song at the end
		p.addSong(s);
	}
	
	
	// Doesn't work, multiple songs of same title, and need access to the song... update hashValue in Song
	private void updateFrequentPlays(Song s) {
		Playlist p = null;
		for(Playlist t : this.allPlaylists) {
			if(t.getName().equals("Frequently Played"))
				p = t;
		}
		
		// Never can happen
		if(p == null)
			return;
		
		p.removeAll();
		
		HashMap<Integer, Song> counter = new HashMap<>();
		for(Song t : this.songList) {
      counter.put(t.getPlays(), t);
    }
		
		ArrayList<Integer> temp = new ArrayList<>(counter.keySet());
		Collections.sort(temp, Collections.reverseOrder());
		int i = 0;
		while(i < temp.size() && i < 10) {
			// Add the songs associated with the 10 most plays
			p.addSong(counter.get(temp.get(i)));
			++i;
		}
	}
	
	// Methods to alter the songs in the library.
	@Override
	public void addSong(Song song) {
		super.addSong(song);
		this.allAlbums.add(song.getAlbum());
		this.genrePlaylist(song.getGenre());
	}
	
	public void addAlbum(Album album) {
		if(!this.allAlbums.contains(album))
			this.allAlbums.add(album);
		
		for(Song s : album.getSongs()) {
			if(!this.songList.contains(s))
				this.songList.add(s);
		}
	}
	
	public void removeSong(Song song) {
		for(Song s : this.songList) {
			if(s.equals(song))
				this.songList.remove(s);
		}
		this.genrePlaylist(song.getGenre());
	}
	
	public void removeAlbum(Album album) {
		for(Song s : this.songList) {
			if(s.getAlbum().equals(album))
				this.songList.remove(s);
		}
		this.genrePlaylist(album.getGenre());
		this.allAlbums.remove(album);
	}
	
	// Creates, removes, or adds song to a genre playlist
	private void genrePlaylist(String g) {
		int count = countGenre(g);
		Playlist genre = null;
		for(Playlist p : this.allPlaylists) {
			if(p.getName().equals(g)) {
				genre = p;
				break;
			}
		}
		
		// Remove the playlist
		if(genre != null)
			this.allPlaylists.remove(genre);
		
		// Need to update the playlist
		if(count >= 10) {
			// Currently no playlist (or user made playlist) of this genre, create and add
			if(genre == null || genre.isRemovable()) {
				genre = new Playlist(g, false);		
				this.allPlaylists.add(genre);
			}
			
			// Add all, non included songs of genre to the playlist
			for(Song temp : this.songList) {
				if(temp.getGenre().equals(g) && !genre.getPlaylist().contains(temp)) {
					genre.addSong(temp);
				}
			}
		}
	}
	
	// Helper method, returns the amount of songs in the inputted genre
	private int countGenre(String g){
		int count = 0;
		for(Song temp : this.songList) {
			if(temp.getGenre().equals(g)) {
				++count;
			}
		}
		return count;
	}
	
	public void shuffle() {
		Collections.shuffle(this.songList);
	}

	// Setters
	public void setRating(Song song, Rating rate) {
		for(Song s : this.songList) {
			if(song.equals(s)){
				if(rate == Rating.FIVE)
					s.setFavorite();
				
				// Add rating of 4 or 5 to top rated playlist
				if(rate == Rating.FIVE || rate == Rating.FOUR) {
					for(Playlist p : this.allPlaylists) {
						if(p.getName().equals("Top Rated")) 
							p.addSong(s);
					}
				}
				s.setRating(rate);
			}
		}
	}
	
	public void setFavorite(Song song) {
		for(Song s : this.songList) {
			if(s.equals(song)) {
				s.setFavorite();
				
				// Add favorited songs to favorites playlist
				for(Playlist p : this.allPlaylists) {
					if(p.getName().equals("Favorites")) 
						p.addSong(s);
				}
			}
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
		if (tempPlaylist == null || !tempPlaylist.isRemovable()) {
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
		if (tempPlaylist == null || !tempPlaylist.isRemovable()) {
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
	
	public ArrayList<String> getAlbumList(){
		ArrayList<String> s = new ArrayList<>();
		for(Album a : this.allAlbums) {
			s.add(a.getAlbumName());
		}
		return s; 
	}
}
