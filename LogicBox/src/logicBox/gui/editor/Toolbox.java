


package logicBox.gui.editor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import logicBox.gui.GUI;
import prototypes.snappingProto.SnappingPrototype;



/**
 * The editor toolbox, where components are displayed in a palette for easy creation.
 * @author John Murphy
 * @author Lee Coakley
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
	 * Add a category of buttons.then the list that will appear under the category
	 * @param category
	 * @param items
	 */
	public void addCategory( String title, ToolboxButton...buttons ) {
		addCategory( title );
		JPanel panel = new ToolboxCategoryPanel( buttons );
		add( panel );
	}
	
	
	
	/**
	 * Add a the category heading and separator.
	 * @param heading
	 * @param items
	 */
	private void addCategory( String title ) {
		add( createCategoryLabel( title ) );
		add( new JToolBar.Separator() );
	}
	
	
	
	private JLabel createCategoryLabel( String title ) {
		JLabel label = new JLabel( title );
		Font   font  = label.getFont();
		Font   bold  = new Font( font.getName(), Font.BOLD, font.getSize() );
		
		label.setFont( bold );
		
		return label;
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
			public void hierarchyChanged( HierarchyEvent ev ) {
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
	
	
	
	
	
	public static void main( String[] args ) {
		GUI.setNativeStyle();
		
		JFrame frame = new EditorFrame();
		
		ToolboxButton[] buttons = {
			new ToolboxButton( GraphicGen.generateGateBuffer(), "test", null ),
			new ToolboxButton( GraphicGen.generateGateNot(),    "test", null ),
			new ToolboxButton( GraphicGen.generateGateAnd(2),   "test", null ),
			new ToolboxButton( GraphicGen.generateGateNand(2),  "test", null ),
			new ToolboxButton( GraphicGen.generateGateOr(2),    "test", null ),
			new ToolboxButton( GraphicGen.generateGateNor(2),   "test", null ),
			new ToolboxButton( GraphicGen.generateGateXor(2),   "test", null ),
			new ToolboxButton( GraphicGen.generateGateXnor(2),  "test", null )
		};
		
		Toolbox toolbox = new Toolbox( frame );
		toolbox.addCategory( "Gates", buttons );
		
		frame.setSize( new Dimension(600,600) );
		frame.add( toolbox, "west" );
		frame.add( new EditorPanel() );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}






























