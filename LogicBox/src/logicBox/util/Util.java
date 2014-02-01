


package logicBox.util;



/**
 * Generic utility functions.
 * @author Lee Coakley
 */
public class Util
{
	/**
	 * Get a random integer in the given half-open range.
	 * @param low
	 * @param highex (exclusive)
	 * @return int
	 */
	public static int randomIntRange( int low, int highex ) {
		return low + ((int) Math.floor(Math.random() * (highex-low)));
	}
	
	
	
	
	
	/**
	 * Capitalise a word.
	 */
	public static String capitalise( String word ) {
		return word.substring( 0, 1 ).toUpperCase() + word.substring( 1 );
	}
}
