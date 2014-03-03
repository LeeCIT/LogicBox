
package prototypes.zipPrototype;

public class CompressionMain {
	public static void main(String[] args) {
		String toSave = "This will be saved and compressed";
		
		FileMangerZip.saveStringCompressed(toSave);
		System.out.println("Done Compresing");
	
		
	}
}
