


package logicBox.gui.help;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import logicBox.sim.component.*;
import logicBox.gui.SearchPanel;
import logicBox.gui.Searchable;



/**
 * Shows information about various components in the simulator.
 * @author Shaun O'Donovan
 * @author Lee Coakley
 * TODO: create for a specific type of component
 */
public class HelpPanel extends JPanel
{
	private ComponentType componentType;
	private Map<ComponentType,ComponentHelpInfo> componentMap;
	private JTextPane textPane = new JTextPane();
	private SearchPanel<ComponentType> searchPanel;
	
	
	
	public HelpPanel( Map<ComponentType,ComponentHelpInfo> compMap ) {
		super();
		this.componentMap = compMap;
		textPane.setContentType("text/html");
		createSearchPanel();
		addComponents();
	}
	
	
	
	/**
	 * Display the description of the specified component.
	 */
	private void displayDescription() {
		textPane.setText("");
		StringBuilder builder = new StringBuilder();
		builder.append("<html><body>");
		builder.append("<h1>" + getCompName() +"</h1>");
		builder.append("<hr>");
		builder.append("<p>" + getCompDescription() + "</p>");
		builder.append("</body></html>");
		textPane.setText(builder.toString());
	}
	
	
	
	/**
	 * Return the help menu name for the component
	 * passed in.
	 * @return
	 */
	private String getCompName() {
		if ( ! componentMap.containsKey(componentType) )
			return "Missing info";
			
			return componentMap.get(componentType).getCompName();	
	}
	
	
	
	/**
	 * Return the help menu description for the component
	 * passed in.
	 * @return
	 */
	private String getCompDescription() {
		if ( ! componentMap.containsKey(componentType) )
			return "Missing info";
			
			return componentMap.get(componentType).getCompDescription();	
	}
	
	
	
	
	/**
	 * Change the help information currently displayed.
	 * @param compType
	 */
	public void setDisplayedInfo(ComponentType compType) {
		this.componentType = compType;
		displayDescription();
	}
	
	

	/**
	 * Return get the JTextArea holding the description
	 * of the component.
	 * @return
	 */
	public JTextPane getComponentDescriptionArea(){
		return textPane;
	}
	
	
	
	private void createSearchPanel() {
		List<Searchable<ComponentType>> searchables = new ArrayList<>();

		for (ComponentType type: ComponentType.values())
			searchables.add( new Searchable<ComponentType>( type, type.name() ) );
		
		searchPanel = new SearchPanel<ComponentType>( searchables );
		
		searchPanel.addListSelectionListener( new ListSelectionListener() {
			public void valueChanged( ListSelectionEvent ev ) {
				if (searchPanel.hasSelectedItem()) {
					componentType = searchPanel.getSelectedItem();
					displayDescription();
				}
			}
		});
	}
	
	
	
	private void addComponents() {
		setLayout( new MigLayout( "", "[grow,fill]", "[grow,fill]" ) );
		
		createSearchPanel();
		JScrollPane scrollPane = new JScrollPane( textPane );
		JSplitPane  splitPane  = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, searchPanel, scrollPane );
		
		textPane.setEditable( false ); // Text cannot be edited.
		splitPane.setResizeWeight( 0.15 );
		
		add( splitPane );
	}
}


















