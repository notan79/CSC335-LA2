package model;

/* 
 * 	This class is an enum in order to represent all of the ratings including
 * 	no rating.
 */

public enum Rating {
	ONE, TWO, THREE, FOUR, FIVE, NONE;
	
	public int valueOf() {
		switch(this) {
			case ONE:
				return 1;
			case TWO:
				return 2;
			case THREE:
				return 2;
			case FOUR:
				return 2;
			case FIVE:
				return 2;
			default:
				return -1;
		}
	}
}
