package model.library;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Album;
import model.Song;

import java.time.Year;

/*
 * 	Author:		Cameron Liu
 * 
 * 	Purpose: 	Sub class of StoreFront to represent the store to pull
 * 				songs and albums from
 * 
 * 	Instance Variables: 
 * 			- songList: protected ArrayList inherited from StoreFront
 * 
 * 	Methods: 
 * 			- private void parseFiles(String)
 * 			- private ArrayList<String> parseMainFile(String)
 * 			- private ArrayList<Album> parseAlbums(ArrayList<String>)
 * 			- public String getFileNameFormat(String, String)
 * 	
 */
public class MusicStore extends StoreFront {

	public MusicStore(String fname) {
		super();
		this.parseFiles(fname);
	}

	// Method to catch FileNotFoundException
	private void parseFiles(String fname) {
		try {
			this.parseAlbums(this.parseMainFile(fname));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			return;
		}
	}

	// Extract all the album titles from the albums.txt file.
	private ArrayList<String> parseMainFile(String fname) throws FileNotFoundException {
		ArrayList<String> temp = new ArrayList<>();

		Scanner scanner = new Scanner(new File(fname));

		while (scanner.hasNextLine()) {
			String tempString = scanner.nextLine();
			String[] line = tempString.split(",");
			String title = line[0];
			String artist = line[1];
			temp.add(getFileNameFormat(title, artist));
		}
		scanner.close();
		return temp;
	}

	// For each album, add all the songs from its respective file.
	private ArrayList<Album> parseAlbums(ArrayList<String> titles) throws FileNotFoundException {
		ArrayList<Album> temp = new ArrayList<Album>();
		ArrayList<Song> songs = new ArrayList<>();

		for (int i = 0; i < titles.size(); i++) {
			
			// Read in the current album file
			Scanner scanner = new Scanner(new File(titles.get(i)));
			boolean flag = true;

			// Read all songs from the file
			while (scanner.hasNext()) {
				String tempString = scanner.nextLine();
				
				// First line
				if (flag) {
					String[] line = tempString.split(",");
					String albumName = line[0];
					String artist = line[1];
					String genre = line[2];
					String year = line[3];
					temp.add(new Album(albumName, artist, genre, Year.parse(year)));
					flag = false;
				} 
				// Rest of lines
				else {
					Song s = new Song(tempString, temp.get(temp.size() - 1));
					songs.add(s);
					// is this gigachad way? ↓↓↓
					temp.get(temp.size() - 1).addSong(s); // this gets the Album that was created in the if statement
				}
			}

			// Each song needs the FINISHED album before being added to instance variables
			for (Song song : songs) {
				int index = temp.indexOf(song.getAlbum());
				super.addSong(new Song(song.getTitle(), temp.get(index)));
			}
			scanner.close();
		}
		return temp;
	}

	// Gets the formatted string for the album files
	private String getFileNameFormat(String title, String artist) {
		// moved method from album,
		return "albums/" + title + "_" + artist + ".txt";
	}
}
