package model.library;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import model.Album;
import model.Playlist;
import model.Rating;
import model.Song;

import java.lang.StringBuilder;



public class User extends LibraryModel {
	private final String username; 
	private final String password;
	private final static int saltLength = 64;
	
	public static void main(String[] args) {
		try {
			FileWriter fileWriter = new FileWriter(new File("data/user_data"), false);			
			fileWriter.close();
			fileWriter = new FileWriter(new File("data/login"), false);			
			fileWriter.close();
		} catch (IOException e) {
			// This can never happen, the file is already created
			System.exit(1);
		} 
		
		MusicStore ms = new MusicStore("albums/albums.txt");
		User u = new User("user", "pass");
		Album a = ms.findAlbumByTitle("21").get(0);
		u.addAlbum(a);
		u.setFavorite(a.getSongs().get(0));
		u.setRating(a.getSongs().get(1), Rating.FOUR);
		
		u.playSong(a.getSongs().get(0));

		u.saveData();
		User u0 = u;
		
		u = new User("user2", "pass2");
		a = ms.findAlbumByTitle("19").get(0);
		u.addAlbum(a);
		u.setFavorite(a.getSongs().get(0));
		u.setFavorite(a.getSongs().get(1));

		u.saveData();
		
		User userTemp = loadAllData("data/user_data", ms).get(0);
		System.out.println(u0);
		System.out.println(userTemp);
		
		System.out.println(u0.equals(userTemp));
	}
	
	public User(String username, String password) {
		this.username = username; 
		this.password = salt(encrypt(password));
		writeFile();
	}
	
	private void writeFile() {
		try {
			FileWriter fileWriter = new FileWriter(new File("data/login"), true);			
			fileWriter.write(username + " " + password + "\n");
			fileWriter.close();
		} catch (IOException e) {
			// This can never happen, the file is already created
			System.exit(1);
		} 
	}
	
