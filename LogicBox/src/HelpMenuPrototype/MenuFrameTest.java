


package HelpMenuPrototype;



import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import logicBox.sim.component.ComponentType;
import logicBox.util.Util;



public class MenuFrameTest extends JFrame 
{	
	
	
	
	public MenuFrameTest() 
	{	
		HelpPanel menu = new HelpPanel( getComponentMap() );
		
		
		
		setLayout( new MigLayout() );
		setSize(300,300);
		add( menu, "wrap" );
		setVisible(true);
		
		menu.setDisplayedInfo( ComponentType.gateOr );
	}
	
	
	
	private static ComponentType getRandomComp() {
		ComponentType[] components = ComponentType.values();
		int random = Util.randomIntRange(0, components.length);
		return components[random];
	}

	
	
	private Map<ComponentType, String> getComponentMap() {
		Map<ComponentType, String> compMap = new HashMap<ComponentType, String>();
		
		compMap.put(ComponentType.gateAnd, "And gate info");
		compMap.put(ComponentType.gateNot, "Not gate info");
		compMap.put(ComponentType.gateOr, "This right here will be everything and anything you need to know about what an or gate does!");
		
		return compMap;
	}
	
	
	
	public static void main(String[] args) 
	{
		MenuFrameTest frame = new MenuFrameTest();
		
		
		System.out.println( MenuFrameTest.getRandomComp() );
	}
	
	
	
}
