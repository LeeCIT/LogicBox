


package logicBox.gui.editor.tools;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorComponentActive;
import logicBox.gui.editor.EditorController;
import logicBox.gui.editor.EditorCreationCommand;
import logicBox.gui.editor.EditorCreationParam;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.GraphicComActive;
import logicBox.sim.component.ComponentActive;
import logicBox.util.Callback;
import logicBox.util.CallbackParam;



/**
 * Manages tools which interact with the EditorPanel.
 * @author Lee Coakley
 */
public class ToolManager
{
	private EditorController controller;
	private ToolContextual   toolContextual;
	private ToolHighlighter  toolHighlighter;
	private ToolPlacer       toolPlacer;
	private ToolTraceDrawer  toolTraceDrawer;
	
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
	
	
	
	public void initiateComponentCreation( final EditorCreationCommand ecc ) {
		takeExclusiveControl( toolPlacer );
		
		toolPlacer.placementStart( ecc.getGraphicPreview(), new CallbackParam<EditorCreationParam>() {
			public void execute( EditorCreationParam param ) {
				ComponentActive  scom = ecc.getComponentPayload();
				GraphicComActive gca  = scom.getGraphic();
				EditorComponent  ecom = new EditorComponentActive( scom, gca, param.pos, param.angle );
				controller.getWorld().add( ecom );
			}
		});
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
	
	
	
	public void initiateTraceCreation() {
		takeExclusiveControl( toolTraceDrawer );
	}
	
	
	
	public void delete() {
		if ( ! toolContextual.isAttached())
			releaseControl();
		
		toolHighlighter.reset(); // Ensure no residual drawing is done
		toolContextual.delete();
	}
	
	
	
	public void selectAll() {
		releaseControl();
		toolContextual.selectAll();
	}
	
	
	
	public void selectNone() {
		releaseControl();
		toolContextual.selectNone();
	}
	
	
	
	public void selectInvert() {
		releaseControl();
		toolContextual.selectInvert();
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
		toolContextual  = add( new ToolContextual (this) );
		toolHighlighter = add( new ToolHighlighter(this) );
		toolPlacer      = add( new ToolPlacer     (this) );
		toolTraceDrawer = add( new ToolTraceDrawer(this) );
		
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























