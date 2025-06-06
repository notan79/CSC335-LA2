package view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

import model.Album;
import model.Compare;
import model.Rating;
import model.Song;
import model.library.LibraryModel;
import model.library.MusicStore;
import model.library.StoreFront;
import model.library.User;


public class UI {
	// Some comment
	private static Scanner scanner;
	private static MusicStore ms;
	private static User curUser;
	private static ArrayList<User> users = new ArrayList<>();
	private static boolean canLibrary = false; 

	/*
	 * 	Sets the user input scanner, creates music store and user library,
	 * 	calls the main prompt to start.
	 */
	public static void run() {

		scanner = new Scanner(System.in);

		ms = new MusicStore("albums/albums.txt");
		users = User.loadAllData("data/user_data", ms);
		mainPrompt();
	}

	private static void mainPrompt() {
		canLibrary = true;
		System.out.print("Choose an action (1-3):\n" + "1. Login.\n" + "2. Create Account.\n"
				+ "3. Exit.\n\n" + "Enter action: ");

		String inpString = runScannerOptions(3);

		if (inpString.equals("1")) {
			login();
		} else if (inpString.equals("2")) {
			createUser();
		} else {
			System.out.println("\nEnding program.");
			scanner.close();
			System.exit(0);
		}
		mainPrompt();
	}

	private static void login() {
		System.out.print("Enter username: ");
		String userName = scanner.nextLine().strip();
		System.out.print("Enter password: ");
		String pw = scanner.nextLine().strip();
		if(User.validateLogin(userName, pw)) {
			System.out.printf("%s is now logged in!\n\n", userName);
			for(User u : users) {
				// Find the user (no repeat usernames)
				if(u.getUsername().equals(userName)) {
					curUser = u;
					break;
				}
			}
			userPrompt();
		}else {
			System.out.println("Failed to login. Incorrect username or password.\n");
		}
	}

	private static void createUser() {
		System.out.print("Enter a username: ");
		String userName = scanner.nextLine().strip();
		if(User.usernameExist(userName) == null) {
			System.out.print("Enter a password: ");
			String pw = scanner.nextLine().strip();
			
			User temp = new User(userName, pw);
			users.add(temp);
			System.out.println("Created new user.\n");
			
			// Save the login information when a new user is made
			temp.saveLogin();
			curUser = temp;
			userPrompt();
		}else {
			System.out.println("Username already exists.\n");
		}
	}

	/*
	 * 	"User Prompt," allows to move between store and library as well as
	 * 	to logout.
	 */
	private static void userPrompt() {
		System.out.printf("%s is logged in.\n", curUser.getUsername());
		System.out.print("Choose an action (1-3):\n" + "1. Search Music Store.\n" + "2. Access User Library.\n"
				+ "3. Logout.\n\n" + "Enter action: ");

		String inpString = runScannerOptions(3);

		if (inpString.equals("1")) {
			searchPrompt(ms);
		} else if (inpString.equals("2")) {
			libraryPrompt();
		} else {
			System.out.println("\nLogging out.\n");
			saveUserData();
			canLibrary = false;
			return;
		}
	}
	
	private static void saveUserData() {
		// Kill the user data file, will rewrite all the data
		try {
			FileWriter fileWriter = new FileWriter(new File("data/user_data"), false);
			fileWriter.close();
		} catch (IOException e) {
			// This can never happen, the file is already created
			System.exit(1);
		}
		
		// Save all the user data
		for(User u : users) {
			u.saveData();
		}
	}

