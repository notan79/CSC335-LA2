package model;

import java.util.Comparator;

/*
 * 	Utility class to make comparing Songs and Albums by title and artist less redundant
 */
public class Compare {

	// Compare the Songs by their artist
	public static class CompareSongByArtist implements Comparator<Song> {

		@Override
		public int compare(Song s0, Song s1) {
			return s0.getArtist().compareTo(s1.getArtist());
		}
	}

	// Compare the Songs by their title
	public static class CompareSongByTitle implements Comparator<Song> {

		@Override
		public int compare(Song s0, Song s1) {
			return s0.getTitle().compareTo(s1.getTitle());
		}
	}

	// Compare the Albums by their artist
	public static class CompareAlbumByArtist implements Comparator<Album> {

		@Override
		public int compare(Album a0, Album a1) {
			return a0.getArtist().compareTo(a1.getArtist());
		}
	}

	// Compare the Albums by their title
	public static class CompareAlbumByTitle implements Comparator<Album> {

		@Override
		public int compare(Album a0, Album a1) {
			return a0.getAlbumName().compareTo(a1.getAlbumName());
		}
	}

}
