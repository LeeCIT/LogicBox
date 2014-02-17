


package HelpMenuPrototype;



import java.awt.*;
import java.util.Map;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import logicBox.sim.ComponentType;



public class HelpPanel extends JPanel
{
	
	
	
	private ComponentType componentType;
	private Map <ComponentType, String> componentMap;
	private JTextArea compDescription;
	
	
	
	public HelpPanel() {
		
	}
	
	
	
	public HelpPanel( Map<ComponentType, String> compMap ) {
		super( new MigLayout() );
		setLayout( new MigLayout("fill") );
		this.componentMap = compMap;
		compDescription = new JTextArea();
		setSize(300, 300);
		addToPanel();
		displayDescription();
	}
	
	
	
	/**
	 * Draw the help information onto the panel.
	 */
	public void paintComponent( Graphics g ) {	
		//super.paintComponent( g );
		
		//if ( getDescription().length() > (super.getX()) )
		//{
		//	String holderOne = getDescription().substring(0, super.getX()-1) + " \n";
		//	g.drawString(holderOne, 10, 15);
		//	g.drawString(getDescription(), 10, 25);
		//}
		//else
		//g.drawString(getDescription(), 10, 15);
		
		//System.out.println(getX());
		//System.out.println(getDescription().length());
	}
	
	
	
	/**
	 * Display the description of the specified component.
	 * @param compDes
	 */
	private void displayDescription() {
		compDescription.setText(getDescription());
	}
	
	
	
	/**
	 * Return the help menu description for the component
	 * passed in.
	 * @param componentType
	 * @return
	 */
	private String getDescription(){
		if ( ! componentMap.containsKey(componentType) ){
			return "Missing info";
		}
		
		return componentMap.get(componentType);
	}
	
	
	
	/**
	 * Change the help information currently displayed.
	 * @param compType
	 */
	public void setDisplayedInfo(ComponentType compType) {
		this.componentType = compType;
		//repaint();
		displayDescription();
	}
	
	
	
	/**
	 * Add components to the HelpPanel
	 */
	private void addToPanel()
	{
		JScrollPane scroll = new JScrollPane(compDescription);
		add( scroll, "grow" );
		compDescription.setPreferredSize(new Dimension(getSize()));
		compDescription.setLineWrap(true); //Wrap the text when it reaches the end of the TextArea.
		compDescription.setWrapStyleWord(true); //Wrap at every word rather than every letter.
		compDescription.setEditable(false); //Text cannot be edited.
		
	}
}
