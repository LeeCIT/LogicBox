


package logicBox.gui;



/**
 * Defines searchable items for the SearchPanel class.
 * @author Lee Coakley
 */
public class Searchable<T>
{
	private T      obj;
	private String searchStr;
	
	
	
	public Searchable( T obj, String searchStr ) {
		this.obj       = obj;
		this.searchStr = searchStr;
	}
	
	
	
	/**
	 * Defines the object the SearchPanel will return when the search string matches.
	 */
	public T getObject() {
		return obj;
	}
	
	
	
	/**
	 * Defines what text is searched by the SearchPanel.
	 */
	public String getSearchString() {
		return searchStr;
	}
	
	
	
	/**
	 * Defines what is displayed in the SearchPanel's GUI.
	 * By default T's toString() method determines the displayed string.
	 * Override this if you want to display something else.
	 */
	public String getDisplayString() {
		return obj.toString();
	}
	
	
	
	public final String toString() {
		return getDisplayString();
	}
}
