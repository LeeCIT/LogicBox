


package logicBox.gui.help;
import java.util.ArrayList;
import java.util.List;
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
 */
public class HelpPanel extends JPanel
{
	private SearchPanel<ComponentType> searchPanel;
	private GraphicInfoPanel           graphicInfoPanel;
	
	
	
	public HelpPanel() {
		super();
		setupComponents();
		setupLayout();
	}
	
	
	
	public void showInfoFor( ComponentType type ) {
		graphicInfoPanel.showInfoFor( type );
	}
	
	
	
	public void setSelectedComponent( ComponentType type ) {
		searchPanel.setSelectedItem(type);
	}
	
	
	
	private void setupComponents() {
		createSearchPanel();
		graphicInfoPanel = new GraphicInfoPanel();
	}
	
	
	
	private void setupLayout() {
		setLayout( new MigLayout( "", "[grow,fill]", "[grow,fill]" ) );
		
		JSplitPane splitPane = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT,
			searchPanel,
			graphicInfoPanel
		);
		
		splitPane.setResizeWeight( 0.15 );
			
		add( splitPane );
	}
	
	
	
	private void createSearchPanel() {
		List<Searchable<ComponentType>> searchables = new ArrayList<>();

		for (ComponentType type: ComponentType.values())
			searchables.add( new Searchable<ComponentType>( type, type.getName() ) );
		
		searchPanel = new SearchPanel<ComponentType>( searchables );
		
		searchPanel.addListSelectionListener( new ListSelectionListener() {
			public void valueChanged( ListSelectionEvent ev ) {
				if (searchPanel.hasSelectedItem()) {
					showInfoFor( searchPanel.getSelectedItem() );
				}
			}
		});
	}
}


















