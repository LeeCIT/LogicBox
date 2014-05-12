


package logicBox.util;

import java.util.Arrays;



/**
 * Implements the "Digital Differential Analyser" line traversal algorithm.
 * @author Lee Coakley
 */
public abstract class LineDDA
{
	public static void traverseDdaLine( Vec2 a, Vec2 b, int cellsize, final CallbackParam<int[]> callback ) {
		final Vec2  va   = a.copy();
		final Vec2  vb   = b.copy();
		final int[] offs = doNegOffsets( va, vb, cellsize );
		final int[] cell = new int[ 2 ];
		
		traverseDdaLineUnsigned( va, vb, cellsize, new CallbackParam<int[]>() {
			public void execute( int[] c ) {
				cell[0] = c[0] - offs[0];
				cell[1] = c[1] - offs[1];
				callback.execute( cell );
			}
		});
	}
	
	
	
	private static int[] doNegOffsets( Vec2 a, Vec2 b, int cs ) {
		double[] negs = new double[ 2 ];
		int   [] offs = new int   [ 2 ];
		Vec2  [] vecs = { a, b };
		
		for (int i=0; i<2; i++) {
			negs[i] = maxNegDeltaZ( a.get(i), b.get(i) );
			offs[i] = (int) (Geo.ceilToMultiple(negs[i], cs) / cs);
		}
		
		for (int v=0; v<2; v++) {
			vecs[v].x += offs[0] * cs;
			vecs[v].y += offs[1] * cs;
		}
		
		return offs;
	}
	
	
	
	private static double maxNegDeltaZ( double a, double b ) {
		if (a<0 || b<0)
			 return Math.abs( Math.min(a,b) );
		else return 0;
	}
	
	
	
	private static void traverseDdaLineUnsigned( Vec2 a, Vec2 b, int cellsize, CallbackParam<int[]> callback ) {
		int[] start    = { (int) a.x,       (int) a.y };
		int[] end      = { (int) b.x,       (int) b.y };
		int[] delta    = { end[0]-start[0], end[1]-start[1] };
		
		Vec2      uv        = Geo.normalise( new Vec2(delta[0], delta[1]) ); // Delta unit vector.
	    double [] deltaUV   = uv.toArray();
	    boolean[] deltaZ    = { delta[0]==0, delta[1]==0 };
	    boolean[] deltaGEZ  = { delta[0]>=0, delta[1]>=0 };
	    int    [] cellStart = new int[2];
	    int    [] cellEnd   = new int[2];
	    int    [] cellCur   = new int[2];
	    
	    for (int ax=0; ax<2; ax++) {
	    	cellStart[ax] = start[ax] / cellsize;
	    	cellEnd  [ax] = end  [ax] / cellsize;
	    	cellCur  [ax] = cellStart[ax];
	    }
	    
	    if (deltaZ[0] && deltaZ[1]) { // Goin' nowhere
	    	callback.execute( cellStart );
	    	return;
	    }
	    
	    int   [] pStep   = new int   [2]; // Loop step direction, -1 or +1.
	    double[] pDelta  = new double[2]; // Loop increment, 0-inf as axisd approaches 0.
	    double[] axisAcc = new double[2]; // Loop initial threshold for cell crossover.
	    
	    for (int ax=0; ax<2; ax++) {
	    	int srcp   = -(start[ax] % cellsize);
		    int addGEZ = deltaGEZ[ax] ? cellsize : 0;
		    int offset = srcp + addGEZ;
		    
		    pStep  [ax] = deltaGEZ[ax] ? +1 : -1;
		    pDelta [ax] = cellsize / Math.abs(deltaUV[ax]);
		    axisAcc[ax] = offset / deltaUV[ax];
		    
		    if (deltaZ[ax])                     // Ensure only one delta is used in this case.
		    	axisAcc[ax] = Double.MAX_VALUE; // (If both are zero, the function will have returned)
	    }
	    
	    
	    boolean hitX = false;
	    boolean hitY = false;
	    
	    for (;;) {
	    	callback.execute( cellCur );
	    	
	        if (cellCur[0] == cellEnd[0]) hitX = true;
	        if (cellCur[1] == cellEnd[1]) hitY = true;
	        
	        if (hitX && hitY)
	            return;
	        
	        int     ax   = (axisAcc[0] >= axisAcc[1]) ? 1 : 0;
	        axisAcc[ax] += pDelta[ax];
	        cellCur[ax] += pStep [ax];
	    }
	}
	
	
	
	
	
	public static void main( String[] args ) {
		final Vec2  a = new Vec2( -32, -96 );
		final Vec2  b = new Vec2( 224, 224 );
		final int   c = 64;
		
		traverseDdaLine( a, a, c, new CallbackParam<int[]>() {
			public void execute( int[] cell ) {
				System.out.println(  Arrays.toString(cell)  );
			}
		});
		
		System.out.println( "\n----------\n" );
		
		traverseDdaLine( a, b, c, new CallbackParam<int[]>() {
			public void execute( int[] cell ) {
				System.out.println(  Arrays.toString(cell)  );
			}
		});
		
		System.out.println( "\n----------\n" );
		
		final int[] o = doNegOffsets( a, b, c );
		traverseDdaLineUnsigned( a, b, c, new CallbackParam<int[]>() {
			public void execute( int[] c ) {
				int[] cell = { c[0]-o[0], c[1]-o[1] };
				System.out.println( Arrays.toString(cell) );
			}
		});
	}
}































