


package prototypes.snappingProto;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import logicBox.util.Geo;
import logicBox.util.Vec2;



public class SnappingPrototype extends ComponentAdapter {
	private boolean   motionInterlock;
	private Component snapTo; // Ignore for now, just snap to desktop
	private int snappingDistance = 16;
	
	
	
	public SnappingPrototype( Component snapTo ) {
		this.snapTo = snapTo;
	}
	
	
	
	public void componentMoved( ComponentEvent ev ) {
		if (motionInterlock)
			return;
		
		//Rectangle      desk  =//GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		Rectangle      ref   = snapTo.getBounds();
		Rectangle      com   = ev.getComponent().getBounds();
		List<EdgePair> edges = getSnappableEdgePairs( ref, com, snappingDistance);
		
		for (EdgePair pair: edges) {
			System.out.println( "C-" + pair.com.edge + " to R-" + pair.ref.edge + " (" + pair.dist +"px)" );
			snapToEdge( ev.getComponent(), com, pair );
		}
	}
	
	
	
	private void snapToEdge( Component comp, Rectangle rectComp, EdgePair edgePair ) {
		Vec2 pos  = new Vec2( comp.getLocation() );
		Vec2 size = new Vec2( comp.getWidth(), comp.getHeight() );
		
		Rectangle compRect = comp.getBounds();
		Rectangle snapRect = snapTo.getBounds();
		
		// Increase the size of the component box so it snaps within the distance required only
		compRect.x += snappingDistance;
		compRect.y += snappingDistance;

		if( snapRect.intersects(compRect) ) {
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
		}
		
			

		motionInterlock = true;
		comp.setLocation( (int) pos.x, (int) pos.y );
		motionInterlock = false;
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
		public double  dist;

		public EdgePair( EdgePos ref, EdgePos com, double dist ) {
			this.ref  = ref;
			this.com  = com;
			this.dist = dist;
		}
	}
	
	
	
	private EdgePos[] getEdges( Rectangle rect ) {
		Vec2 pos  = new Vec2( rect.getX(),     rect.getY()      );
		Vec2 size = new Vec2( rect.getWidth(), rect.getHeight() );
		
		EdgePos[] edges = {
			new EdgePos( Edge.left,   pos.x          ),
			new EdgePos( Edge.top,    pos.y          ),
			new EdgePos( Edge.right,  pos.x + size.x ),
			new EdgePos( Edge.bottom, pos.y + size.y ),
		};
		
		return edges;
	}
	
	
	
	private List<EdgePair> getEdgesWithinDistanceThreshold( EdgePos[] refs, EdgePos[] coms, double thresh ) {
		List<EdgePair> list = new ArrayList<>();
		
		for (EdgePos ref: refs)
		for (EdgePos com: coms) {
			double dist = Geo.absDiff( ref.pos, com.pos );
			
			if (dist <= thresh)
				list.add( new EdgePair(ref,com,dist) );
		}
		
		return list;
	}
	
	
	
	private List<EdgePair> getSnappableEdgePairs( Rectangle ref, Rectangle com, double thresh ) {
		EdgePos[] edgeRef = getEdges( ref );
		EdgePos[] edgeCom = getEdges( com );
		
		return getEdgesWithinDistanceThreshold( edgeRef, edgeCom, thresh );
	}
	
	
	
	
	
	//Demo main, just to test the functionality
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JLabel label = new JLabel("Move to sides to snap. Main Frame");

		JFrame secondDemo = new JFrame();
		JLabel secondLab  = new JLabel("Demo test, move towards the edge of the screen to snap, Second frame");

		//First frameC
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.addComponentListener( new SnappingPrototype() );
		frame.setSize(400, 400); //Just simulating demo size
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		//Second frame
		secondDemo.getContentPane().add(secondLab);
		secondDemo.addComponentListener( new SnappingPrototype(frame) );
		secondDemo.setSize(150, 300);
		secondDemo.setVisible(true);
		secondDemo.setLocationRelativeTo(null);
	}
}



