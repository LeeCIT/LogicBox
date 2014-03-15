


package logicBox.gui.editor.tools;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorWorld;



/**
 * Manages tools which interact with the EditorPanel.
 * @author Lee Coakley
 */
public class ToolManager
{
	private ToolDragger     toolDragger;
	private ToolHighlighter toolHighlighter;
	private ToolPlacer      toolPlacer;
	private ToolSelector    toolSelector;
	private ToolTraceDrawer toolTraceDrawer;
	
	private List<Tool>            tools;
	private List<EditorComponent> selection;
	
	
	
	public ToolManager( EditorPanel panel, EditorWorld world, Camera cam ) {
		selection = new ArrayList<>();
		tools     = new ArrayList<>();
		
		toolDragger = add( new ToolDragger(panel, world, cam, this) );
	}
	
	
	
	public ToolDragger getDragger() {
		return toolDragger;
	}
	
	
	
	public ToolHighlighter getHighlighter() {
		return toolHighlighter;
	}
	
	
	
	public ToolPlacer getPlacer() {
		return toolPlacer;
	}
	
	
	
	public ToolSelector getSelector() {
		return toolSelector;
	}
	
	
	
	public ToolTraceDrawer getTraceDrawer() {
		return toolTraceDrawer;
	}
	
	
	
	public List<Tool> getTools() {
		return tools;
	}
	
	
	
	protected List<EditorComponent> getSelection() {
		return selection;
	}
	
	
	
	private <T extends Tool> T add( T tool ) {
		tools.add( tool );
		return tool;
	}
}
