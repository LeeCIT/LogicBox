package prototypes.ToolBarProto;

import java.awt.Insets;
import java.awt.Window;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

import prototypes.snappingProto.SnappingPrototype;



public class Toolbox extends JToolBar
{	
	public Toolbox() {}
	
	
	/**
	 * Creates a toolbox with a frame title and the default orientation
	 * @param frameName
	 * @param vertical
	 */
	public Toolbox(String frameName, int vertical) {
		super(frameName, vertical);
		super.setMargin(new Insets(0, 2, 0, 0));
		super.setOrientation(JToolBar.VERTICAL);
	}


	/**
	 * For adding many items at once. Takes a list of items and adds them to the toolbox
	 * @param items
	 */
	public void addToolBoxItemList(List<ToolboxItem> items) {
		for (ToolboxItem toolbarItem: items)
			toolbarItem.addTo(this);
	}
	
	
	
	/**
	 * Add a individual toolbox item
	 * @param item
	 */
	public void addToolBoxItemButton(ToolboxItem item) {
		item.addTo(this);
	}
	
	
	
	/**
	 * Add a the category heading and 
	 * @param heading
	 * @param items
	 */
	public void addCategory(String heading) {
		add(new JLabel(" " + heading));
		this.add(new JToolBar.Separator());
	}
	
	
	
	
	/**
	 * Add the snapping ability to the toolbox
	 * @param toolbar	The toolbox to snap
	 * @param frame		If you want to snap to other frames
	 * @param snappingdistance	The distance the object should snap to
	 */
	public void addSnapping(final JFrame frame, int snappingdistance) {
		// Fix to a terrible flaw in JToolbar
		this.addHierarchyListener(new HierarchyListener() {			
			public void hierarchyChanged(HierarchyEvent e) {
				JToolBar bar = (JToolBar) e.getComponent();
				final Window topLevel = SwingUtilities.windowForComponent(bar);
				if (topLevel instanceof JDialog) {
					((JDialog) topLevel).addComponentListener(new SnappingPrototype(frame));
				}    
			}
		});
	}
	
		
	
	/**
	 * Add the snapping ability to the toolbox. It will snap to the outer edges of the screen
	 * @param toolbar	The toolbox to snap
	 */
	public void addSnapping() {
		// Fix to a terrible flaw in JToolbar
		this.addHierarchyListener(new HierarchyListener() {			
			public void hierarchyChanged(HierarchyEvent e) {
				JToolBar bar = (JToolBar) e.getComponent();
				final Window topLevel = SwingUtilities.windowForComponent(bar);
				if (topLevel instanceof JDialog) {
					((JDialog) topLevel).addComponentListener(new SnappingPrototype());
				}    
			}
		});
	}
	
	
	
	/**
	 * Add the snapping ability to the toolbox
	 * @param toolbar	The toolbox to snap
	 * @param frame	If you want to snap to other frames
	 */
	public void addSnapping(final JFrame frame) {
		// Fix to a terrible flaw in JToolbar
		this.addHierarchyListener(new HierarchyListener() {			
			public void hierarchyChanged(HierarchyEvent e) {
				JToolBar bar = (JToolBar) e.getComponent();
				final Window topLevel = SwingUtilities.windowForComponent(bar);
				if (topLevel instanceof JDialog) {
					((JDialog) topLevel).addComponentListener(new SnappingPrototype(frame));
				}    
			}
		});
	}
	
	
	
	/**
	 * Pass in a list of buttons and they will be added to the toolbox
	 * @param buttons
	 */
	public void addListOfButtons(List<ToolboxButtonCallback> buttons) {
		this.add(new ToolboxPanel(buttons));
	}
	
	
	
	
	
	
	
	
	/**
	 * Prevent the toolbox from going horizontal
	 * @param toolbox
	 */
	public static void preventToolBoxhorizontalOrientation(final JToolBar toolbox) {
		toolbox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String propName = evt.getPropertyName();
				if ("orientation".equals(propName)) {
					Integer oldValue = (Integer) evt.getOldValue();
					Integer newValue = (Integer) evt.getNewValue();
					if (newValue.intValue() == JToolBar.HORIZONTAL) {
						newValue = JToolBar.VERTICAL;
					}	
				}
				}});
	}
	
	
	
	
	
}
