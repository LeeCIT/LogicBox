


package logicBox.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.controllers.RemappableObjectInputStream;



/**
 * Provides IO utility functions.
 * @author Lee Coakley
 */
public abstract class Storage
{
	/**
	 * Read a file line-by-line into a list.
	 * @param filename The file to read.
	 * @return List with one line per element.  Empty if If the file is inaccessible or empty.
	 */
	public static List<String> read( String filename ) {
		final List<String> list = new ArrayList<String>();
		
		read( filename, new CallbackParam<String>() {
			public void execute( String line ) {
				list.add( line );
			}
		});
		
		return list;
	}
	
	
	
	/**
	 * Read a file line-by-line, passing each line to the callback as a string.
	 * @param filename
	 * @param callback
	 * @return boolean: Whether the file was successfully read.
	 */
	public static boolean read( String filename, CallbackParam<String> callback ) {		
		try {
			File           file       = new File( filename );
			FileReader     fileReader = new FileReader( file );
			BufferedReader buffReader = new BufferedReader( fileReader, 1<<16 );
			
			while (buffReader.ready())
				callback.execute( buffReader.readLine() );
			
			buffReader.close();
			return true;
		} 
		catch (IOException ex) {
			return false;
		}
	}
	
	
	
	/**
	 * Write a string to a file.
	 * @param filename File to write to.
	 * @param str String to write, can be multiple lines.
	 */
	public static boolean write( String filename, String str ) {
		try {
			File           file       = new File( filename );
			FileWriter     fileWriter = new FileWriter( file );
			BufferedWriter buffWriter = new BufferedWriter( fileWriter, 1<<16 );
			buffWriter.write( str );
			buffWriter.close();
			return true;
		}
		catch (IOException ex) {
			return false;
		}
	}
	
	
	
	/**
	 * Write a series of lines to a file.
	 * @param filename File to write to.
	 * @param str List of strings to write.  Each is appended with a newline.
	 */
	public static boolean write( String filename, List<String> strList ) {
		String strAcc = "";
		
		for (String str: strList)
			strAcc += str + "\n";
		
		return Storage.write( filename, strAcc );
	}
	
	
	
	/**
	 * Read a single object from a file.
	 * @param filename
	 * @param type
	 * @return Reference to object
	 */
	public static <T> T read( String filename, Class<T> type ) {
		T object = null;
		
		try {
			FileInputStream   fis = new FileInputStream( filename );
			ObjectInputStream ois = new ObjectInputStream( fis );
			
			object = (T) ois.readObject();
			ois.close();
		}
		catch (Exception ex) {
			throw new RuntimeException( ex );
		}
		
		return object;
	}
	
	
	
	/**
	 * Write an object to a file.
	 * @param filename
	 * @param object
	 */
	public static void write( String filename, Serializable object ) {
		try {
			FileOutputStream   fos = new FileOutputStream( filename );
			ObjectOutputStream oos = new ObjectOutputStream( fos );
			
			oos.writeObject( object );
			oos.close();
		}
		catch (Exception ex) {
			throw new RuntimeException( ex );
		}
	}	
	
	
	
	/**
	 * Check whether a file exists.
	 * @param filename
	 * @return
	 */
	public static boolean exists( String filename ) {		
		return new File(filename).exists();
	}
}

































