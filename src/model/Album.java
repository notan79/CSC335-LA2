package model;

import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;

/* Authors(s): Cameron Liu and Nathan Crutchfield
 * 
 * Purpose: Represents an actual album with the name for the album, artist, genre, and year
 * It also has an ArrayList of song objects in order to store all the songs in the album. Each of 
 * the instance variables are final, as they are not able to be edited once created. We do this because albums 
 * are always finalized, and do not need to be edited one created. 
 * 
 * Instance Variables: 
 * 	albumName: name of the album 
 * 	artist: artist of the album 
 * 	genre: genre of album 
 * 	year: year the album was created
 * 	songs: ArrayList<Song>, arraylist of songs that stores all songs from the album
 *
 * 
 * Methods: 
 * 	public static Album fromAlbumName(String): returns the albums from the name
 * 	public static Album fromArtist(String): returns the albums from the artist
 * 	public void addSong(Song): adds the song from the parameter to the list of songs
 * 	public String getAlbumName(): returns the album name
 * 	public String getArtist(): returns the artist of the album 
 * 	public String getGenre(): returns the genre of the album
 * 	public Year getYear(): returns the year of the album
 * 	public ArrayList<Song> getSongs(): returns the list of songs from the album
 * 
 */

public class Album {

	private final String albumName;
	private final String artist;
	private final String genre;
	private final Year year;
	private final ArrayList<Song> songs = new ArrayList<>();

	/*
	 * 	Constructor for when you have everything for the album 
	 * 
	 * 	@pre: albumName != null && artist != null && genre != null && year != null
	 */
	public Album(String albumName, String artist, String genre, Year year) {
		this.albumName = albumName;
		this.artist = artist;
		this.genre = genre;
		this.year = year;
	}

	/*
	 * 	Copy constructor
	 * 	
	 * 	@pre: album != null
	 */
	public Album(Album album) {
		this.albumName = album.albumName;
		this.artist = album.artist;
		this.genre = album.genre;
		this.year = album.year;

		for (Song s : album.songs) {
			this.songs.add(new Song(s));
		}
	}
	
	// These methods create a temporary album either from title or from artist
	public static Album fromAlbumName(String title) {
		return new Album(title, "", "", Year.parse("0"));
	}
	
	public static Album fromArtist(String artistName) {
		return new Album("", artistName, "", Year.parse("0"));
	}

	public void addSong(Song song) {
		// adds song to album 
		this.songs.add(new Song(song));
	}

	// Getters
	public String getAlbumName() {
		return this.albumName;
	}

	public String getArtist() {
		return this.artist;
	}

	public String getGenre() {
		return this.genre;
	}

	public Year getYear() {
		return this.year;
	}

	public ArrayList<Song> getSongs() {
		ArrayList<Song> temp = new ArrayList<>();
		for (Song s : this.songs) {
			temp.add(new Song(s));
		}
		return temp;
	}

	// Override methods
	@Override
	public boolean equals(Object o) {
		// overrides default equals methods and checks classes instead. 
		// after that it returns if the temp album and the current album share the same information.
		if (o == null)
			return false;
		if (o.getClass() != this.getClass())
			return false;

		Album temp = (Album) o;
		return this.artist.equals(temp.artist) && this.albumName.equals(temp.albumName) && this.year.equals(temp.year);
	}

	@Override
	public String toString() {
		// overrides toString method and just creates a string with the album name, artist, genre, and year with formatting. 
		// it also prints all of the songs
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.albumName);
		sBuilder.append(" by ");
		sBuilder.append(this.artist);
		sBuilder.append(". Genre: ");
		sBuilder.append(this.genre);
		sBuilder.append(". Year: ");
		sBuilder.append(this.year.toString());
		sBuilder.append(".\n");
		for (Song song : this.songs) {
			sBuilder.append("  - ");
			sBuilder.append(song.getTitle());
			sBuilder.append("\n");
		}

		return sBuilder.toString();
	}
}
