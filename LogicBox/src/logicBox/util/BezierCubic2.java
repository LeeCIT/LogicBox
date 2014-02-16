


package logicBox.util;

import java.util.ArrayList;
import java.util.List;



/**
 * Defines a cubic bezier curve.
 * Provides parametric point finding and line-based intersect and traversal.
 * @author Lee Coakley
 */
public class BezierCubic2
{
	public Vec2 a, c1, c2, b;
	
	
	
	public BezierCubic2( Vec2 a, Vec2 c1, Vec2 c2, Vec2 b ) {
		this.a  = a;
		this.c1 = c1;
		this.c2 = c2;
		this.b  = b;
	}
	
	
	
	public interface BezierTraverser {
		public void process( Line2 line ); 
	}
	
	
	
	/**
	 * Traverse the curve by splitting it into a series of line segments.
	 */
	public void traverse( int segments, BezierTraverser bt ) {
		Vec2   last = getPoint( 0 );
		double t    = 1.0 / (double)(segments);
		double acc  = t;
		
		for (int i=1; i<=segments; i++) {
			Vec2 p = getPoint( acc );
			bt.process( new Line2(last,p) );
			last = p;
			acc += t;
		}
	}
	
	
	
	/**
	 * Access the curve coordinates parametrically.
	 * @param t 0 is the beginning of the curve, 1 is the end.
	 */
	public Vec2 getPoint( double t ) {
		Vec2 aC1  = Geo.lerp( a,  c1, t );
		Vec2 c1C2 = Geo.lerp( c1, c2, t );
		Vec2 c2B  = Geo.lerp( c2, b,  t );
		
		Vec2 mlA  = Geo.lerp( aC1,  c1C2, t );
		Vec2 mlB  = Geo.lerp( c1C2, c2B,  t );
		
		return Geo.lerp( mlA, mlB, t );
	}
	
	
	
	public class IntersectResult {
		public boolean    intersects;
		public List<Vec2> posList;
		
		public IntersectResult() {
			posList = new ArrayList<>();
		}
	}
	
	
	
	public IntersectResult intersect( Line2 line, int segments ) {
		final IntersectResult result = new IntersectResult();
		final Line2           ref    = line;
		
		traverse( segments, new BezierTraverser() {
			public void process( Line2 com ) {
				Line2.IntersectResult ir = ref.intersect( com );
				
				if (ir.intersects) {
					result.intersects = true;
					result.posList.add( ir.pos );
				}
			}
		});
		
		return result;
	}
	
	
	
	/**
	 * A cubic bezier curve always lies inside the convex hull of [A, C1, C2, B].
	 */
	public Bbox2 getBbox() {
		List<Vec2> points = new ArrayList<>();
		points.add( a  );
		points.add( c1 );
		points.add( c2 );
		points.add( b  );
		return Bbox2.createFromPoints( points );
	}
	
	
	
	
	
	public static void main( String[] args ) {
		BezierCubic2 bez = new BezierCubic2(
			new Vec2(0,  0),
			new Vec2(0, 10),
			new Vec2(10,10),
			new Vec2(10, 0)
		);
		
		bez.traverse( 32, new BezierTraverser() {
			public void process( Line2 line ) {
				System.out.println( line );
			}
		});
		
		
		System.out.println( "\n\nIntersects:" );
		for (Vec2 v: bez.intersect( new Line2(0,5,10,5), 32).posList)
			System.out.println( v );
		
		
		System.out.println( "\n\nBounds: " );
		System.out.println( bez.getBbox() );
	}
}























