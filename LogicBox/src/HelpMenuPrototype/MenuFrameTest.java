


package HelpMenuPrototype;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import logicBox.sim.ComponentType;
import logicBox.util.Util;



public class MenuFrameTest extends JFrame 
{	
	public MenuFrameTest() 
	{	
		HelpMenu menu = new HelpMenu( getComponentMap() );
		
		
		setLayout( new MigLayout("debug", "[fill,grow]", "[fill,grow]") );
		setSize(300,300);
		add( menu, "wrap" );
		setVisible(true);
		
		
		menu.setDisplayedInfo( ComponentType.gateAnd );
		
		menu.addMouseListener( new ClickTest(menu) );
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
		
		return compMap;
	}
	
	

	public static void main(String[] args) 
	{
		MenuFrameTest frame = new MenuFrameTest();
		
		
		System.out.println( MenuFrameTest.getRandomComp() );
	}
	
	
	
	private class ClickTest extends MouseAdapter
	{
		private HelpMenu menu;
		
		
		public ClickTest( HelpMenu menu ) {
			this.menu = menu;
		}
		
		
		
		@Override
		public void mouseClicked(MouseEvent e) {
			menu.setDisplayedInfo(getRandomComp());
		}
		
		
	}
}
