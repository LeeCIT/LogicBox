


package logicBox.gui;
import java.net.URL;
import javax.swing.ImageIcon;



/**
 * Loads icons from the resources.icons package.
 * TODO Doesn't work when exported to JAR
 * @author Lee Coakley
 * @author Robert O'Leary
 */
public class IconLoader
{
	/**
	 * Load an icon from the resources.icons package.
	 * Throws if no such icon exists.
	 */
	public static ImageIcon load( IconEnum icon ) {
		return loadIconFromResource( icon.getFilename() );
	}
	
	
	
	private static ImageIcon loadIconFromResource( String name ) {
		URL url = IconLoader.class.getClassLoader().getResource( "resources/icons/" + name );
		
		if (url == null)
			throw new RuntimeException( "Icon not found: " + name );
		
		return new ImageIcon( url );
	}
}
