


package logicBox.gui.editor;

import java.awt.Insets;
import java.awt.Window;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import prototypes.snappingProto.SnappingPrototype;



/**
 * The editor toolbox, where components are displayed in a pallet for easy creation.
 * @author John Murphy
 * @author Lee Coakley
 *
 */
public class Toolbox extends JToolBar
{
	public Toolbox( JFrame attachedFrame ) {
		super("Toolbox");
		setMargin( new Insets(0, 2, 0, 0) );
		setOrientation( JToolBar.VERTICAL );
		makeSnappable( attachedFrame );
	}
	
	
	
	/**
	 * Add the category heading then the list that will appear under the category
	 * @param category
	 * @param items
	 */
	public void addCategory( String title, ToolboxButton...buttons ) {
		addCategory( title );
		JPanel panel = new ToolboxCategory( buttons );
		add( panel );
	}
	
	
	
	/**
	 * Add a the category heading and separator.
	 * @param heading
	 * @param items
	 */
	private void addCategory( String title ) {
		add( new JLabel( title )      );
		add( new JToolBar.Separator() );
	}
	
	
	
	/**
	 * Add the snapping ability to the toolbox
	 * @param toolbar The toolbox to snap
	 * @param frame	  If you want to snap to other frames
	 * TODO this adds a component listener but never removes it.  Is this safe?
	 */
	private void makeSnappable( final JFrame frame ) {
		// Fix to a terrible flaw in JToolbar (what flaw?)
		this.addHierarchyListener(new HierarchyListener() {			
			public void hierarchyChanged(HierarchyEvent e) {
				final Window topLevel = SwingUtilities.windowForComponent( Toolbox.this );
				
				if (topLevel instanceof JDialog)
					((JDialog) topLevel).addComponentListener(new SnappingPrototype(frame)); // TODO
			}
		});
	}
	
	
	
	/**
	 * Prevent the toolbox from going horizontal
	 * TODO test
	 * @param toolbox
	 */
	private void preventToolBoxhorizontalOrientation() {
		addPropertyChangeListener( new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String propName = evt.getPropertyName();
				
				if (propName != null
				&&  propName.equals("orientation")) {
					Integer newValue = (Integer) evt.getNewValue();
					
					if (newValue.intValue() == JToolBar.HORIZONTAL)
						newValue = JToolBar.VERTICAL;
				}
			}
		});
	}
}
