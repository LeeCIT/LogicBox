


package logicBox.gui.editor.tools;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorController;
import logicBox.gui.editor.EditorCreationCommand;
import logicBox.gui.editor.EditorCreationParam;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.GraphicComActive;
import logicBox.sim.component.ComponentActive;
import logicBox.util.Callback;
import logicBox.util.CallbackParam;
import logicBox.util.Util;



/**
 * Manages tools which interact with the EditorPanel.
 * @author Lee Coakley
 */
public class ToolManager
{
	private EditorController   controller;
	private ToolContextual     toolContextual;
	private ToolHighlighter    toolHighlighter;
	private ToolJunctionPlacer toolJunctionPlacer;
	private ToolPlacer         toolPlacer;
	private ToolTraceDrawer    toolTraceDrawer;
	
	private List<Tool> tools;
	
	
	
	public ToolManager( EditorController ctrl ) {
		this.controller  = ctrl;
		this.tools       = new ArrayList<>();
		
		setupTools( getEditorPanel(), controller.getWorld(), controller.getCamera() );
		releaseControl();
	}
	
	
	
	public EditorPanel getEditorPanel() {
		return controller.getEditorPanel();
	}
	
	
	
	public void powerOn() {
		controller.powerOn();
	}
	
	
	
	public void powerReset() {
		controller.powerReset();
	}
	
	
	
	public void powerOff() {
		controller.powerOff();
	}
	
	
	
	public boolean cut() {
		releaseControlIfNotContextual();
		toolHighlighter.reset(); // Ensure no residual drawing is done
		return toolContextual.cut();
	}
	
	
	
	public boolean copy() {
		releaseControlIfNotContextual();
		return toolContextual.copy();
	}
	
	
	
	public void paste() {
		releaseControlIfNotContextual();
		toolContextual.paste();
		getEditorPanel().repaint();
	}
	
	
	
	public void delete() {
		releaseControlIfNotContextual();
		toolHighlighter.reset(); // Ensure no residual drawing is done
		toolContextual.delete();
	}
	
	
	
	public void selectAll() {
		releaseControlIfNotContextual();
		toolContextual.selectAll();
	}
	
	
	
	public void selectNone() {
		releaseControlIfNotContextual();
		toolContextual.selectNone();
	}
	
	
	
	public void selectInvert() {
		releaseControlIfNotContextual();
		toolContextual.selectInvert();
	}
	
	
	
	private void releaseControlIfNotContextual() {
		if ( ! toolContextual.isAttached())
			releaseControl();
	}



	public void initiateComponentCreation( final EditorCreationCommand ecc ) {
		takeExclusiveControl( toolPlacer );
		
		toolPlacer.placementStart( ecc.getGraphicPreview(), new CallbackParam<EditorCreationParam>() {
			public void execute( EditorCreationParam param ) {
				ComponentActive  scom = Util.deepCopy( ecc.getComponentPayload() );
				GraphicComActive gca  = Util.deepCopy( scom.getGraphic() );
				
				controller.getWorld().add( EditorComponent.create(scom, gca, param) );
			}
		});
	}
	
	
	
	public void initiateTraceCreation() {
		takeExclusiveControl( toolTraceDrawer );
	}
	
	
	
	public void initiateJunctionCreation() {
		takeExclusiveControl( toolJunctionPlacer );
		toolJunctionPlacer.placementStart();
	}
	
	
	
	protected void takeExclusiveControl( Tool tool ) {
		detachAndResetAll();
		tool.attach();
		
		getEditorPanel().repaint();
	}
	
	
	
	protected void takeCooperativeControl( Tool a, Tool b ) {
		detachAndResetAll();
		a.attach();
		b.attach();
		
		getEditorPanel().repaint();
	}
	
	
	
	protected void releaseControl() {
		takeCooperativeControl( toolContextual, toolHighlighter );
	}
	
	
	
	protected EditorController getEditorController() {
		return controller;
	}
	
	
	
	private void detachAndResetAll() {
		for (Tool t: tools) {
			t.detach();
			t.reset();
		}
	} 
	
	
	
	private void setupTools( EditorPanel panel, EditorWorld world, Camera cam ) {
		toolContextual     = add( new ToolContextual    ( this ) );
		toolHighlighter    = add( new ToolHighlighter   ( this ) );
		toolJunctionPlacer = add( new ToolJunctionPlacer( this ) );
		toolPlacer         = add( new ToolPlacer        ( this ) );
		toolTraceDrawer    = add( new ToolTraceDrawer   ( this ) );
		
		addUndoDeselectCallback();
	}
	
	
	
	private void addUndoDeselectCallback() {
		controller.getHistoryManager().addOnUndoRedoCallback( new Callback() {
			public void execute() {
				toolContextual.selectNone();
			}
		});
	}



	private <T extends Tool> T add( T tool ) {
		tools.add( tool );
		return tool;
	}
}























