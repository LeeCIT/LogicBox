


package logicBox.gui.editor;

import java.awt.Color;
import java.io.Serializable;



/**
 * All graphics which can be drawn in the EditorPanel derive from this class.
 * @author Lee Coakley
 */
public abstract class Graphic implements Serializable, Drawable, GraphicIntersector
{
	private static final long serialVersionUID = 1L;
	
	protected Color colStrokeNormal;
	protected Color colStrokeHighlight;
	protected Color colStrokeSelect;
	protected Color colStrokeSelectHighlight;
	
	protected Color colFillNormal;
	protected Color colFillHighlight;
	protected Color colFillSelect;
	protected Color colFillSelectHighlight;
	
	protected Color   colStroke;
	protected Color   colFill;
	private   boolean isSelected;
	private   boolean isHighlighted;
	private   boolean isInverted;
	
	
	
	public Graphic() {
		this.colStrokeNormal          = EditorStyle.colComponentStroke;
		this.colStrokeHighlight       = EditorStyle.colHighlightStroke;
		this.colStrokeSelect          = EditorStyle.colSelectionStroke;
		this.colStrokeSelectHighlight = EditorStyle.colHighlightSelectStroke;
		
		this.colFillNormal            = EditorStyle.colComponentFill;
		this.colFillHighlight         = EditorStyle.colHighlightFill;
		this.colFillSelect            = EditorStyle.colSelectionFill;
		this.colFillSelectHighlight   = EditorStyle.colHighlightSelectFill;
		
		this.colStroke = colStrokeNormal;
		this.colFill   = colFillNormal;
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
				colStroke = colStrokeSelectHighlight;
				colFill   = colFillSelectHighlight;
			} else {
				colStroke = colStrokeHighlight;
				colFill   = colFillHighlight;
			}
		} else {
			if (isSelected) {
				colStroke = colStrokeSelect;
				colFill   = colFillSelect;
			} else {
				colStroke = colStrokeNormal;
				colFill   = colFillNormal;
			}
		}
	}
}
