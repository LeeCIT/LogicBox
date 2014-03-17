


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
	private ToolContextual  toolContextual;
	private ToolHighlighter toolHighlighter;
	private ToolPlacer      toolPlacer;
	private ToolTraceDrawer toolTraceDrawer;
	
	private List<EditorComponent> selection;
	
	private List<Tool> tools;
	private Tool       activeTool;
	
	
	
	public ToolManager( EditorPanel panel, EditorWorld world, Camera cam ) {
		selection = new ArrayList<>();
		tools     = new ArrayList<>();
		
		setupTools ( panel, world, cam );
		setActiveTool( toolContextual );
	}
	
	
	
	public void setActiveTool( Tool tool ) {
		for (Tool t: tools)
			t.detach();
		
		activeTool = tool;
		activeTool.attach();
	}
	
	
	
	private void setupTools( EditorPanel panel, EditorWorld world, Camera cam ) {
		toolContextual  = add( new ToolContextual (panel, world, cam, this) );
		toolHighlighter = add( new ToolHighlighter(panel, world, cam, this) );
		toolPlacer      = add( new ToolPlacer     (panel, world, cam, this) );
		toolTraceDrawer = add( new ToolTraceDrawer(panel, world, cam, this) );
	}
	
	
	
	private <T extends Tool> T add( T tool ) {
		tools.add( tool );
		return tool;
	}
	
	
	
	public ToolContextual  getContextual()  { return toolContextual;  }
	public ToolHighlighter getHighlighter() { return toolHighlighter; }
	public ToolPlacer      getPlacer()      { return toolPlacer;      }
	public ToolTraceDrawer getTraceDrawer() { return toolTraceDrawer; }
	
	
	
	public List<Tool> getTools() {
		return tools;
	}
	
	
	
	protected boolean hasSelection() {
		return ! selection.isEmpty();
	}
	
	
	protected List<EditorComponent> getSelection() {
		return selection;
	}
}























