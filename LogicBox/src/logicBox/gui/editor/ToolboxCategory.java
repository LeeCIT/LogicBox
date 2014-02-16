


package logicBox.gui.editor;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;



public class ToolboxCategory extends JPanel
{
	public ToolboxCategory( ToolboxButton...buttons ) {
		super( new MigLayout( "wrap 3" ) );
		
		for (ToolboxButton butt: buttons)
			add( butt, "w 64, h 64" );
	}
}
