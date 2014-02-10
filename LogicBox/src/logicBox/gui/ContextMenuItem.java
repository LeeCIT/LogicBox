


package logicBox.gui;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;

import logicBox.util.Callback;


/**
 * @author Lee Coakley
 */
public abstract class ContextMenuItem
{	
	protected abstract void addTo( JPopupMenu menu );
	
	
	
	public static void main(String[] args) {
		List<ContextMenuItem> items = new ArrayList<>();
		
		items.add( new ContextMenuString( null, "Skinner", new Callback() {
			public void execute() {
				System.out.println( "Yeah yeah yeah, skinner yeah" );
			}
		}));
		
		items.add( new ContextMenuSeparator() );
		
		items.add( new ContextMenuString( null, "Skinner", new Callback() {
			public void execute() {
				System.out.println( "Yeah yeah yeah, skinner yeah" );
			}
		}));
		
		ContextMenu menu = new ContextMenu( items );
	}
}
