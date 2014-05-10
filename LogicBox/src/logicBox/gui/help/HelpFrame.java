


package logicBox.gui.help;
import javax.swing.JFrame;
import logicBox.gui.GUI;
import logicBox.gui.IconEnum;
import logicBox.gui.IconLoader;
import logicBox.sim.component.ComponentType;



/**
 * Shows a help window with information about how the various components work.
 * @author Shaun O'Donovan
 * @author Lee Coakley
 */
public class HelpFrame extends JFrame 
{
	private static HelpFrame instance;
	
	private HelpPanel helpPanel;
	
	
	
	private HelpFrame() {
		super( "LogicBox - Help" );
		
		setHelpIcon();
		setupComponents();
		
		pack();
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setSize( 720, 480 );
	}
	
	
	
	public static HelpFrame getInstance() {
		if (instance == null)
			instance = new HelpFrame();
		
		return instance;
	}
	
	
	
	public void showInfoFor( ComponentType type ) {
		helpPanel.showInfoFor( type );
	}
	
	
	
	public void setSelectedComponent( ComponentType type ) {
		helpPanel.setSelectedComponent( type );
	}
	
	
	
	private void setHelpIcon() {
		try {
			setIconImage( IconLoader.load(IconEnum.help).getImage() );
		}
		catch (Exception ex) {
			ex.printStackTrace(); // Don't give a crap if this fails.
		}
	}
	
	
	
	private void setupComponents() {
		helpPanel = new HelpPanel();
		add( helpPanel );
	}
	
	
	
	
	
	public static void main(String[] args) {
		GUI.setNativeStyle();
		new HelpFrame().setVisible( true );
	}
}
