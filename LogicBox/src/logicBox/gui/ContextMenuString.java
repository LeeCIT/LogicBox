


package logicBox.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import logicBox.util.Callback;


/**
 * 
 * @author Lee Coakley
 */
public class ContextMenuString extends ContextMenuItem
{	
	public Icon     icon;
	public String   name;
	public Callback callback;
	public char     mnemonic;
	
	
	
	public ContextMenuString( Icon icon, String name, char mnemonic, Callback callback ) {
		this.icon     = icon;
		this.name     = name;
		this.callback = callback;
		this.mnemonic = mnemonic;
	}
	
	
	
	protected void addTo( final JPopupMenu menu ) {
		JMenuItem item = new JMenuItem();
		item.setName( name );
		
		if (icon != null)
			item.setIcon( icon );
		
		if (mnemonic != 0)
			item.setMnemonic( mnemonic );
		
		if (callback != null)
			item.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					callback.execute();
				}
			});
		
		menu.add( item );
	}
}