	/*
	 * 	Allows for any StoreFront object (music store and user) to
	 * 	search for artists, songs, albums from its songList.
	 */
	private static void searchPrompt(StoreFront s) {

		// Same search actions in both user library and music store.
		String tempString = "user library";
		if (s.getClass() == MusicStore.class) {
			tempString = "music store";
		}

		System.out.printf("\nYou are searching in %s. Choose an action (1-6):\n" + "1. Search for song by title.\n"
				+ "2. Search for song by artist.\n" + "3. Search for album by title.\n"
				+ "4. Search for album by artist.\n" + "5. Search for song by genre.\n" + "6. Exit search.\n\n"
				+ "Enter action: ", tempString);
		String inpString = runScannerOptions(6);

		// Find Song by Title
		if (inpString.equals("1")) {
			System.out.print("Enter song title: ");
			inpString = scanner.nextLine().strip();
			HashSet<Song> tempHashSet = s.findSongByTitle(inpString);
			if (tempHashSet.size() > 0) {
				System.out.printf("\nFound these songs with title: %s\n", inpString);
			} else {
				System.out.printf("\nFound no songs with title: %s\n", inpString);
			}

			for (Song song : tempHashSet) {
				System.out.println("- " + song);
				
				String str = "";
				if(s.getClass() != MusicStore.class) {
					System.out.print("Get album information (Enter Y for yes): ");
					str = scanner.nextLine().strip();
				}

				if(str.equals("Y")) {
					albumInfo(s, song.getAlbum());
				}
			}
		}
		// Find song by artist
		else if (inpString.equals("2")) {
			System.out.print("Enter artist: ");
			inpString = scanner.nextLine().strip();
			HashSet<Song> tempHashSet = s.findSongByArtist(inpString);
			if (tempHashSet.size() > 0) {
				System.out.printf("\nFound these songs with artist: %s\n", inpString);
			} else {
				System.out.printf("\nFound no songs with artist: %s\n", inpString);
			}

			for (Song song : tempHashSet) {
				System.out.println("- " + song);
			}
		}
		// Find songs by album
		else if (inpString.equals("3")) {
			System.out.print("Enter album title: ");
			inpString = scanner.nextLine().strip();
			HashSet<Album> tempHashSet = s.findAlbumByTitle(inpString);
			if (tempHashSet.size() > 0) {
				System.out.printf("\nFound these albums with title: %s\n", inpString);
			} else {
				System.out.printf("\nFound no albums with title: %s\n", inpString);
			}
			if(s.getClass() == MusicStore.class)
				for (Album album : tempHashSet) {
					System.out.println("- " + album);
				}
			else
				for (Album album : tempHashSet) {
					albumInfo(s, album);
				}
		}
		// Find Albums by artist
		else if (inpString.equals("4")) {
			System.out.print("Enter artist: ");
			inpString = scanner.nextLine().strip();
			HashSet<Album> tempHashSet = s.findAlbumByArtist(inpString);
			if (tempHashSet.size() > 0) {
				System.out.printf("\nFound these albums with artist: %s\n", inpString);
			} else {
				System.out.printf("\nFound no albums with artist: %s\n", inpString);
			}

			if(s.getClass() == MusicStore.class)
				for (Album album : tempHashSet) {
					System.out.println("- " + album);
				}
			else
				for (Album album : tempHashSet) {
					albumInfo(s, album);
				}
		}
		else if (inpString.equals("5")) {
			System.out.print("Enter genre: ");
			inpString = scanner.nextLine().strip();
			HashSet<Song> tempHashSet = s.findSongByGenre(inpString);
			if (tempHashSet.size() > 0) {
				System.out.printf("\nFound these songs with the genre: %s\n", inpString);
			} else {
				System.out.printf("\nFound no songs with genre: %s\n", inpString);
			}

			for (Song s0 : tempHashSet) {
				System.out.println("- " + s0);
			}
		}

		// Exit the music store
		else {
			System.out.printf("\nLeaving %s.\n\n", tempString);
			if(s.getClass() == MusicStore.class)
				userPrompt();
			else {
				libraryPrompt();
			}
			return;
		}

		// Stay in the search mode
		searchPrompt(s);
	}
	
	private static void albumInfo(StoreFront s, Album a) {
		// Happens when is a Library Object
		LibraryModel m = (LibraryModel) s;
		ArrayList<Song> allSongs = m.getSongList();
		
		boolean albumInLib = m.getAlbumList().contains(a.getAlbumName());
		HashSet<Album> temp = ms.findAlbumByTitle(a.getAlbumName()); 
		
		// Needs to be populated with the songs
		for(Album t : temp) {
			if(t.equals(a))
				a = t;
		}
		
//		System.out.println(a.getSongs());
		System.out.printf("%s by %s. Genre: %s. Year: %s.\n", a.getAlbumName(), a.getArtist(), a.getGenre(), a.getYear());
		if(albumInLib) {
			for(Song t : a.getSongs()) 
				System.out.printf("  - %s (%s)\n", t, (allSongs.contains(t) ? "Added" : "Not Added"));
		}
		else 
			System.out.println("No songs from this album are in the library.");
	}

