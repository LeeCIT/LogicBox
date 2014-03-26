


package logicBox.gui.editor.toolbar;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;



/**
 * Button for the Editor toolbar
 * @author John
 */
public class EditorToolBarButton extends JButton
{
	public EditorToolBarButton( Icon icon, String tooltip ) {
		super();
		
		if (icon != null)
			setIcon( icon );
		
		setToolTipText( tooltip );
		setMaximumSize( new Dimension(25,23) );
		setFocusable( false );
		
		addRolloverListener();
	}
	
	
	
	private void addRolloverListener() {
		addMouseListener( new MouseAdapter() {
			public void mouseExited( MouseEvent e ) {
				getModel().setRollover( false );
			}
			
			public void mouseEntered( MouseEvent e ) {
				getModel().setRollover( true );
			}
		});
	}
	
	
	
	protected void paintBorder( Graphics g ) {
		if (getModel().isRollover())
			super.paintBorder( g );
	}
}
