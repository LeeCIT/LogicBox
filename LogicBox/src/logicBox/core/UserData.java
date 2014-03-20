


package logicBox.core;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



/**
 * Handles saving and loading user settings, saved files etc.
 * @author Lee Coakley
 */
public abstract class UserData
{
	private static Map<String,Serializable> config = new HashMap<>();
	
	
	
	private static String getDir() {
		return System.getProperty( "user.home" ) + File.separator + ".LogicBox" + File.separator;
	}
	
	
	
	private static String getFile() {
		return getDir() + "config.cfg";
	}
	
	
	
	
	
	public static void main( String[] args ) {
		System.out.println( getFile() );
	}
}
