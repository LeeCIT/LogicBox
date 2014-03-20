


package logicBox.gui.editor;

import java.awt.Color;
import java.io.Serializable;



/**
 * All graphics which can be drawn in the EditorPanel derive from this class.
 * @author Lee Coakley
 */
public abstract class Graphic implements Serializable, Drawable
{
	private static final long serialVersionUID = 1L;
	
	protected Color   colStroke;
	protected Color   colFill;
	private   boolean isSelected;
	private   boolean isHighlighted;
	private   boolean isInverted;
	
	
	
	public Graphic() {
		this.colStroke = EditorStyle.colComponentStroke;
		this.colFill   = EditorStyle.colComponentFill;
	}
	
	
	
	public void setHighlighted( boolean state ) {
		isHighlighted = state;
		updateColours();
	}
	
	
	
	public boolean isHighlighted() {
		return isHighlighted;
	}
	
	
	
	public void setSelected( boolean state ) {
		isSelected = state;
		updateColours();
	}
	
	
	
	public boolean isSelected() {
		return isSelected;
	}
	
	
	
	public void setInverted( boolean state ) {	
		isInverted = state;
	}
	
	
	
	public boolean isInverted() {
		return isInverted;
	}
	
	
	
	private void updateColours() {
		if (isHighlighted) {
			if (isSelected) {
				colStroke = EditorStyle.colHighlightSelectStroke;
				colFill   = EditorStyle.colHighlightSelectFill;
			} else {
				colStroke = EditorStyle.colHighlightStroke;
				colFill   = EditorStyle.colHighlightFill;
			}
		} else {
			if (isSelected) {
				colStroke = EditorStyle.colSelectionStroke;
				colFill   = EditorStyle.colSelectionFill;
			} else {
				colStroke = EditorStyle.colComponentStroke;
				colFill   = EditorStyle.colComponentFill;
			}
		}
	}
}
