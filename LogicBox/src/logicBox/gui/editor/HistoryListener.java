


package logicBox.gui.editor;
import java.io.Serializable;



/**
 * Allows an implementing class to to be affected by a history manager.
 * @author Lee Coakley
 * @see HistoryManager
 */
public interface HistoryListener<T extends Serializable>
{
	/**
	 * Return an object which encapsulates the state managed by the history stream.
	 */
	public T getHistoryState();
	
	
	
	/**
	 * Undo/redo: Use a history object to set the current state of the object.
	 */
	public void setStateFromHistory( T object );
}
