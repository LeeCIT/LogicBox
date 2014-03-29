


package logicBox.util;



/**
 * Provides complex string functions intended to make it easier to make nice debug output. 
 * @author Lee Coakley
 */
public abstract class StringUtil
{
	/**
	 * Replace part of a string by overwriting it.
	 * If pos is beyond the end then spaces are inserted.
	 * @param str Source string
	 * @param substr Inserted string
	 * @param pos Position to begin overwriting.
	 * @return Modified string
	 */
	public static String overwritePos( String str, String substr, int pos ) {
		StringBuilder outstr = new StringBuilder( str );
		int           newlen = pos + substr.length();
		
	    while (newlen > outstr.length())
	    	outstr.append( " " );
	    
	    for (int i=pos; i<newlen; i++)
	        outstr.setCharAt( i, substr.charAt(i-pos) );
	    
	    return outstr.toString();
	}
	
	
	
	/**
	 * Insert substring, shifting forward existing characters at that position.
	 */
	public static String insertBefore( String str, String substr, int pos ) {    
	    return new StringBuilder(str).insert(pos,substr).toString();
	}
	
	
	
	/**
	 * Repeat a string.
	 */
	public static String repeat( String str, int count ) {
		if (count <= 0)
			return "";
		
		StringBuilder sb = new StringBuilder( str.length() * count );
		
		for (int i=0; i<count; i++)
			sb.append( str );
		
		return sb.toString();
	}
	
	
	
	/**
	 * Vertically align multi-line text according to the position of a reference symbol.
	 * See main() below for a demonstration.
	 */
	public static String align( String multiline, String alignBy, int from ) {
		String[]  lines   = multiline.split( "\n" );
		int[]     poses   = new int[ lines.length ];
		final int NOMATCH = -1;
		
		for (;;) {
			int furthest = NOMATCH;
			
			for (int i=0; i<lines.length; i++) {
				int pos = lines[i].indexOf( alignBy, from );
				poses[i] = pos;
				furthest = Math.max( furthest, pos );
			}
			
			if (furthest == NOMATCH)
				break;
			
			for (int i=0; i<lines.length; i++) {
				if (poses[i] != NOMATCH) {
					int    reps = furthest - poses[i];
					String repl = repeat( " ", reps );
					lines[i] = insertBefore( lines[i], repl, poses[i] );
				}
			}
			
			from = furthest + alignBy.length();
		}
		
		return combineLines( lines );
	}
	
	
	
	/**
	 * Align multi-line text according to the position of a reference symbol.
	 */
	public static String align( String multiline, String alignBy ) {
		return align( multiline, alignBy, 0 );
	}
	
	
	
	/**
	 * Combine an array of strings into multiline text.
	 */
	public static String combineLines( String[] strings ) {
		StringBuilder sb = new StringBuilder();
		
		for (String str: strings) {
			sb.append( str  );
			sb.append( "\n" );
		}
		
		return sb.toString();
	}
	
	
	
	/**
	 * Get the longest line out of an array of lines.
	 * Doesn't account for newlines; lines must be pre-split.
	 */
	public static String findLongest( String[] lines ) {
		if (lines.length == 0)
			return null;
		
		String longest = lines[0];
		int    bestLen = longest.length();
		
		for (String str: lines) {
			if (str.length() > bestLen) {
				longest = str;
				bestLen = str.length();
			}
		}
		
		return longest;
	}
	
	
	
	/**
	 * Capitalise a word.
	 */
	public static String capitalise( String word ) {
		return word.substring( 0, 1 ).toUpperCase() + word.substring( 1 );
	}
	
	
	
	
	
	public static void main( String[] args ) {
		String str =
			"a = totesBadical = b;\n" +
			"whoa = mega = c;\n" +
			"cowabunga = um = ...;";
		
		System.out.println( "Input:\n" + str + "\n\n" );
		System.out.println( "Output:\n" + align(str,"=",0) );
	}
}


















