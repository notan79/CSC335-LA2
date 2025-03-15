package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import model.library.MusicStore;

class MusicStoreTest {
	
	private MusicStore mStore = new MusicStore("albums/albums.txt");

	@Test
	void testConstructor() {
		assertEquals(mStore.getSongList().size(), 163);
		String[] arr = {"Adele", "Norah Jones", "Alabama Shakes", 
						"Mana", "Ozomatli", "Ben Harper", "Amos Lee",
						"Leonard Cohen", "Mumford & Sons", "OneRepublic",
						"Coldplay", "Dolly Parton", "Carol King", "The Heavy"};
		ArrayList<String> arrayList = mStore.getArtists(); 
		for(String string : arr) {
			arrayList.indexOf(string);
			assertNotEquals(arrayList.indexOf(string), -1);
		}
		assertEquals(arrayList.size(), arr.length);

		
		String[] arr2 = {"19", "21", "Begin Again", "Boys & Girls", "Cuando Los Angeles Lloran",
				"Don't Mess With the Dragon", "Fight for Your Mind", "Mission Bell", "Old Ideas",
				"Sigh No More", "Waking Up", "A Rush of Blood to the Head", "Coat of Many Colors",
				"Tapestry", "Sons"};
		
		arrayList = mStore.getAlbums();
		for(String string : arr2) {
			arrayList.indexOf(string);
			assertNotEquals(arrayList.indexOf(string), -1);
		}
		assertEquals(arrayList.size(), arr2.length);
		
		MusicStore ms = new MusicStore("albums/otherFile.txt");
		assertEquals(ms.getSongList().size(), 0);
	}
}
