


package logicBox.gui.editor.tools;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import logicBox.gui.Gfx;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorStyle;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.Graphic;
import logicBox.gui.editor.RepaintListener;
import logicBox.util.Bbox2;
import logicBox.util.Callback;
import logicBox.util.CallbackRepeater;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Handles dragging, rotating and selection.
 * @author Lee Coakley
 * TODO there are still some bugs.
 * TODO add support for context menus on components
 */
public class ToolContextual extends Tool
{
	private boolean dragHasLock;
	private boolean selectHasLock;
	
	private boolean dragInitiated;
	private boolean dragging;
	private Vec2    dragInitiatedAt;
	private Vec2    dragOffset;
	private double  rotateStartAngle;
	
	private Selection selection;
	private boolean   selectInitiated;
	private boolean   selecting;
	private Vec2      selectInitiatedAt;
	private Vec2      selectPosNow;
	
	private MouseAdapter    dragListener;
	private MouseAdapter    selectListener;
	private RepaintListener repaintListener;
	
	
	
	public ToolContextual( final EditorPanel panel, EditorWorld world, Camera cam, ToolManager manager ) {
		super( panel, world, cam, manager );
		this.selection       = new Selection();
		this.dragListener    = createDragListener();
		this.selectListener  = createSelectListener();
		this.repaintListener = createSelectRepaintListener();
		
		// TODO DEBUG ONLY
		new CallbackRepeater( 33, new Callback() {
			public void execute() {
				panel.repaint();
			}
		});
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		panel.addMouseListener      ( dragListener );
		panel.addMouseMotionListener( dragListener );
		panel.addMouseListener      ( selectListener );
		panel.addMouseMotionListener( selectListener );
		panel.addRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		panel.removeMouseListener      ( dragListener );
		panel.removeMouseMotionListener( dragListener );
		panel.removeMouseListener      ( selectListener );
		panel.removeMouseMotionListener( selectListener );
		panel.removeRepaintListener( repaintListener );
		setAttached( false );
	}
	
	
	
	public Selection getSelection() {
		return selection;
	}
	
	
	
	public boolean hasSelection() {
		return ! getSelection().isEmpty();
	}
	
	
	
	
	
	//////////////////////////////////////////////////
	// Dragging
	////////////////////////////////////////////////
	
	private MouseAdapter createDragListener() {
		return new MouseAdapter() {
			public void mousePressed ( MouseEvent ev ) { onDragPress  ( ev ); }
			public void mouseReleased( MouseEvent ev ) { onDragRelease( ev ); }
			public void mouseDragged ( MouseEvent ev ) { onDragDrag   ( ev ); }
		};
	}
	
	
	
	private void onDragPress( MouseEvent ev ) {
		if (selectHasLock)
			return;
		
		if ( ! ev.isShiftDown() && ! ev.isControlDown()) {
			if (isLeft (ev)) dragInitiate();
			if (isRight(ev)) dragCancel();
		}
	}
	
	
	
	private void onDragRelease( MouseEvent ev ) {
		if (selectHasLock)
			return;
		
		if (isLeft(ev))
			dragComplete();
	}
	
	
	
	private void onDragDrag( MouseEvent ev ) {
		if (selectHasLock)
			return;
		
		if ( ! ev.isShiftDown())
			 dragMove  ();
		else rotateMove();
	}
	
	
	
	private void dragInitiate() {
		Vec2 pos = cam.getMousePosWorld();
		
		if (selectHasLock || ! isComponentAt(pos)) // Select will take control
			return;
		
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		
		dragInitiated   = true;
		dragInitiatedAt = pos;
	}
	
	
	
	private void dragMove() {
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		Vec2 pos = cam.getMousePosWorld();
		
		if (dragInitiated) {
			if ( ! dragging) 
				if (isDragThresholdMet(dragInitiatedAt,pos))
					enterDraggingState();
		}
		
		if (dragging) {
			panel.setCursor( new Cursor(Cursor.MOVE_CURSOR) );
			selection.setPos( pos.add(dragOffset) );
			panel.repaint();
		}
	}
	
	
	
	private void enterDraggingState() {
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		dragHasLock   = true;
		dragging      = true;
		dragInitiated = false;
		
		EditorComponent ecom = getComponentAt( dragInitiatedAt );
		
		if (selection.isEmpty())
			selection.add( ecom );
		
		if ( ! selection.contains( ecom )) {
			selection.clear();
			selection.set( ecom );
		}
		
		dragOffset       = selection.getPos().subtract( cam.getMousePosWorld() );
		rotateStartAngle = selection.getAngle();
		panel.repaint();
	}
	
	
	
	private void rotateMove() {
		if ( ! (dragInitiated || dragging))
			return;
		
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		
		panel.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		
		Vec2   pos     = cam.getMousePosWorld();
		double angle   = Geo.angleBetween( selection.getPos(), pos );
		double snapped = Geo.roundToMultiple( angle, 45 );
		selection.setAngle( snapped );
		panel.repaint();
	}
	
	
	
	private void dragComplete() {
		if ( ! dragging)
			return;
		
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		dragFinishedCommon();
	}
	
	
	
	private void dragCancel() {
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		boolean wasDragging = dragging;
		
		dragFinishedCommon();
		
		if (wasDragging) {		
			selection.setPos  ( dragInitiatedAt.add(dragOffset) );
			selection.setAngle( rotateStartAngle );
		}
	}
	
	
	
	private void dragFinishedCommon() {
		dragHasLock   = false;
		dragInitiated = false;
		dragging      = false;
		panel.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		panel.repaint();
	}
	
	
	
	
	
	
	
	//////////////////////////////////////////////////
	// Selection
	////////////////////////////////////////////////
	
