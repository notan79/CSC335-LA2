package model;

/*
 * 	This class is an enum in order to represent all of the ratings including
 * 	no rating.
 */

public enum Rating {
	ONE, TWO, THREE, FOUR, FIVE, NONE;

	// Get a numeric representation of the rating for the comparator
	public int valueOf() {
		switch(this) {
			case ONE:
				return 1;
			case TWO:
				return 2;
			case THREE:
				return 3;
			case FOUR:
				return 4;
			case FIVE:
				return 5;
			default:
				return -1;
		}
	}
}
