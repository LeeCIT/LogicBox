
package prototypes.zipPrototype;

public class CompressionMain {
	public static void main(String[] args) {
		String toSave = "This will be saved and compressed";
		
		// Compress
		FileMangerZip.saveStringCompressed(toSave);
		System.out.println("Done Compresing");
		
		// Decompress
		String compressedString = FileMangerZip.getCompressedString();
		System.out.println("compresed string: " + compressedString);
	}
}