	/*
	 * 	Functionality for the user library.
	 */
	private static void libraryPrompt() {
		System.out.printf("\nYou are in user library. Choose an action (1-8):\n" + "1. Search for songs or albums.\n"
				+ "2. Add/remove song or album, or shuffle songs in library.\n" + "3. Rate a song.\n" + "4. Favorite a song.\n"
				+ "5. Get information for library.\n" + "6. Create playlist.\n"
				+ "7. Add or remove song from playlist or shuffle playlist.\n" + "8. Play song.\n" + "9. Exit User Library.\n\n" + "Enter action: ");

		String inpString = runScannerOptions(9);
		if (inpString.equals("1")) {
			searchPrompt(curUser);
		} else if (inpString.equals("2")) {
			addRemoveShuffleLibrary();
		} else if (inpString.equals("3")) {
			rateSongInLibrary();
		} else if (inpString.equals("4")) {
			favoriteSong();
		} else if (inpString.equals("5")) {
			getLibraryInformation();
		} else if (inpString.equals("6")) {
			createPlaylist();
		} else if (inpString.equals("7")) {
			addRemoveFromPlaylist();
		} else if(inpString.equals("8")) {
			playSong();
		}
		// If input is 9, exit library
		else {
			System.out.printf("\nLeaving User Library.\n\n");
			userPrompt();
			return;
		}
		// Stay in library
		if(canLibrary)
			libraryPrompt();
	}
	
	// Plays a song
	private static void playSong() {
		System.out.print("\nEnter song title to play: ");
		String inpString = scanner.nextLine().strip();
		HashSet<Song> arr = curUser.findSongByTitle(inpString);
		if (arr.size() == 0) {
			System.out.printf("No songs in library with title: %s\n", inpString);
		} else {
			for (Song song : arr) {
				System.out.printf("Play %s? (Enter Y for yes): ", song);
				inpString = scanner.nextLine().strip();
				if (inpString.equals("Y")) {
					curUser.playSong(song);
					System.out.printf("Played %s %d times.\n", song, song.getPlays()+1);
				}
			}
		}
	}

	/*
	 * 	Gets the information from the library: albums, songs, playlists, favorites...
	 */
	private static void getLibraryInformation() {
		String inpString;
		String sorted;
		System.out.printf("\nYou are in User library. Choose an action (1-6):\n" + "1. Show song titles.\n"
				+ "2. Show artists.\n" + "3. Show albums.\n" + "4. Show playlists.\n" + "5. Search for playlist.\n" + "6. Show favorited songs.\n\n"
				+ "Enter action: ");
		inpString = runScannerOptions(6);
		
		// Get songs
		if (inpString.equals("1")) {
			ArrayList<Song> temp = curUser.getSongList();
			System.out.printf("\nChoose an action (1-4):\n" + "1. Sort by title.\n"
					+ "2. Sort by artist.\n" + "3. Sort by rating.\n" 
					+ "4. Not sorted.\n\nEnter Action: ");
			
			sorted = runScannerOptions(4);
			
			if(sorted.equals("1"))
				Collections.sort(temp, new Compare.CompareSongByTitle());
			else if(sorted.equals("2"))
				Collections.sort(temp, new Compare.CompareSongByArtist());
			else if(sorted.equals("3"))
				Collections.sort(temp, new Compare.CompareSongByRating());
			
			for (Song song : temp) {
				System.out.println("- " + song);
			}
			
		}
		// Get artists
		else if (inpString.equals("2")) {
			HashSet<String> hashSet = curUser.getArtists();
			for (String s : hashSet) {
				System.out.println("- " + s);
			}
		}
		// Get albums
		else if (inpString.equals("3")) {
			HashSet<String> hashSet = curUser.getAlbums();
			for (String s : hashSet) {
				System.out.println("- " + s);
			}
		}
		// Get playlists
		else if (inpString.equals("4")) {
			ArrayList<String> arrayList = curUser.getPlaylistsFormatted();
			for (String s : arrayList) {
				System.out.println("- " + s);
			}
		}
		// Get playlists
		else if (inpString.equals("5")) {
			System.out.print("\nEnter playlist name: ");
			String pname = scanner.nextLine().strip();
			ArrayList<String> arrayList = curUser.getPlaylistsFormatted();
			
			boolean flag = false;
			for (String s : arrayList) {
				if(s.split("\n")[0].strip().equals(pname)) {
					flag = true;
					System.out.println(s);
				}
			}
			if(!flag) {
				System.out.printf("No playlist with name: %s.\n", pname);
			}
		}
		// Get favorites
		else {
			ArrayList<String> arrayList = curUser.getFavorites();
			for (String s : arrayList) {
				System.out.println("- " + s);
			}
		}
	}
	
