


package logicBox.gui;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import logicBox.sim.component.ComponentType;
import net.miginfocom.swing.MigLayout;



/**
 * Panel for searching a list of short text items.
 * @author Lee Coakley
 * @see    Searchable
 */
public class SearchPanel<T> extends JPanel
{
	private List<Searchable<T>> searchableItems;
	 
	private JLabel               label;
	private JTextField           searchField;
	private JButton              searchClear;
	private JList<Searchable<T>> searchList;
	private JScrollPane          scrollPane;
	
	
	
	private SearchPanel() {
		super( new MigLayout("insets 0, filly", "[grow]", "[][fill,grow][]") );
	}
	
	
	
	public SearchPanel( String labelText ){
		this( labelText, new ArrayList<Searchable<T>>() );
	}
	
	
	
	public SearchPanel( String labelText, List<Searchable<T>> searchables ) {
		this();
		setup( labelText, searchables );
	}
	
	
	
	/**
	 * Generates an event when the selected list item changes.
	 * It can change to nothing.  Check if the selection is non-null.
	 */
	public void addListSelectionListener( ListSelectionListener lsl ) {
		searchList.addListSelectionListener( lsl );
	}
	
	
	
	/**
	 * Generates an event when the clear button is used.
	 */
	public void addClearButtonListener( ActionListener lis ) {
		searchClear.addActionListener( lis );
	}	
	
	
	
	/**
	 * Set searchables to be used for searching and display.
	 * Searching does not alter this data.
	 * If the search field is empty then everything is displayed.
	 */
	public void setSearchableItems( List<Searchable<T>> items ) {		
		searchableItems = new ArrayList<Searchable<T>>( items );
		refreshDisplayedItems();
	}
	
	
	
	/**
	 * Get searchables.
	 */
	public List<Searchable<T>> getSearchableItems() {		
		return searchableItems;
	}
	
	
	
	/**
	 * Get the selected item.
	 * Returns null if nothing is selected.
	 * @return Selected item
	 */
	public T getSelectedItem() {
		return searchList.getSelectedValue().getObject();
	}
	
	
	
	/**
	 * Sets the selected item to the first item in the list which compares equal.
	 * If item is null, any existing selection is cleared.
	 * @param item 
	 * @return Whether it existed
	 */
	public boolean setSelectedItem( T item ) {
		if (item == null) {
			searchList.clearSelection();
			return false;
		}
		
		doSearchClear();
		
		for (int i=0; i<getSearchableItemCount(); i++) {
			if (searchableItems.get( i ).getObject().equals(item)) {
				setSelectedIndex( i );
				return true;
			}
		}
		
		return false;
	}
	
	

	public boolean hasSelectedItem() {
		return ( ! searchList.isSelectionEmpty());
	}
	
	
	
	public int getSearchableItemCount() {
		return searchableItems.size();
	}
	
	
	
	public int getDisplayedItemCount() {
		return searchList.getModel().getSize();
	}
	
	
	
	public int getSelectedIndex() {
		return searchList.getSelectedIndex();
	}
	
	
	
	public void setSelectedIndex( int index ) {
		searchList.setSelectedIndex( index );
	}
	
	
	
	public String getSearchText() {
		return searchField.getText();
	}
	
	
	
	public void clearSearch() {
		doSearchClear();
	}
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// Internals
	/////////////////////////////////////////////////////////////////////////
	
	private void setup( String labelText, List<Searchable<T>> searchableItems ) {
		setupComponents( labelText );
		setupActions();
		setSearchableItems( searchableItems );
	}
	
	
	
	private void setupComponents( String labelText ) {		
		label       = new JLabel( labelText );
		searchField = new JTextField( 12 );
		searchClear = new JButton( "Clear" );
		
		searchList = new JList<Searchable<T>>();
		searchList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		
		scrollPane = new JScrollPane( searchList );
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		scrollPane.setVerticalScrollBarPolicy  ( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS      );
		
		add( label,       "wrap, growy 0" );
		add( scrollPane,  "wrap, grow, hmin 72px" );
		add( searchField, "growx, split 2" );
		add( searchClear );
	}
	
	
	
	private void setupActions() {
		searchField.addKeyListener( new KeyAdapter() {
			public void keyReleased(KeyEvent ev) {
				doSearchAndDisplayResults();
			} 
		});
		
		searchClear.addActionListener( new ActionListener() { 
			public void actionPerformed(ActionEvent ev) {
				doSearchClear();
			}
		});
	}
	
	
	
	private void doSearchAndDisplayResults() {
		String userStr = searchField.getText();
		
		if (userStr.isEmpty()) {
			refreshDisplayedItems();
			return;
		}
		
		List<Searchable<T>> searchResult = new ArrayList<Searchable<T>>();
		
		for (Searchable<T> item: searchableItems) 
			if (searchCompare( item.getSearchString(), userStr )) 
				searchResult.add( item );

		setDisplayedItems( searchResult );
	}
	
	
	
	/**
	 * Compare the list item string with the user's search input.
	 * Any partial case-insensitive match counts.
	 * @param ref Reference string (internal)
	 * @param com Comparison string (user input)
	 * @return bool, whether com matches ref according to the search criterion.
	 */
	private boolean searchCompare( String ref, String com )	{
		String refLower = ref.toLowerCase();
		String comLower = com.toLowerCase();
		return refLower.contains( comLower );
	}
	
	
	
	private void doSearchClear() {
		searchField.setText( "" );
		refreshDisplayedItems();
	}

	
	
	private void setDisplayedItems( List<Searchable<T>> items ) {
		searchList.setListData( items.toArray( new Searchable[items.size()] ) );
	}
	
	
	
	private void refreshDisplayedItems() {
		setDisplayedItems( searchableItems );
	}
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	// Test
	/////////////////////////////////////////////////////////////////////////
	
	public static void main( String[] args ) {
		GUI.setNativeStyle();
		JFrame frame = new JFrame();
		
		List<Searchable<ComponentType>> searchables = new ArrayList<>();
		
		for (ComponentType type: ComponentType.values())
			searchables.add( new Searchable<ComponentType>( type, type.name() ) );
		
		
		final SearchPanel<ComponentType> sp = new SearchPanel<ComponentType>( "Component Search", searchables );
		
		sp.addListSelectionListener( new ListSelectionListener() {
			public void valueChanged( ListSelectionEvent ev ) {
				if (sp.hasSelectedItem())
					System.out.println( sp.getSelectedItem() );
			}
		});
		
		sp.setSelectedItem( ComponentType.demux );
		
		frame.add( sp );
		frame.pack();
		frame.setVisible( true );
	}
}




























