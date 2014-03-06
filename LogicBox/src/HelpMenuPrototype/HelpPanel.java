


package HelpMenuPrototype;



import java.awt.*;
import java.util.Map;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import logicBox.sim.component.ComponentType;
import logicBox.gui.SearchPanel;



public class HelpPanel extends JPanel
{
	
	
	
	private ComponentType componentType;
	private Map <ComponentType, String> componentMap;
	private JTextArea compDescription;
	
	
	
	public HelpPanel() {
		
	}
	
	
	
	public HelpPanel( Map<ComponentType, String> compMap ) {
		super();
		setLayout( new MigLayout() );
		this.componentMap = compMap;
		compDescription = new JTextArea();
		setSize(300, 300);
		addToPanel();
		displayDescription();
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
	 * Return get the JTextArea holding the description
	 * of the component.
	 * @return
	 */
	public JTextArea getComponentDescriptionArea(){
		return compDescription;
	}
	
	
	
	/**
	 * Add components to the HelpPanel
	 */
	private void addToPanel()
	{
		add( new SearchPanel<>("Search:") );
		JScrollPane scroll = new JScrollPane(compDescription);
		add( scroll, "w 100%, h 100%" );
		compDescription.setPreferredSize(new Dimension(getSize()));
		compDescription.setLineWrap(true); //Wrap the text when it reaches the end of the TextArea.
		compDescription.setWrapStyleWord(true); //Wrap at every word rather than every letter.
		compDescription.setEditable(false); //Text cannot be edited.
		
	}
}
