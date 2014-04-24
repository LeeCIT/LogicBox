


package logicBox.gui.editor.toolbox;

import java.awt.Font;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;



public class ToolboxCategoryPanel extends JPanel
{
	public ToolboxCategoryPanel( String title, int wrapAfter, ButtTargetable...buttons ) {
		super( new MigLayout( "wrap " + wrapAfter ) );
		addCategory(title);
		
		for (ButtTargetable butt: buttons) {
			int w = 48;
			int h = 32;
			
			if (butt instanceof ToolboxButtonSplit)
				w += ((ToolboxButtonSplit) butt).getSplitWidth();
			
			add( butt.getButton(), "w "+w+", h "+h+" , wmax "+w+", hmax "+h+"" );
		}
	}
	
	
	
	/**
	 * Add a the category heading and separator.
	 * @param heading
	 * @param items
	 */
	private void addCategory( String title ) {
		add( createCategoryLabel( title ), "span 3" );
	}
	
	
	
	private JLabel createCategoryLabel( String title ) {
		JLabel label = new JLabel( title );
		Font   font  = label.getFont();
		Font   bold  = new Font( font.getName(), Font.BOLD, font.getSize() );
		
		label.setFont( bold );
		
		return label;
	}
}
