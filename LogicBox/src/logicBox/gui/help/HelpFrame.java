


package logicBox.gui.help;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import logicBox.gui.GUI;
import logicBox.gui.IconEnum;
import logicBox.gui.IconLoader;
import logicBox.sim.component.*;



/**
 * Shows a help window with information about how the various components work.
 * @author Shaun O'Donovan
 * @author Lee Coakley
 * TODO: add constructor for a specific type of component
 * TODO: this is a singleton; creating another should change an existing one instead
 * TODO: show/hide on button press in main GUI
 */
public class HelpFrame extends JFrame 
{
	private HelpPanel helpPanel;
	private static HelpFrame instance = null;
	
	
	
	private HelpFrame() {
		super( "LogicBox - Help" );
		
		setHelpIcon();
		setupComponents();
		
		pack();
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setSize( 720, 480 );
		setVisible( true );
	}
	
	
	
	private HelpPanel getHelpPanel() {
		return helpPanel;
	}
	
	
	
	public HelpFrame getInstance() {
		if (instance == null)
			instance = new HelpFrame();
		
		return instance;
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
		helpPanel = new HelpPanel( getComponentTypeMap() );
		add( helpPanel );
	}
	
	
	
	private static Map<ComponentType, ComponentHelpInfo> getComponentTypeMap() {
		Map<ComponentType, ComponentHelpInfo> compMap = new HashMap<ComponentType, ComponentHelpInfo>();
		
		ComponentHelpInfo andInfo = new ComponentHelpInfo("And Gate", "Description of an And Gate", ComponentType.gateAnd);
		ComponentHelpInfo orInfo  = new ComponentHelpInfo("OR Gate", "Description of an Or Gate", ComponentType.gateOr);
		
		compMap.put(ComponentType.gateAnd, andInfo);
		compMap.put(ComponentType.gateOr, orInfo);
		
		return compMap;
	}
	
	
	
	
	
	public static void main(String[] args) {
		GUI.setNativeStyle();
		new HelpFrame().getHelpPanel().setDisplayedInfo( ComponentType.gateOr );;
	}
}