	private RepaintListener createSelectRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (selecting)
					drawSelectionFeedback( g );
			}
		};
	}
	
	
	
	private void drawSelectionFeedback( Graphics2D g ) {
		Bbox2                 bbox  = getSelectBbox();
		List<EditorComponent> ecoms = world.find( bbox );
		
		for (int i=0; i<ecoms.size(); i++) {
			Graphic graphic = ecoms.get(i).getGraphic();
			boolean selPrev = graphic.isSelected();
			
			graphic.setSelected( true );
			graphic.draw( g );
			graphic.setSelected( selPrev );
		}
		
		drawSelectionBbox( g );
	}
	
	
	
	private void drawSelectionBbox( Graphics2D g ) {
		Bbox2  bbox       = getSelectBbox();
		double zoom       = cam.getZoom();
		double modulation = Geo.boxStep( bbox.getSmallest(), 4, 32 ); 
		double modScaled  = Geo.lerp( 0.2, 1.0, modulation );
		float  thickness  = (float) ((EditorStyle.compThickness * modScaled) / zoom);
		double radius     = (int)   (2.0 / zoom);
		
		Gfx.pushStrokeAndSet( g, EditorStyle.makeSelectionStroke(thickness) );
			Gfx.pushColorAndSet( g, EditorStyle.colSelectionStroke );
			
				Gfx.pushColorAndSet( g, EditorStyle.colSelectionFill );
					Gfx.pushCompositeAndSet( g, 0.25 );
						Gfx.drawBbox( g, bbox, true );
					Gfx.popComposite( g );
				Gfx.popColor( g );
					
				Gfx.drawBboxRounded( g, bbox, radius, false );
			Gfx.popColor ( g );
		Gfx.popStroke( g );
	}
	
	
	
	private MouseAdapter createSelectListener() {
		return new MouseAdapter() {
			public void mousePressed ( MouseEvent ev ) { onSelectPress  ( ev ); }
			public void mouseReleased( MouseEvent ev ) { onSelectRelease( ev ); }
			public void mouseClicked ( MouseEvent ev ) { onSelectClick  ( ev ); }
			public void mouseDragged ( MouseEvent ev ) { onSelectDrag   ( ev ); }
		};
	}
	
	
	
	private void onSelectPress( MouseEvent ev ) {
		if (dragHasLock)
			return;
		
		if (isLeft(ev))
			selectInitiate( ev.isShiftDown() || ev.isControlDown() );
			
		if (isRight(ev))
			selectCancel();
	}
	
	
	
	private void onSelectRelease( MouseEvent ev ) {
		if (dragHasLock)
			return;
		
		if (isLeft(ev))
			selectComplete( ev.isShiftDown(), ev.isControlDown() );
	}
	
	
	
	private void onSelectClick( MouseEvent ev ) {
		if (dragging || selecting)
			return;
		
		if (isLeft(ev)) {
			boolean shift = ev.isShiftDown();
			boolean ctrl  = ev.isControlDown();
			selectAddRemove( shift, ctrl );
		}
	}
	
	
	
	private void onSelectDrag( MouseEvent ev ) {
		if (dragHasLock)
			return;
		
		selectMove();
	}
	
	
	
	private Bbox2 getSelectBbox() {
		return Bbox2.createFromPoints( selectInitiatedAt, selectPosNow );
	}
	
	
	
	private void selectInitiate( boolean modifying ) {
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		Vec2    pos   = cam.getMousePosWorld();
		boolean hover = isComponentAt( pos );
		
		if (hover)
			return; // Allow shift/alt click selection mods to take effect
		
		if ( ! hover && ! modifying) {
			selection.clear();
			panel.repaint();
		}
		
		selectHasLock     = true;
		selectInitiated   = true;
		selectInitiatedAt = pos;
		selectPosNow      = pos;
	}
	
	
	
	private void selectMove() {
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		Vec2 pos = cam.getMousePosWorld();
		
		if (selectInitiated) {
			if ( ! selecting) 
				if (isDragThresholdMet(selectInitiatedAt,pos)) {
					selecting       = true;
					selectInitiated = false;
				}
		}
		
		if (selecting) {
			selectPosNow = pos;
			panel.repaint();
		}
	}
	
	
	
	private void selectComplete( boolean isAdditive, boolean isSubtractive ) {
		if ( ! selectHasLock)
			return;
		
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		modifySelection( isAdditive, isSubtractive );
		selectFinishedCommon();
	}
	
	
	
	private void modifySelection( boolean isAdditive, boolean isSubtractive ) {
		if (isAdditive && isSubtractive)
			return;
		
		Bbox2 bbox = getSelectBbox();
		
		if (bbox.getSmallest() > 0) { // There would be no visual feedback
			List<EditorComponent> sel = world.find( bbox );
			
			if (!isAdditive && !isSubtractive) selection.set      ( sel );
			else if (isAdditive)               selection.addAll   ( sel );
			else if (isSubtractive)            selection.removeAll( sel );
		}
	}



	private void selectCancel() {
		System.out.println( new Object(){}.getClass().getEnclosingMethod().getName() );
		selectFinishedCommon();
	}
	
	
	
	private void selectFinishedCommon() {
		selectHasLock   = false;
		selectInitiated = false;
		selecting       = false;
		panel.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		panel.repaint();
	}
    
    
    
    private void selectAddRemove( boolean adding, boolean toggling ) {
    	if (selecting)
    		return;
    	
		EditorComponent ecom = getComponentAt( cam.getMousePosWorld() );
		
		if (ecom == null)
			return;
		
		if (adding && toggling)
			return;
		
		if (adding)
			 selection.add( ecom );
		
		if (toggling) {
			if (selection.contains( ecom ))
				 selection.remove( ecom );
			else selection.add   ( ecom );
		}
		
		panel.repaint();
	}
}



















