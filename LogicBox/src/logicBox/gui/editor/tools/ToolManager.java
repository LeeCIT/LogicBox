


package logicBox.gui.editor.tools;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorCreationCommand;
import logicBox.gui.editor.EditorCreationParam;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.GraphicComActive;
import logicBox.sim.component.ComponentActive;
import logicBox.util.CallbackParam;



/**
 * Manages tools which interact with the EditorPanel.
 * @author Lee Coakley
 */
public class ToolManager
{
	private EditorPanel     panel;
	private ToolContextual  toolContextual;
	private ToolHighlighter toolHighlighter;
	private ToolPlacer      toolPlacer;
	private ToolTraceDrawer toolTraceDrawer;
	
	private List<Tool> tools;
	
	
	
	public ToolManager( EditorPanel panel ) {
		this.panel = panel;
		this.tools = new ArrayList<>();
		
		setupTools( panel, panel.getWorld(), panel.getCamera() );
		releaseControl();
	}
	
	
	
	public ToolContextual  getContextual()  { return toolContextual;  }
	public ToolHighlighter getHighlighter() { return toolHighlighter; }
	public ToolPlacer      getPlacer()      { return toolPlacer;      }
	public ToolTraceDrawer getTraceDrawer() { return toolTraceDrawer; }
	
	
	
	public EditorPanel getEditorPanel() {
		return panel;
	}
	
	
	
	public void takeExclusiveControl( Tool tool ) {
		detachAndResetAll();
		tool.attach();
	}
	
	
	
	public void takeCooperativeControl( Tool a, Tool b ) {
		detachAndResetAll();
		a.attach();
		b.attach();
	}
	
	
	
	public void releaseControl() {
		takeCooperativeControl( toolContextual, toolHighlighter );
	}
	
	
	
	public void initiateComponentCreation( final EditorCreationCommand ecc ) {
		takeExclusiveControl( toolPlacer );
		toolPlacer.placementStart( ecc.getGraphicPreview(), new CallbackParam<EditorCreationParam>() {
			public void execute( EditorCreationParam param ) {
				ComponentActive  scom = ecc.getComponentPayload();
				GraphicComActive gca  = scom.getGraphic();
				EditorComponent  ecom = new EditorComponent( scom, gca, param.pos, param.angle );
				panel.getWorld().add( ecom );
			}
		});
	}
	
	
	
	private void detachAndResetAll() {
		for (Tool t: tools) {
			t.detach();
			t.reset();
		}
	}
	
	
	
	private void setupTools( EditorPanel panel, EditorWorld world, Camera cam ) {
		toolContextual  = add( new ToolContextual (this) );
		toolHighlighter = add( new ToolHighlighter(this) );
		toolPlacer      = add( new ToolPlacer     (this) );
		toolTraceDrawer = add( new ToolTraceDrawer(this) );
	}
	
	
	
	private <T extends Tool> T add( T tool ) {
		tools.add( tool );
		return tool;
	}
}























