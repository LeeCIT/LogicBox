


package logicBox.gui.editor;

import java.awt.Font;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;



public class ToolboxCategoryPanel extends JPanel
{
	public ToolboxCategoryPanel( String title, ToolboxButton...buttons ) {
		super( new MigLayout( "wrap 3" ) );
		addCategory(title);
		
		for (ToolboxButton butt: buttons)
			add( butt, "w 48, h 32" );
	}
	
	
	
	/**
	 * Add a the category heading and separator.
	 * @param heading
	 * @param items
	 */
	private void addCategory( String title ) {
		add( createCategoryLabel( title ), "wrap" );
	}
	
	
	
	private JLabel createCategoryLabel( String title ) {
		JLabel label = new JLabel( title );
		Font   font  = label.getFont();
		Font   bold  = new Font( font.getName(), Font.BOLD, font.getSize() );
		
		label.setFont( bold );
		
		return label;
	}
}
