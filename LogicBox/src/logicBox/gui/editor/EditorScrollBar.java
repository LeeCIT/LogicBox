


package logicBox.gui.editor;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.BoundedRangeModel;
import javax.swing.JScrollBar;
import logicBox.util.Bbox2;
import logicBox.util.Callback;
import logicBox.util.Evaluator;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Scrollbar specialised for the editor.
 * It adapts to reflect the viewable area of the camera and can control its position.
 * @author Lee Coakley
 */
public class EditorScrollBar extends JScrollBar
{
	private static final int extentBorder = 1024; // World extent tolerance (more usable with some give)
	private static final int scrollRange  = 256;  // Range of the scrollbar values
	
	private Camera           cam;
	private Evaluator<Bbox2> evalWorldExtent;
	private Evaluator<Bbox2> evalViewExtent;
	private Bbox2            view;
	private Bbox2            extent;
	private boolean          receiveInterlock;
	private boolean          transmitInterlock;
	private boolean          isHorizontal;
	
	
	
	public EditorScrollBar( int orientation ) {
		super( orientation );
		this.isHorizontal = (orientation == HORIZONTAL);
	}
	
	
	
	public void attachTo( Camera cam, Evaluator<Bbox2> evalWorldExtent, Evaluator<Bbox2> evalViewExtent ) {
		this.cam             = cam;
		this.evalWorldExtent = evalWorldExtent;
		this.evalViewExtent  = evalViewExtent;
		
		Callback cb = createCameraSyncCallback();
		cam.addTransformCallback( cb );
		cb.execute(); // Ensure view/extent are always non-null
		
		setupEvents();
	}
	
	
	
	public void setOrientation( int orientation ) {
		throw new UnsupportedOperationException();
	}
	
	
	
	private void setupEvents() {
		addAdjustmentListener( new AdjustmentListener() {
			public void adjustmentValueChanged( AdjustmentEvent ev ) {
				if (transmitInterlock)
					return;
				
				if (isHorizontal)
					 moveCameraX();
				else moveCameraY();
			}
		});
	}
	
	
	
	private void moveCameraX() {
		moveCamera(
			true,
			extent.getLeft(),
			extent.getRight(),
			view.getSize().x
		);
	}
	
	
	
	private void moveCameraY() {
		moveCamera(
			false,
			extent.getTop(),
			extent.getBottom(),
			view.getSize().y
		);
	}
	
	
	
	private void moveCamera( boolean isX, double extMin, double extMax, double viewSize ) {
		double viewHalf = viewSize * 0.5;
		double min      = extMin + viewHalf;
		double max      = extMax - viewHalf;
		
		double f   = getFrac();
		double v   = Geo.lerp( min, max, f );
		Vec2   pos = cam.getPan();
		
		if (isX)
			 pos.x = v;
		else pos.y = v;
		
		receiveInterlock = true;
		cam.panTo( pos );
		receiveInterlock = false;
	}
	
	
	
	private Callback createCameraSyncCallback() {
		return new Callback() {
			public void execute() {
				if (receiveInterlock)
					return;
				
				updateBounds();
				
				if (isHorizontal)
					 syncToCamX();
				else syncToCamY();
			}
		};
	}
	
	
	
	private void updateBounds() {
		view   = evalViewExtent .evaluate();
		extent = evalWorldExtent.evaluate().expand( extentBorder * 2 );
	}
	
	
	
	private double getFrac() {
		BoundedRangeModel m = getModel();
		return Geo.boxStep( m.getValue(), m.getMinimum(), m.getMaximum()-m.getExtent() );
	}



	private void syncToCamX() {
		syncToCam(
			view.getLeft(), view.getSize().x,
			extent.getLeft(), extent.getRight(), extent.getSize().x
		);
	}
	
	
	
	private void syncToCamY() {	
		syncToCam(
			view.getTop(), view.getSize().y,
			extent.getTop(), extent.getBottom(), extent.getSize().y
		);
	}
	
	
	
	private void syncToCam( double viewMin, double viewSize, double extMin, double extMax, double extSize ) {	
		boolean canMove = (viewSize < extSize);
		
		setEnabled( canMove );
		
		if ( ! canMove)
			return;
		
		double fracMin  = Geo.boxStep( viewMin, extMin, extMax );
		double fracSize = viewSize / extSize;
		
		if (fracMin + fracSize > 1.0)
			fracMin = 1.0 - fracSize;
		
		int val = (int) Geo.roundArith( scrollRange * fracMin  );
		int ext = (int) Geo.roundArith( scrollRange * fracSize );
		
		transmitInterlock = true;
		setValues( val, ext, 0, scrollRange );
		transmitInterlock = false;		
	}
}















