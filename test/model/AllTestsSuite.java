package model;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ AlbumTest.class, SongTest.class, MusicStoreTest.class, 
				PlaylistTest.class, LibraryModelTest.class})
public class AllTestsSuite {
	
}