	private static void addRemoveShuffleLibrary() {
		String inpString;
		System.out.printf("\nChoose an action (1-3):\n"
				+ "1. Add song or album.\n" + "2. Remove song or album.\n" + "3. Shuffle library.\n" + "Enter action: ");
		inpString = runScannerOptions(3);
		if(inpString.equals("1"))
			addToLibrary();
		else if(inpString.equals("2")) {
			removeFromLibrary();
		}else {
			curUser.shuffleLibrary(); 
			System.out.println("Shuffled library.");
		}
	}
	
	private static void removeFromLibrary() {
		String inpString;
		System.out.printf("\nChoose an action (1-2):\n"
				+ "1. Remove song.\n" + "2. Remove album.\n" + "Enter action: ");
		inpString = runScannerOptions(2);
		
		if(inpString.equals("1")) {
			System.out.print("\nEnter song title to remove: ");
			inpString = scanner.nextLine().strip();
			HashSet<Song> arr = ms.findSongByTitle(inpString);
			if (arr.size() == 0) {
				System.out.printf("No songs in library with title: %s\n", inpString);
			} else {
				for (Song song : arr) {
					System.out.printf("Remove %s? (Enter Y for yes): ", song);
					inpString = scanner.nextLine().strip();
					if (inpString.equals("Y")) {
						curUser.removeSong(song);
					}
				}
			}
		}
		else {
			System.out.print("\nEnter album title to remove: ");
			inpString = scanner.nextLine().strip();
			HashSet<Album> arr = ms.findAlbumByTitle(inpString);
			if (arr.size() == 0) {
				System.out.printf("No songs in library with title: %s\n", inpString);
			} else {
				for (Album a : arr) {
					System.out.printf("Remove %s? (Enter Y for yes): ", a);
					inpString = scanner.nextLine().strip();
					if (inpString.equals("Y")) {
						curUser.removeAlbum(a);
					}
				}
			}
		}
	}

	/*
	 * 	Prompt to add a given song or album to library
	 */
	private static void addToLibrary() {
		String inpString;
		System.out.printf("\nYou are adding from music store to user library. Choose an action (1-3):\n"
				+ "1. Add song.\n" + "2. Add album.\n" + "3. Exit.\n\n" + "Enter action: ");
		inpString = runScannerOptions(3);

		// Add song by title
		if (inpString.equals("1")) {
			System.out.print("\nEnter song title to add: ");
			inpString = scanner.nextLine().strip();
			HashSet<Song> arr = ms.findSongByTitle(inpString);
			if (arr.size() == 0) {
				System.out.printf("No songs in music store with title: %s\n", inpString);
			} else {
				for (Song song : arr) {
					System.out.printf("Add %s? (Enter Y for yes): ", song);
					inpString = scanner.nextLine().strip();
					if (inpString.equals("Y")) {
						curUser.addSong(song);
					}
				}
			}
		}
		// Add all songs from album
		else if (inpString.equals("2")) {
			System.out.print("\nEnter album title to add: ");
			inpString = scanner.nextLine().strip();
			HashSet<Album> arr = ms.findAlbumByTitle(inpString);
			if (arr.size() == 0) {
				System.out.printf("No albums in music store with title: %s", inpString);
			} else {
				for (Album album : arr) {
					System.out.printf("Add %s by %s (%s)? (Enter Y for yes): ", album.getAlbumName(), album.getArtist(),
							album.getYear());
					inpString = scanner.nextLine().strip();
					if (inpString.equals("Y")) {
						for (Song song : album.getSongs()) {
							curUser.addSong(song);
						}
					}
				}
			}
		}
	}

	/*
	 * 	Rate a given song
	 */
	private static void rateSongInLibrary() {
		String inpString;

		System.out.println("Valid ratings are 1-5. Anything else will be interpreted as no rating.");
		System.out.print("\nEnter song title to rate: ");
		inpString = scanner.nextLine().strip();
		HashSet<Song> arr = curUser.findSongByTitle(inpString);

		// No songs in the library with given name
		if (arr.size() == 0) {
			System.out.printf("No songs in library with title: %s", inpString);
		}
		// Prompt which song to rate (could be repeat names)
		else {
			for (Song song : arr) {
				System.out.printf("Rate %s: ", song);
				inpString = scanner.nextLine().strip();
				if (inpString.equals("1")) {
					curUser.setRating(song, Rating.ONE);
				} else if (inpString.equals("2")) {
					curUser.setRating(song, Rating.TWO);
				} else if (inpString.equals("3")) {
					curUser.setRating(song, Rating.THREE);
				} else if (inpString.equals("4")) {
					curUser.setRating(song, Rating.FOUR);
				} else if (inpString.equals("5")) {
					curUser.setRating(song, Rating.FIVE);
				} else {
					curUser.setRating(song, Rating.NONE);
				}
			}
		}
	}

