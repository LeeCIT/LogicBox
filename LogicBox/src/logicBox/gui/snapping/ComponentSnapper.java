


package logicBox.gui.snapping;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import logicBox.util.Bbox2;
import logicBox.util.Geo;
import logicBox.util.Vec2;



public class ComponentSnapper extends ComponentAdapter {
	private boolean   motionInterlock;
	private Component snapTo; 
	private int       snapThreshold = 16;
	
	
	
	public ComponentSnapper( Component snapTo ) {
		this.snapTo = snapTo;
	}
	
	
	
	public void componentMoved( ComponentEvent ev ) {
		if (motionInterlock)
			return;
		
		Component snapee      = ev.getComponent();
		Bbox2     bboxDesk    = new Bbox2( GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds() );
		Bbox2     bboxComp    = new Bbox2( snapee.getBounds() );
		Bbox2     bboxSnap    = new Bbox2( snapTo.getBounds() );
		Bbox2     bboxOverlap = bboxSnap.expand( new Vec2(snapThreshold*2) );
		boolean   shouldSnap  = bboxOverlap.overlaps( bboxComp );
		
		if (shouldSnap) {
			Vec2           pos   = new Vec2( snapee.getLocation() );
			List<EdgePair> edges = getSnappableEdgePairs( bboxSnap, bboxComp, snapThreshold );
			
			for (EdgePair pair: edges)
				pos = snapToEdge( pos, bboxComp, pair );
			
			motionInterlock = true;
			snapee.setLocation( (int) pos.x, (int) pos.y );
			motionInterlock = false;
		}
	}
	
	
	
	private Vec2 snapToEdge( Vec2 compPos, Bbox2 bboxComp, EdgePair edgePair ) {
		Vec2 pos  = compPos.copy();
		Vec2 size = bboxComp.getSize();
		
		switch (edgePair.ref.edge) {
			case left:
			case right:
				switch (edgePair.com.edge) {
					case left:   pos.x = edgePair.ref.pos;           break;
					case right:  pos.x = edgePair.ref.pos - size.x;  break;
				} break;
			case top:
			case bottom:
				switch (edgePair.com.edge) {
					case top:    pos.y = edgePair.ref.pos;           break;
					case bottom: pos.y = edgePair.ref.pos - size.y;  break;
				} break;
		}
		
		return pos;
	}
	
	
	
	private enum Edge {
		left,
		right,
		top,
		bottom
	};
	
	
	
	private class EdgePos {
		public Edge   edge;
		public double pos;
		
		public EdgePos( Edge edge, double pos ) {
			this.edge = edge;
			this.pos  = pos;
		}
	};
	
	
	
	private class EdgePair {
		public EdgePos ref,com;

		public EdgePair( EdgePos ref, EdgePos com ) {
			this.ref  = ref;
			this.com  = com;
		}
	}
	
	
	
	private EdgePos[] getEdges( Bbox2 rect ) {
		EdgePos[] edges = {
			new EdgePos( Edge.left,   rect.getLeft()   ),
			new EdgePos( Edge.top,    rect.getTop()    ),
			new EdgePos( Edge.right,  rect.getRight()  ),
			new EdgePos( Edge.bottom, rect.getBottom() ),
		};
		
		return edges;
	}
	
	
	
	private List<EdgePair> getEdgesWithinDistanceThreshold( EdgePos[] refs, EdgePos[] coms, double thresh ) {
		List<EdgePair> list = new ArrayList<>();
		
		for (EdgePos ref: refs)
		for (EdgePos com: coms) {
			double dist = Geo.absDiff( ref.pos, com.pos );
			
			if (dist <= thresh)
				list.add( new EdgePair(ref,com) );
		}
		
		return list;
	}
	
	
	
	private List<EdgePair> getSnappableEdgePairs( Bbox2 ref, Bbox2 com, double thresh ) {
		EdgePos[] edgeRef = getEdges( ref );
		EdgePos[] edgeCom = getEdges( com );
		
		return getEdgesWithinDistanceThreshold( edgeRef, edgeCom, thresh );
	}
	
	
	
	
	
	// Demo main, just to test the functionality
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel("Move to sides to snap. Main Frame");

		JFrame secondDemo = new JFrame();
		JLabel secondLab  = new JLabel("Demo test, move towards the edge of the screen to snap, Second frame");

		// First frameC
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.addComponentListener( new SnappingPrototype() );
		frame.setSize(400, 400); //Just simulating demo size
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		// Second frame
		secondDemo.getContentPane().add(secondLab);
		secondDemo.addComponentListener( new ComponentSnapper(frame) );
		secondDemo.setSize(150, 300);
		secondDemo.setVisible(true);
		secondDemo.setLocationRelativeTo(null);
	}
}



