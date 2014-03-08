


package HelpMenuPrototype;



import java.awt.*;
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



public class HelpPanel extends JPanel
{
	
	
	
	private ComponentType componentType;
	private Map <ComponentType, String> componentMap;
	private JTextArea compDescription;
	private SearchPanel<ComponentType> search; 
	
	
	
	public HelpPanel() {
		
	}
	
	
	
	public HelpPanel( Map<ComponentType, String> compMap  ) {
		super();
		setLayout( new MigLayout() );
		
		
		this.componentMap = compMap;
		
		
		compDescription = new JTextArea();
		
		
		componentSearch();
		
		setSize(800, 300);
		addToPanel();
	}
	
	
	
	/**
	 * Display the description of the specified component.
	 */
	private void displayDescription() {
		//compDescription.append(getCompName() + "\n");
		compDescription.setText(getDescription());
	}
	
	
	
	/**
	 * Return the help menu description for the component
	 * passed in.
	 * @return
	 */
	private String getDescription(){
		if ( ! componentMap.containsKey(componentType) )
			return "Missing info";
			
			return componentMap.get(componentType);	
			
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
	public JTextArea getComponentDescriptionArea(){
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
				if (search.hasSelectedItem())
				{
					componentType = search.getSelectedItem();
					displayDescription();
				}
				//System.out.println( search.getSelectedItem() );
			}
		});

		//search.setSelectedItem( ComponentType.demux );
		
	}
	
	
	/**
	 * Add components to the HelpPanel
	 */
	private void addToPanel()
	{
		add( search );
		JScrollPane scroll = new JScrollPane(compDescription);
		add( scroll, "w 100%, h 100%" );
		compDescription.setPreferredSize(new Dimension(getSize()));
		compDescription.setLineWrap(true); //Wrap the text when it reaches the end of the TextArea.
		compDescription.setWrapStyleWord(true); //Wrap at every word rather than every letter.
		compDescription.setEditable(false); //Text cannot be edited.
		
	}
	
	
	
	
}
