


package logicBox.gui.editor.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;
import logicBox.gui.editor.EditorWorld;



/**
 * Handles reading and writing .LBX files.
 * Employs class name remapping for backwards compatibility.
 * @author Lee Coakley
 */
public abstract class Storage
{
	public static EditorWorld load( File file ) throws ClassNotFoundException, IOException {
		byte[] data = readBytes( file );
		
		try {
			return decompress( data );
		}
		catch (ZipException ex) { // Older files weren't uncompressed.
			return readUncompressed( data );
		}
	}
	
	
	
	public static void save( File file, EditorWorld world ) throws IOException {
		writeBytes( file, compress(world) );
	}
	
	
	
	private static Path toPath( File file ) {
		return Paths.get( file.getPath() );
	}
	
	
	
	private static byte[] readBytes( File file ) throws IOException {		
		return Files.readAllBytes( toPath(file) );
	}
	
	
	
	private static void writeBytes( File file, byte[] data ) throws IOException {
		Files.write( toPath(file), data );
	}
	
	
	
	private static EditorWorld readUncompressed( byte[] bytes ) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream( bytes );
		ObjectInputStream    objStream  = new RemappableObjectInputStream( byteStream  );
		
		EditorWorld world = (EditorWorld) objStream.readObject();
		
		objStream.close();
		
		return world;
	}
	
	
	
	private static byte[] compress( EditorWorld world ) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		GZIPOutputStream      zipStream  = new GZIPOutputStream( byteStream );
		ObjectOutputStream    objStream  = new ObjectOutputStream( zipStream  );
		
		objStream.writeObject( world );
		
		objStream .close();
		zipStream .close();
		byteStream.close();
		
		return byteStream.toByteArray();
	}
	
	
	
	private static EditorWorld decompress( byte[] compressed ) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream( compressed );
		GZIPInputStream      zipStream  = new GZIPInputStream( byteStream );
		ObjectInputStream    objStream  = new RemappableObjectInputStream( zipStream  );
		
		EditorWorld world = (EditorWorld) objStream.readObject();
		
		objStream .close();
		zipStream .close();
		byteStream.close();
		
		return world;
	}
}