	// 0 to 93, add the int value of 'a' 64 times
	private String salt(String password) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(password);
		for (int i = 0; i < User.saltLength; i++) {
			char randChar = (char)('!' + random.nextInt(93));
			sb.append(String.valueOf(randChar));
		}		
		return sb.toString();
	}
	
	private String encrypt(String password) {
		String temp = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			byte[] mDigest = md.digest(password.getBytes());
			
			BigInteger bi = new BigInteger(1, mDigest);
			
			temp = bi.toString(16);
			
		} catch (NoSuchAlgorithmException e) {
			// This can never happen
			System.exit(1);
		}
		return temp;
	}
	
	
	public boolean validateLogin(String username, String password) {
		// checks if the user has the same user and pass
		String temp = usernameExist(username);
		temp = temp.substring(0, temp.length() - User.saltLength);
		return temp != null && temp.equals(encrypt(password));
	}
	
	private String usernameExist(String username) {
		// returns the encrypted and salted pass associated with user
		File file = new File("data/login");
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNext()) {		
				String[] lines = scan.nextLine().split(" ");
				// assumes that there is none of the same username
				if (lines[0].equals(username)) {
					return lines[1];
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// this can never happen
			System.exit(1);
		} 
		return null;
		
	}
	
	
	// Getters 
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() { 
		return this.password;
	}
	
	
	// saving files
	public void saveData() {
		try {
			FileWriter fileWriter = new FileWriter(new File("data/user_data"), true);			
			fileWriter.write(this.getData());
			fileWriter.close();
		} catch (IOException e) {
			// This can never happen, the file is already created
			System.exit(1);
		} 
		
	}
	
	private String getData() {
		StringBuilder sb = new StringBuilder("Start:"+ this.username);
		sb.append("\nLibrary:");
		sb.append(this.getRatings());
		sb.append("\nPlaylists:");
		sb.append(this.getPlaylistInfo());
		return sb.toString();
	}
	
	private String getRatings() {
		StringBuilder sb = new StringBuilder("[");
		for(Song s : this.songList) {
			sb.append(s);
			sb.append(" ");
			sb.append(s.getRating());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1); // remove additional ","
		sb.append("]");
		return sb.toString();
	}
	
	private String getPlaylistInfo() {
		StringBuilder sb = new StringBuilder();
		ArrayList<String> arr = this.getPlaylists();
		for(String p : arr) {
			sb.append("\n");
			ArrayList<String> songs = this.getSongsFromPlaylist(p);
			sb.append(p);
			sb.append(":");
			sb.append(songs);
		}
		sb.append("\n");
		return sb.toString();
	}
	
	// Loads in all the user data from the specified file
	public static ArrayList<User>loadAllData(String file, MusicStore ms) {
		
		// Read in the file
		Scanner scan = null;
		try {
			scan = new Scanner(new File(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// never happens
		if(scan == null)
			return null;
		
		ArrayList<User> users = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		while(scan.hasNextLine()) {
			sb.append(scan.nextLine());
		}
		
		// Split the starting of the different users
		String[] userDataArr = sb.toString().strip().split("Start:");
		
		// For each of the users...
		for(String user : userDataArr) {
			// Ignore '\n'
			if(user.length() > 0) {
				
				// Extract the user info
				int userNameIndex = user.indexOf("Library:");
				String username = user.substring(0,userNameIndex);
				
				User tempUser = new User(username, "");
				
				int playlistsIndex = user.indexOf("Playlists:");
				userNameIndex = user.indexOf(":");
				
				// Add all the songs to the library
				String[] songsInLibrary = user.substring(userNameIndex+2, playlistsIndex-1).split(",");
				for(String s: songsInLibrary) {
					String[] temp = s.split("by");
					String songName = temp[0].strip();
					
					temp = temp[1].strip().split(" ");
					String artistName = temp[0].strip();
					String strRating = temp[1].strip();
					Rating rating;
					switch(strRating) {
						case "ONE": 
							rating = Rating.ONE;
							break;
						case "TWO": 
							rating = Rating.TWO;
							break;
						case "THREE": 
							rating = Rating.THREE;
							break;
						case "FOUR": 
							rating = Rating.FOUR;
							break;
						case "FIVE": 
							rating = Rating.FIVE;
							break;
						default:
							rating = Rating.NONE;
							break;
					}
					
					ArrayList<Song> possSongs = ms.findSongByTitle(songName);
					for(Song song : possSongs) {
						if(song.getArtist().equals(artistName)) {
							tempUser.addSong(song);
							tempUser.setRating(song, rating);
						}
					}
				}
				
				
				// Add all the playlists to the library
				String[] playlists = user.substring(playlistsIndex+10).split("]");
				for(String s : playlists) {
					int next = s.indexOf(':');
					String playlistName = s.substring(0, next);
					tempUser.createPlaylist(playlistName);
					
					// Genre playlists should already be made
					boolean alreadyMade = tempUser.getSongsFromPlaylist(playlistName).size() > 0;
					
					s = s.substring(next+2).strip();
					
					// If should add to the playlist
					if(!alreadyMade && s.length() > 0) {
						String[] songs = s.split(",");
						for(String songStr : songs) {
							String[] temp = songStr.split("by");
							String songName = temp[0].strip();
							
							String artistName = temp[1].strip();
							ArrayList<Song> possSongs = tempUser.findSongByTitle(songName);
							for(Song song : possSongs) {
								if(song.getArtist().equals(artistName))
									if(playlistName.equals("Favorites")) 
										tempUser.setFavorite(song);
									else
										tempUser.forceAddSongToPlaylist(playlistName, song.getTitle(), song.getArtist());
							}
						}
					}
				}
				users.add(tempUser);
			}
		}
		return users;
	}
	
	// Adds a song to a specified playlist when reading user data from file
	private boolean forceAddSongToPlaylist(String playlistName, String title, String artist) {
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
	
	@Override
	public String toString() {
		return this.getData().strip();
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	public boolean equals(Object o) {
		if(o == null) return false;
		
		if(o.getClass() != this.getClass()) return false;
		User temp = (User) o;
		
		return temp.hashCode() == this.hashCode();
	}
	
	
}