


package HelpMenuPrototype;



import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import net.miginfocom.swing.MigLayout;
import logicBox.sim.component.*;
import logicBox.gui.SearchPanel;
import logicBox.gui.Searchable;



public class HelpPanel extends JPanel
{
	
	
	
	private ComponentType componentType;
	private Map <ComponentType, ComponentInfo> componentMap;
	private JTextPane compDescription = new JTextPane();
	private SearchPanel<ComponentType> search; 
	
	
	
	public HelpPanel() {}

	
	
	public HelpPanel( Map<ComponentType, ComponentInfo> compMap  ) {
		super();
		setLayout( new MigLayout() );
		this.componentMap = compMap;
		compDescription.setContentType("text/html");
		componentSearch();
		setSize(800, 300);
		addToPanel();
	}
	
	
	
	/**
	 * Display the description of the specified component.
	 */
	private void displayDescription() {
			compDescription.setText("");
			StringBuilder builder = new StringBuilder();
			builder.append("<html>");
			builder.append("<h1>" + getCompName() +"</h1>");
			builder.append("<hr>");
			builder.append("Un-edited text");
			builder.append("</html>");
			compDescription.setText(builder.toString());
	}
	
	
	
	/**
	 * Return the help menu description for the component
	 * passed in.
	 * @return
	 */
	private String getCompName(){
		if ( ! componentMap.containsKey(componentType) )
			return "Missing info";
			
			return componentMap.get(componentType).getCompName();	
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
		return compDescription;
	}
	
	
	
	
	private void componentSearch() {
		
		
		////////////////////////////////////////////////////////////////////
		// Search Test
		///////////////////////////////////////////////////////////////////
		
		List<Searchable<ComponentType>> searchables = new ArrayList<>();

		for (ComponentType type: ComponentType.values())
			searchables.add( new Searchable<ComponentType>( type, type.name() ) );
		
		search = new SearchPanel<ComponentType>("Component Search:", searchables);
		
		search.addListSelectionListener( new ListSelectionListener() {
			public void valueChanged( ListSelectionEvent ev ) {
				if (search.hasSelectedItem()) {
					componentType = search.getSelectedItem();
					displayDescription();
				}
			}
		});
	}
	
	
	/**
	 * Add components to the HelpPanel
	 */
	private void addToPanel()
	{
		add( search );
		JScrollPane scroll = new JScrollPane(compDescription);
		add( scroll, "w 80%, h 100%" );
		compDescription.setPreferredSize(new Dimension(getSize()));
		compDescription.setEditable(false); //Text cannot be edited.
		
	}
	
	
	
	
}
