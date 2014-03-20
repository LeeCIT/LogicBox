
package prototypes.zipPrototype;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileMangerZip {
	private static String fileLocation = "demoZip.zip";
	private static String fileName     = "file Name Here";
	
	
	public static void saveStringCompressed(String toSave) {
		try {
			FileOutputStream fileOutput = new FileOutputStream( fileLocation );
			ZipOutputStream  zipOutput  = new ZipOutputStream ( fileOutput   );
			
			zipOutput.putNextEntry(new ZipEntry(fileName));
			zipOutput.write(toSave.getBytes(Charset.forName("UTF-8"))); // I read somewhere that the default isn't good to use because of platform dependency
			
			zipOutput.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public static String getCompressedString () {
		String inputString = "";
		
		try {
			FileInputStream input    = new FileInputStream( fileLocation );
			ZipInputStream  zipInput = new ZipInputStream ( input        );
			BufferedReader  streamReader = new BufferedReader( new InputStreamReader(zipInput)) ;
			
			while ( zipInput.getNextEntry() != null ) {
				inputString += streamReader.readLine();
			}
			zipInput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputString;
	}
}
