package model;

/* Author(s): Cameron Liu and Nathan Crutchfield
 *
 * Purpose: The purpose of this class is to represent a Song by creating instance variables
 * title, rating, album, and isFavorite.
 *
 * Instance Variables:
 * 	title: title of the Song
 * 	rating: rating of the Song
 * 	album: album that the Song is from
 * 	isFavorite: if the Song is a favorite
 * 	plays: how many times the song has been played
 *
 * Methods:
 *
 * public static Song fromTitle(String): returns a song from the title
 * public static Song fromAlbum(Album): returns a song from the album
 * public String getTitle(): gets the title
 * public String getArtist(): gets the artist
 * public String getGenre(): gets the genre
 * public Rating getRating(): gets the rating
 * public Album getAlbum(): gets the album
 * public boolean isFavorite(): checks if the song is favorite
 * public int getPlays(): gets the amount of plays
 * public void setFavorite(): sets the current song to favorite
 * public void setRating(Rating): sets the rating to the rating from the constructor, which is an ENUM.
 * public void play(): play the song
 *
 */

public final class Song {

	private final String title;
	private Rating rating;
	private Album album;
	private boolean isFavorite = false;
	private int plays = 0;

	/*
	 * 	Basic Song constructor
	 * 	@pre: title != null && Album != null
	 */
	public Song(String title, Album album) {
		this.title = title;
		this.rating = Rating.NONE;
		this.album = new Album(album);
		
	}

	/*
	 * 	Copy constructor
	 * 	@pre: song != null
	 */
	public Song(Song song) {
		this.title = song.title;
		this.rating = song.rating;
		this.isFavorite = song.isFavorite;
		this.album = new Album(song.album);     
		this.plays = song.getPlays(); 
	}

	/*
	 * 	Temporary constructor
	 * 	@pre: title != null
	 */
	private Song(String title) {
		this.title = title;
	}

	// These methods create a temporary song from a given title or album.
	public static Song fromTitle(String title) {
		return new Song(title);
	}

	public static Song fromAlbum(Album album) {
		return new Song("", new Album(album));
	}

	// Getters
	public String getTitle() {
		// gets the title of the song
		return title;
	}

	public String getArtist() {
		// gets the artist of the song
		return this.album.getArtist();
	}

	public String getGenre() {
		// gets the artist of the song
		return this.album.getGenre();
	}

	public Rating getRating() {
		// get the current rating of the song
		return this.rating;
	}

	public Album getAlbum() {
		// gets the album of the song
		if (this.album == null) {
			return null;
		}
		return new Album(this.album);
	}

	public boolean isFavorite() {
		// checks if the song is a favorite, returns a boolean
		return this.isFavorite;
	}

	public int getPlays() {
		return this.plays;
	}

	// Setters
	public void setFavorite() {
		// sets the current song to favorite
		this.isFavorite = true;
	}

	public void setRating(Rating rate) {
		// set the rating to the rating in the constructor.
		if (rate == Rating.FIVE) {
			this.setFavorite();
		}
		this.rating = rate;
	}

	public void play() {
		++this.plays;
	}

	// Override methods
	@Override
	public boolean equals(Object o) {
		if (o == null || this.album == null || (o.getClass() != this.getClass())) {
			return false;
		}

		Song temp = (Song) o;
		if (temp.album == null) {
			return false;
		}

		return this.getArtist().equals(temp.getArtist()) && this.title.equals(temp.title);
	}

	@Override
	public String toString() {
		return this.title + " by " + this.getArtist();
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}
