


package logicBox.gui.editor.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.EditorComponentTrace;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.EditorWorld.FindClosestTraceResult;
import logicBox.gui.editor.EditorComponentJunction;
import logicBox.gui.editor.Graphic;
import logicBox.gui.editor.GraphicJunction;
import logicBox.gui.editor.GraphicTrace;
import logicBox.gui.editor.RepaintListener;
import logicBox.sim.component.Junction;
import logicBox.sim.component.Pin;
import logicBox.sim.component.Trace;
import logicBox.util.Line2;
import logicBox.util.Vec2;



/**
 * Places junctions onto traces.
 * @author Lee Coakley
 */
public class ToolJunctionPlacer extends Tool
{
	private MouseAdapter    mouseListener;
	private RepaintListener repaintListener;
	
	private boolean placementInitiated;
	private boolean placementArmed;
	private Vec2    placementPos;
	
	
	
	public ToolJunctionPlacer( ToolManager manager ) {
		super( manager );
		this.mouseListener   = createEventListener();
		this.repaintListener = createRepaintListener();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		getEditorPanel().addMouseListener      ( mouseListener );
		getEditorPanel().addMouseMotionListener( mouseListener );
		getEditorPanel().addWorldRepaintListener( repaintListener );
		setTransHint();
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		getEditorPanel().removeMouseListener      ( mouseListener );
		getEditorPanel().removeMouseMotionListener( mouseListener );
		getEditorPanel().removeWorldRepaintListener( repaintListener );
		removeTransHint();
		setAttached( false );
	}
	
	
	
	public void reset() {
		placementInitiated = false;
		placementArmed     = false;
		placementPos       = null;
	}
	
	
	
	private void setTransHint() {
		setTransHint(
		    "Left-click on a trace to insert a junction.\n" +
		    "Right-click to cancel."
		);
	}
	
	
	
	private MouseAdapter createEventListener() {
		return new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (placementInitiated)
				if (isLeft( ev ))
					placementArm();
			}
			
			
			public void mouseReleased( MouseEvent ev ) {
				if ( ! placementInitiated)
					return;
				
				if (placementArmed)
				if (isLeft( ev ))
					placementComplete();
				
				if (isRight( ev ))
					placementCancel();
			}
			
			
			public void mouseMoved( MouseEvent ev ) {
				if ( ! placementInitiated)
					return;
				
				placementMove();
			}
			
			
			public void mouseDragged( MouseEvent ev ) {
				mouseMoved( ev ); // Same thing
			}
		};
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (placementInitiated)
					drawPlacementIndicator( g );
			}
		};
	}
	
	
	
	private void drawPlacementIndicator( Graphics2D g ) {
		EditorWorld.FindClosestTraceResult result = findClosestTrace();
		
		if (result.foundTrace) {			
			Graphic graphic = result.ecom.getGraphic();
			graphic.setHighlighted( true );
			graphic.draw( g );
			graphic.setHighlighted( false );
			
			drawJunction( g, result.closestPos );
		} else {
			drawJunction( g, getMousePosWorld() );
		}
	}
	
	
	
	private void drawJunction( Graphics2D g, Vec2 pos ) {
		Graphic junc = new GraphicJunction( pos );
		
		if (placementArmed)
			junc.setHighlighted( true );
		
		junc.draw( g );
	}
	
	
	
	/**
	 * Begin placing a junction.
	 */
	public void placementStart() {
		placementInitiated = true;
		placementArmed     = false;
		repaint();
	}
	
	
	
	private void placementArm() {
		placementArmed = true;
		repaint();
	}
	
	
	
	private void placementComplete() {
		placementPos   = getSnappedMousePos();
		placementArmed = false;
		
		EditorWorld.FindClosestTraceResult result = findClosestTrace();
		
		if (result.foundTrace)
			 createJunctionOnTrace( result );
		else createJunction( placementPos );
		
		markHistoryChange( "Create junction" );
		repaint();
	}



	private void placementMove() {
		placementPos = getSnappedMousePos();
		repaint();
	}
	
	
	
	private void placementCancel() {
		placementInitiated = false;
		placementArmed     = false;
		placementPos       = null;
		
		getToolManager().releaseControl();
		repaint();
	}
	
	
	
	private Vec2 getSnappedMousePos() {
		return getMousePosWorld();
	}
	
	
	
	private FindClosestTraceResult findClosestTrace() {
		return getWorld().findClosestTrace( getMousePosWorld(), 16 );
	}
	
	
	
	private void createJunction( Vec2 pos ) {
		Junction                junc   = new Junction();
		EditorComponentJunction edJunc = new EditorComponentJunction( junc, pos.copy() );
		
		getWorld().add( edJunc );
	}
	
	
	
	private void createJunctionOnTrace( EditorWorld.FindClosestTraceResult result ) {
		Vec2 pos = result.closestPos;
		
		EditorComponentTrace oldEcom = result.ecom;
		Trace                trace   = oldEcom.getComponent();
		GraphicTrace         graphic = oldEcom.getGraphic(); 
		
		Pin pinSrc = trace.getPinSource();
		Pin pinDst = trace.getPinDest();
		
		Junction junc      = new Junction();
		Trace    srcToJunc = new Trace( pinSrc,           junc.createPin() );
		Trace    juncToDst = new Trace( junc.createPin(), pinDst           );
		
		
		// Graphic
		int         index    = result.lineIndex;
		List<Line2> lines    = graphic.getLines();
		List<Vec2>  pointSrc = new ArrayList<>();
		List<Vec2>  pointDst = new ArrayList<>();
		
		pointDst.add( pos.copy() );
		for (int i=0;     i<=index;       i++) pointSrc.add( lines.get(i).a );
		for (int i=index; i<lines.size(); i++) pointDst.add( lines.get(i).b );
		pointSrc.add( pos.copy() );
		
		
		// Ecoms
		EditorComponentTrace    edTraceSrc = new EditorComponentTrace( srcToJunc, pointSrc );
		EditorComponentTrace    edTraceDst = new EditorComponentTrace( juncToDst, pointDst );
		EditorComponentJunction edJunc     = new EditorComponentJunction( junc, pos.copy() );			
		
		EditorWorld world = getWorld();
		world.remove( oldEcom );
		world.add( edTraceSrc, edTraceDst, edJunc );
	}
}

























