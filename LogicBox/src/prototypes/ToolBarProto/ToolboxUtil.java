package prototypes.ToolBarProto;

import java.awt.Window;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import prototypes.snappingProto.SnappingPrototype;

public class ToolboxUtil{

	/**
	 * Add a category to the JToolbar
	 * @param toolbox	The toolbox to add the category to
	 * @param heading	Category name and what is shown
	 */
	public static void addCategory(JToolBar toolbox, String heading) {
		toolbox.add(new JLabel(heading));
		toolbox.add(new JToolBar.Separator());
	}


	/**
	 * Add the snapping ability to the toolbox
	 * @param toolbar	The toolbox to snap
	 * @param frame	If you want to snap to other frames
	 */
	public static void addSnapping(JToolBar toolbox, final JFrame frame) {
		// Fix to a terrible flaw in JToolbar
		toolbox.addHierarchyListener(new HierarchyListener() {			
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
	public static void addSnapping(JToolBar toolbox) {
		// Fix to a terrible flaw in JToolbar
		toolbox.addHierarchyListener(new HierarchyListener() {			
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
	 * @param frame		If you want to snap to other frames
	 * @param snappingdistance	The distance the object should snap to
	 */
	public static void addSnapping(JToolBar toolbox, final JFrame frame, int snappingdistance) {
		// Fix to a terrible flaw in JToolbar
		toolbox.addHierarchyListener(new HierarchyListener() {			
			public void hierarchyChanged(HierarchyEvent e) {
				JToolBar bar = (JToolBar) e.getComponent();
				final Window topLevel = SwingUtilities.windowForComponent(bar);
				if (topLevel instanceof JDialog) {
					((JDialog) topLevel).addComponentListener(new SnappingPrototype(frame));
				}    
			}
		});
	}



	public static void preventToolBoxhorizontalOrientation(final JToolBar toolbox) {
		toolbox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String propName = evt.getPropertyName();
				if ("orientation".equals(propName)) {
					Integer oldValue = (Integer) evt.getOldValue();
					Integer newValue = (Integer) evt.getNewValue();
					if (newValue.intValue() == JToolBar.HORIZONTAL) {
						toolbox.setOrientation(SwingConstants.VERTICAL);
					}	
				}
				}});
	}




}
