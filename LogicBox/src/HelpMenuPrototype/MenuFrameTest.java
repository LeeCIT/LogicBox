


package HelpMenuPrototype;



import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import logicBox.gui.GUI;
import logicBox.sim.component.*;
import logicBox.util.Geo;



public class MenuFrameTest extends JFrame 
{	
	
	
	
	public MenuFrameTest() 
	{	
		HelpPanel menu = new HelpPanel( getComponentTypeMap() );
		
		
		setLayout( new MigLayout() );
		//setSize(840,300);
		add( menu, "wrap" );
		pack();
		setVisible(true);
		
		menu.setDisplayedInfo( ComponentType.gateOr );
	}
	
	
		
	private Map<ComponentType, String> getComponentTypeMap() {
		Map<ComponentType, String> compMap = new HashMap<ComponentType, String>();
		
		for (ComponentType type: ComponentType.values())
			compMap.put(type, type.name());
		
		//compMap.put(ComponentType.gateAnd, "And gate info");
		//compMap.put(ComponentType.gateNot, "Not gate info");
		//compMap.put(ComponentType.gateOr, "This right here will be everything and anything you need to know about what an or gate does!");
		
		return compMap;
	}
	
	
	
	public static void main(String[] args) 
	{
		GUI.setNativeStyle();
		MenuFrameTest frame = new MenuFrameTest();
	}
	
	
	
}
