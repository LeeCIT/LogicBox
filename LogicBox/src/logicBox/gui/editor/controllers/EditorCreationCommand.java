


package logicBox.gui.editor.controllers;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.sim.component.ComponentActive;



/**
 * Specifies a creation command for the editor panel.
 * The preview graphic is used during placement of the component.
 * @author Lee Coakley
 */
public class EditorCreationCommand
{
	private ComponentActive  componentPayload;
	private GraphicComActive graphicPreview;
	
	
	
	public EditorCreationCommand( ComponentActive componentPayload, GraphicComActive graphicPreview ) {
		this.componentPayload = componentPayload;
		this.graphicPreview   = graphicPreview;
	}
	
	
	
	public ComponentActive getComponentPayload() {
		return componentPayload;
	}
	
	
	
	public GraphicComActive getGraphicPreview() {
		return graphicPreview;
	}
}
