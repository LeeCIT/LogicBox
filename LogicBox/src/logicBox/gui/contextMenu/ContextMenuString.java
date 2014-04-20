


package logicBox.gui.contextMenu;

import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;



/**
 * 
 * @author Lee Coakley
 */
public class ContextMenuString extends ContextMenuItem
{	
	public Icon           icon;
	public String         name;
	public ActionListener al;
	public char           mnemonic;
	
	
	
	public ContextMenuString( Icon icon, String name, char mnemonic, ActionListener al ) {
		this.icon     = icon;
		this.name     = name;
		this.al       = al;
		this.mnemonic = mnemonic;
	}
	
	
	
	protected void addTo( final JPopupMenu menu ) {
		JMenuItem item = new JMenuItem();
		item.setText( name );
		
		if (icon != null)
			item.setIcon( icon );
		
		if (mnemonic != 0)
			item.setMnemonic( mnemonic );
		
		if (al != null)
			item.addActionListener( al );
		
		menu.add( item );
	}
}
