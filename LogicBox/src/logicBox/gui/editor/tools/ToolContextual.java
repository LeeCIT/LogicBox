


package logicBox.gui.editor.tools;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;
import logicBox.gui.Gfx;
import logicBox.gui.contextMenu.ContextMenu;
import logicBox.gui.contextMenu.ContextMenuItem;
import logicBox.gui.contextMenu.ContextMenuString;
import logicBox.gui.editor.Clipboard;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorStyle;
import logicBox.gui.editor.Graphic;
import logicBox.gui.editor.RepaintListener;
import logicBox.util.Bbox2;
import logicBox.util.Geo;
import logicBox.util.Util;
import logicBox.util.Vec2;



/**
 * Handles dragging, rotating and selection.
 * @author Lee Coakley
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
	
	
	
	public ToolContextual( ToolManager manager ) {
		super( manager );
		this.selection       = new Selection();
		this.dragListener    = createDragListener();
		this.selectListener  = createSelectListener();
		this.repaintListener = createSelectRepaintListener();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		getEditorPanel().addMouseListener      ( dragListener );
		getEditorPanel().addMouseMotionListener( dragListener );
		getEditorPanel().addMouseListener      ( selectListener );
		getEditorPanel().addMouseMotionListener( selectListener );
		getEditorPanel().addWorldRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		getEditorPanel().removeMouseListener      ( dragListener );
		getEditorPanel().removeMouseMotionListener( dragListener );
		getEditorPanel().removeMouseListener      ( selectListener );
		getEditorPanel().removeMouseMotionListener( selectListener );
		getEditorPanel().removeWorldRepaintListener( repaintListener );
		setAttached( false );
	}
	
	
	
	public void reset() {
		dragHasLock     = false;
		selectHasLock   = false;
		dragInitiated   = false;
		dragging        = false;
		selectInitiated = false;
		selecting       = false;
		selection.clear();
	}
	
	
	
	public boolean cut() {	
		if ( ! selection.isEmpty()) {
			Clipboard.set( selection );
			getWorld().delete( selection );
			selection.clear();
			markHistoryChange( "Cut" );
			repaint();
			return true;
		} else {
			Clipboard.clear();
			return false;
		}
	}
	
	
	
	public boolean copy() {
		if (hasSelection()) {
			Clipboard.set( selection );
			return true;
		} else {
			Clipboard.clear();
			return false;
		}
	}
	
	
	
	public void paste() {
		if (Clipboard.isEmpty())
			return;
		
		Selection sel = Clipboard.get();
		sel.setPos( getMousePosWorld() );
		getWorld().paste( sel );
		
		this.selection.clear();
		this.selection = sel;
		
		for (EditorComponent ecom: sel)
			ecom.getGraphic().setSelected( true );
		
		markHistoryChange( "Paste" );
		repaint();
	}
	
	
	
	public void delete() {
		if (selection.isEmpty())
			selectUnderlying();
		
		if ( ! selection.isEmpty()) {
			getWorld().delete( selection );
			selection.clear();
			markHistoryChange( "Delete" );
			repaint();
		}
	}
	
	
	
	public Selection getSelection() {
		return selection;
	}
	
	
	
	public boolean hasSelection() {
		return ! getSelection().isEmpty();
	}
	
	
	
	public void selectAll() {
		selection.addAll( getWorld().getComponents() );
		repaint();
	}
	
	
	
	public void selectNone() {
		selection.clear();
		repaint();
	}
	
	
	
	public void selectInvert() { // TODO doesn't work
		Set<EditorComponent> set = Util.createIdentityHashSet( getWorld().getComponents() );
		set.removeAll( selection.ecoms );
		selection.clear();
		selection.set( set );
		
		for (EditorComponent ecom: set)
			System.out.println( "Selected: " + ecom );
		
		repaint();
	}
	
	
	
	
	
	//////////////////////////////////////////////////
	// Dragging / Clicking
	////////////////////////////////////////////////
	
	private MouseAdapter createDragListener() {
		return new MouseAdapter() {
			public void mousePressed ( MouseEvent ev ) { onDragPress  ( ev ); }
			public void mouseReleased( MouseEvent ev ) { onDragRelease( ev ); }
			public void mouseDragged ( MouseEvent ev ) { onDragDrag   ( ev ); }
			public void mouseClicked ( MouseEvent ev ) { onDragClick  ( ev ); }
		};
	}
	
	
	
	private void onDragClick( MouseEvent ev ) {
		if (selectHasLock || dragHasLock)
			return;
		
		Vec2            pos  = getMousePosWorld();
		EditorComponent ecom = getComponentAt( pos );
		
		if (ecom != null) {
			if (isLeft (ev)) ecom.onMouseClick(); // TODO return whether state changed, or give callback that does this
			if (isRight(ev)) doContextMenu( ecom );
		}
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
		
		dragInitiated = false;
		
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
		Vec2            pos            = getMousePosWorld();
		EditorComponent ecom           = getComponentAt( pos );
		boolean         componentAtPos = ecom != null;
		
		if (selectHasLock || ! componentAtPos) // Select will take control
			return;
		
		if ( ! selection.contains( ecom ))
			selection.clear();
		
		dragInitiated   = true;
		dragInitiatedAt = pos;
	}
	
	
	
	private void dragMove() {
		Vec2 pos = getMousePosWorld();
		
		if (dragInitiated) {
			if ( ! dragging) 
				if (isDragThresholdMet(dragInitiatedAt,pos))
					enterDraggingState();
		}
		
		if (dragging) {
			setCursor( new Cursor(Cursor.MOVE_CURSOR) );
			selection.setPos( pos.add(dragOffset) );
			repaint();
		}
	}
	
	
	
	private void enterDraggingState() {
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
		
		dragOffset       = selection.getPos().subtract( dragInitiatedAt );
		rotateStartAngle = selection.getAngle();
		repaint();
	}
	
	
	
	private void rotateMove() {
		if ( ! (dragInitiated || dragging))
			return;
		
		resetCursor();
		
		Vec2   pos     = getMousePosWorld();
		double angle   = Geo.angleBetween( selection.getPos(), pos );
		double snapped = Geo.roundToMultiple( angle, 45 );
		selection.setAngle( snapped );
		repaint();
	}
	
	
	
	private void dragComplete() {
		if ( ! dragging)
			return;
		
		markHistoryChange( "Drag/Rotate" );
		dragFinishedCommon();
	}
	
	
	
	private void dragCancel() {
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
		resetCursor();
		repaint();
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
		List<EditorComponent> ecoms = getWorld().find( bbox );
		
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
		double zoom       = getCamera().getZoom();
		double zoomInv    = 1.0 / zoom;
		double modulation = Geo.boxStep( bbox.getSmallest(), 4*zoomInv, 32*zoomInv ); 
		double modScaled  = Geo.lerp( 0.2, 1.0, modulation );
		double modThick   = (1.0 / EditorStyle.compThickness) * 2; // When zoomed out, thin the box
		double modClamped = modScaled * Geo.clamp( zoom, modThick, 1 );
		float  thickness  = (float) ((EditorStyle.compThickness * modClamped) / zoom);
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
		Vec2    pos   = getMousePosWorld();
		boolean hover = isComponentAt( pos );
		
		if (hover)
			return; // Allow shift/alt click selection mods to take effect
		
		if ( ! hover && ! modifying) {
			selection.clear();
			repaint();
		}
		
		selectHasLock     = true;
		selectInitiated   = true;
		selectInitiatedAt = pos;
		selectPosNow      = pos;
	}
	
	
	
	private void selectMove() {
		Vec2 pos = getMousePosWorld();
		
		if (selectInitiated) {
			if ( ! selecting) 
				if (isDragThresholdMet(selectInitiatedAt,pos)) {
					selecting       = true;
					selectInitiated = false;
				}
		}
		
		if (selecting) {
			selectPosNow = pos;
			repaint();
		}
	}
	
	
	
	private void selectComplete( boolean isAdditive, boolean isSubtractive ) {
		if ( ! selectHasLock)
			return;
		
		modifySelection( isAdditive, isSubtractive );
		selectFinishedCommon();
	}
	
	
	
	private void modifySelection( boolean isAdditive, boolean isSubtractive ) {
		if (isAdditive && isSubtractive)
			return;
		
		Bbox2 bbox = getSelectBbox();
		
		if (bbox.getSmallest() > 0) { // There would be no visual feedback
			List<EditorComponent> sel = getWorld().find( bbox );
			
			if (!isAdditive && !isSubtractive) selection.set      ( sel );
			else if (isAdditive)               selection.addAll   ( sel );
			else if (isSubtractive)            selection.removeAll( sel );
		}
	}



	private void selectCancel() {
		selectFinishedCommon();
	}
	
	
	
	private void selectFinishedCommon() {
		selectHasLock   = false;
		selectInitiated = false;
		selecting       = false;
		resetCursor();
		repaint();
	}
    
    
    
    private void selectAddRemove( boolean adding, boolean toggling ) {
    	if (selecting)
    		return;
    	
		EditorComponent ecom = getComponentAt( getMousePosWorld() );
		
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
		
		repaint();
	}
    
    
    
    private void selectUnderlying() {
		selection.clear();
		
		EditorComponent ecom = getComponentAt( getMousePosWorld() );
		
		if (ecom != null)
			selection.set( ecom );
	}
    
    
    
    private void doContextMenu( EditorComponent ecom ) {
		if (hasSelection())
			 doContextMenuSelection();
		else doContextMenuSingle( ecom );
	}
    
    
    
	private void doContextMenuSelection() {
		System.out.println( "Selection context menu" );
		
		ContextMenuItem[] items = {
			new ContextMenuString( null, "Whatever", (char) 0, null ),
			new ContextMenuString( null, "Yeah",     (char) 0, null )
		};
		
		ContextMenu cm = new ContextMenu( items );
		cm.show( getEditorPanel(), getMousePosCom() );
	}
	
	
	
	private void doContextMenuSingle( EditorComponent ecom ) {
		
	}
	
	
	
	private ContextMenu buildContextMenu( EditorComponent ecom ) {
		//ContextMenu cm = new ContextMenu( items );
		return null;
	}
}



















