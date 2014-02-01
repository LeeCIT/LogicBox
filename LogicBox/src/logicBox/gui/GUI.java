


package logicBox.gui;
import javax.swing.UIManager;



/**
 * Global GUI functions.
 * @author Lee Coakley
 */
public class GUI
{
	/**
	 * Makes the UI style take on the appearance of the underlying platform.
	 * Must be called before any UI elements are created.
	 */
	public static void setNativeStyle() {
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		}
		catch (Exception ex) {
			System.out.println( ex );
		}	
	}
}