	/*
	 * 	Favorite a song in the library
	 */
	private static void favoriteSong() {
		String inpString;
		System.out.print("\nEnter song title to favorite: ");
		inpString = scanner.nextLine().strip();
		HashSet<Song> arr = curUser.findSongByTitle(inpString);

		// No song with given name
		if (arr.size() == 0) {
			System.out.printf("No songs in library with title: %s\n", inpString);
		}
		// Prompt which song to favorite (could be repeat names)
		else {
			for (Song song : arr) {
				System.out.printf("Favorite %s? (Enter Y for yes): ", song);
				inpString = scanner.nextLine().strip();
				if (inpString.equals("Y")) {
					curUser.setFavorite(song);
				}
			}
		}
	}

	/*
	 * 	Creates a new playlist with inputted name
	 */
	private static void createPlaylist() {
		String inpString;
		System.out.print("Enter a name for your playlist: ");
		inpString = scanner.nextLine().strip();
		ArrayList<String> arr = curUser.getPlaylists();

		// No repeat playlists
		if (arr.contains(inpString)) {
			System.out.printf("Already a playlist with name: %s\n", inpString);
		} else {
			curUser.createPlaylist(inpString);
			System.out.printf("Created new playlist: %s\n", inpString);
		}
	}

	/*
	 * 	Prompt to add or remove a song from a given playlist
	 */
	private static void addRemoveFromPlaylist() {
		String inpString;

		System.out.print("Choose playlist: ");
		String playlist = scanner.nextLine().strip();

		// Make sure a playlist exists
		boolean flag = false;
		ArrayList<String> playlists = curUser.getPlaylists();
		for(String p : playlists) {
			if(playlist.equals(p)) {
				flag = true;
			}
		}
		if(!flag) {
			System.out.printf("No playlist with name: %s.\n", playlist);
			return;
		}


		// Find to add or remove song
		System.out.printf("\n" + "1. Add song.\n" + "2. Remove song.\n3. Shuffle playlist.\n\n" + "Enter action: ");
		inpString = scanner.nextLine().strip();

		// Choose a song to add
		if (inpString.equals("1")) {
			System.out.print("Choose song title to add: ");
			inpString = scanner.nextLine().strip();
			System.out.print("Choose artist for song: ");
			if(!curUser.addSongToPlaylist(playlist, inpString, scanner.nextLine().strip())) {
				System.out.printf("Unable to add %s to playlist.\n", inpString);
			} else {
				System.out.println("Song has been added.");
			}
		}
		// Choose a song to remove
		else if (inpString.equals("2")) {
			System.out.print("Choose song to remove: ");
			inpString = scanner.nextLine().strip();
			System.out.print("Choose artist for song remove: ");
			curUser.removeSongFromPlaylist(playlist, inpString, scanner.nextLine().strip());
		} else if (inpString.equals("3")) {
			if(curUser.shufflePlaylist(playlist))
				System.out.printf("%s shuffled.\n", playlist);
			else
				System.out.printf("%s is read only.\n", playlist);
		}else {
			System.out.println("Invalid input.");
		}
	}

	/*
	 * 	Helper method to guarantee a prompt (a number prompt for the different
	 * 	options) is valid.
	 *
	 * 	int options: the amount of options to choose from
	 */
	private static boolean promptValidity(String s, int options) {
		try {
			String.valueOf(s);
		} catch (NumberFormatException e) {
			return false;
		}

		for (int i = 1; i < options + 1; ++i) {
			if (s.equals(String.valueOf(i))) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 	Helper method to get inputs until a valid input is given (using promptValidity)
	 *
	 * 	int options: the amount of options to choose from
	 */
	private static String runScannerOptions(int options) {
		String inpString = "";
		while (scanner.hasNextLine()) {
			inpString = scanner.nextLine().strip();
			if (promptValidity(inpString, options)) {
				break;
			} else {
				System.out.printf("Invalid action: %s. Enter valid action: ", inpString);
			}
		}
		return inpString;
	}
}
