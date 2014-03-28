


package logicBox.gui.editor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import logicBox.gui.Gfx;
import logicBox.util.Bbox2;
import logicBox.util.Evaluator;
import logicBox.util.Geo;
import logicBox.util.Util;
import logicBox.util.Vec2;



/**
 * The main simulation editor interface.
 * @author Lee Coakley
 */
public class EditorPanel extends JPanel
{
	private Set<RepaintListener> repaintListeners;
	
	private Camera cam;
	private Evaluator<List<Graphic>> evalViewables;
	
	private boolean enableGrid;
	private boolean enableAntialiasing;
	private boolean isPrinting;
	
	
	
	public EditorPanel() {
		super( true );
		
		this.enableGrid         = true;
		this.enableAntialiasing = true;
		this.repaintListeners   = Util.createIdentityHashSet();
	}
	
	
	
	public void setCamera( Camera cam ) {
		this.cam = cam;
	}
	
	
	
	public void setViewablesEvaluator( Evaluator<List<Graphic>> eval ) {
		this.evalViewables = eval;
	}
	
	
	
	public void setAntialiasingEnabled( boolean state ) {
		enableAntialiasing = state;
		repaint();
	}
	
	
	
	public boolean getAntialiasingEnabled() {
		return enableAntialiasing;
	}
	
	
	
	public void setGridEnabled( boolean state ) {
		enableGrid = state;
		repaint();
	}
	
	
	
	public boolean getGridEnabled() {
		return enableGrid;
	}
	
	
	
	public void addRepaintListener( RepaintListener rl ) {
		repaintListeners.add( rl );
	}
	
	
	
	public void removeRepaintListener( RepaintListener rl ) {
		repaintListeners.remove( rl );
	}
	
	
	
	public void print( Graphics g ) {
		// TODO zoom out etc
		isPrinting = true;
		super.print( g );
		isPrinting = false;
	}
	
	
	
	protected void paintComponent( Graphics g ) {
		draw( (Graphics2D) g );
	}
	
	
	
	private void draw( Graphics2D g ) {
		Gfx.pushMatrix( g );
			Gfx.pushAntialiasingStateAndSet( g, enableAntialiasing );
				fillBackground( g );
				applyCameraTransform( g );
				drawGrid( g );
				drawComponentGraphics( g );
				drawRepaintListeners( g );
			Gfx.popAntialiasingState( g );
		Gfx.popMatrix( g );
	}
	
	
	
	private void applyCameraTransform( Graphics2D g ) {
		if (cam != null)
			cam.applyTransform( g );
	}
	
	
	
	private void drawComponentGraphics( Graphics2D g ) {
		if (evalViewables == null)
			return;
		
		for (Graphic graphic: evalViewables.evaluate()) {
			boolean wasInverted = graphic.isInverted();
			
			graphic.setInverted( isPrinting );
			graphic.draw( g );
			graphic.setInverted( wasInverted );
		}
	}
	
	
	
	private void drawRepaintListeners( Graphics2D g ) {
		for (RepaintListener rpl: repaintListeners)
			rpl.draw( g );
	}
	
	
	
	private void fillBackground( Graphics2D g ) {
		Gfx.pushColorAndSet( g, (isPrinting) ? Color.white : EditorStyle.colBackground );
			Gfx.pushAntialiasingStateAndSet( g, false );
				g.fillRect( 0, 0, getWidth(), getHeight() );
			Gfx.popAntialiasingState( g );
		Gfx.popColor( g );
	}
	
	
	
	private void drawGrid( Graphics2D g ) {
		if ( ! enableGrid || isPrinting)
			return;
		
		Bbox2 worldRegion  = cam.getWorldViewArea();
		Vec2  cellSize     = new Vec2( 64 );
		Vec2  cellSizeHalf = cellSize.multiply( 0.5 );
		Vec2  offset       = worldRegion.tl.modulo( cellSize ).negate().subtract( cellSizeHalf );
		
		worldRegion = worldRegion.expand( cellSize.x * 3.0 );
		
		double  zoom        = cam.getZoom();
		double  zoomMin     = cam.getZoomMin();
		double  thickThresh = 1.0 / 3.0;
		boolean fakeThin    = zoom <= thickThresh;
		boolean disableAA   = fakeThin || (zoom > 2);
		double  thickness   = fakeThin ? 1.0 : EditorStyle.gridThickness;
		double  thinness    = 1.0 - Geo.boxStep( zoom, zoomMin, thickThresh );
		double  thinSoften  = 0.8;
		double  colFactor   = thinness * thinSoften;
		
		Color col = Geo.lerp( EditorStyle.colGrid, EditorStyle.colBackground, colFactor );
		
		if (disableAA)
			Gfx.pushAntialiasingStateAndSet( g, false );
		
		Gfx.pushColorAndSet( g, col );
			Gfx.drawGrid( g, worldRegion, offset, cellSize, thickness );
		Gfx.popColor( g );
		
		if (disableAA)
			Gfx.popAntialiasingState( g );
	}
}

























