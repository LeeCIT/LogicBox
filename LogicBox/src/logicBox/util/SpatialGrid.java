


package logicBox.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;



/**
 * Divides space into chunks for fast broad-phase searching.
 * @author Lee Coakley
 */
public class SpatialGrid<T>
{
	private ArrayList<Cell> core;
	private int	cellSize;
	private int cellsPerRow;
	private int cellsPerColumn;
	private int cellSizePower;
	private int indexWrapMask;
	
	
	
	public SpatialGrid( int width, int height, int cellSize ) {
		int cs = Geo.roundToNextPowerOfTwo( cellSize );
		
		if (cs != cellSize)
			throw new RuntimeException( "Cell size must be a power of 2.  Got " + cellSize );
		
		this.cellSize       = cs;
		this.cellSizePower  = (int) Geo.log2( cs );
		this.cellsPerRow    = (int) Math.ceil( width  / (double) cs );
		this.cellsPerColumn = (int) Math.ceil( height / (double) cs );
		
		int cellCount      = Geo.roundToNextPowerOfTwo( cellsPerRow * cellsPerColumn );
		this.indexWrapMask = cellCount - 1; // 0001_0000 -> 0000_1111
		
		this.core = new ArrayList<>();
		for (int i=0; i<cellCount; i++)
			core.add( new Cell() );
	}
	
	
	
	public void add( Vec2 pos, T object ) {
		traverse( pos, new InsertionTraversal(object) );
	}
	
	public void add( Bbox2 bbox, T object ) {
		traverse( bbox, new InsertionTraversal(object) );
	}
	
	public void add( Line2 line, T object ) {
		traverse( line, new InsertionTraversal(object) );
	}
	
	
	
	public Iterable<T> findPotentials( Vec2 pos ) {
		return traverse( pos, new AccumulationTraversal() ).getAccumulation();
	}
	
	public Iterable<T> findPotentials( Bbox2 bbox ) {
		return traverse( bbox, new AccumulationTraversal() ).getAccumulation();
	}
	
	public Iterable<T> findPotentials( Line2 line ) {
		return traverse( line, new AccumulationTraversal() ).getAccumulation();
	}
	
	
	
	public void remove( T obj ) {
		for (Cell cell: core)
			cell.remove( obj );
	}
	
	
	
	public void clear() {
		for (Cell cell: core)
			cell.clear();
	}
	
	
	
	public int getCellSize() {
		return cellSize;
	}
	
	
	
	public int getCellsPerRow() {
		return cellsPerRow;
	}
	
	
	
	public int getCellsPerColumn() {
		return cellsPerColumn;
	}
	
	
	
	public int[][] debugGridLevels() {
		int     w = cellsPerRow;
		int     h = cellsPerColumn;
		int[][] g = new int[ h ][ w ];
		
		for (int y=0; y<h; y++)
		for (int x=0; x<w; x++)
			g[y][x] = core.get(y*w+x).size();
		
		return g;
	}
	
	
	
	public double debugLoadOnNonEmptyCells() {
		double load  = 0.0;
		double total = 0.0;
		
		for (Cell cell: core) {
			if (cell.size() > 0) {
				load += cell.size();
				total++;
			}
		}
		
		if (total != 0)
			 return load / total;
		else return 0;
	}
	
	
	
	private Set<T> createIdentityHashSet() {
		return Collections.newSetFromMap( new IdentityHashMap<T,Boolean>() );
	}
	
	
	
	private int coordToIndex( int x, int y ) {
		return ((y * cellsPerRow) + x) & indexWrapMask; // It even chops the sign bit. Magical.
	}
	
	
	
	private <TF extends TraversalFunctor> TF traverse( Vec2 pos, TF trav ) {
		int x = ((int) pos.x) >> cellSizePower;
		int y = ((int) pos.y) >> cellSizePower;
		trav.process( coordToIndex(x,y) );
		return trav;
	}
	
	

	private <TF extends TraversalFunctor> TF traverse( Bbox2 bbox, TF trav ) {
		int xb = ((int) bbox.tl.x           ) >> cellSizePower; // Snap low, div
		int yb = ((int) bbox.tl.y           ) >> cellSizePower; // 
		int xe = ((int) bbox.br.x + cellSize) >> cellSizePower; // Snap high, div
		int ye = ((int) bbox.br.y + cellSize) >> cellSizePower; // 

	    for (int y=yb; y<ye; y++)
	    for (int x=xb; x<xe; x++)
	    	trav.process( coordToIndex(x,y) );
	    
	    return trav;
	}
	
	
	
	private <TF extends TraversalFunctor> TF traverse( Line2 line, TF trav ) {
		// TODO need DDA line algo here
		traverse( line.getBbox(), trav );
		return trav;
	}
	
	
	
	
	
	private class Cell extends ArrayList<T> {};
	
	
	
	private interface TraversalFunctor {
		public void process( int index );
	}
	
	
	
	private class InsertionTraversal implements TraversalFunctor {
		private T obj;
		
		protected InsertionTraversal( T obj ) {
			this.obj = obj;
		}
		
		public void process( int index ) {
			core.get(index).add( obj );
		}
	}
	
	
	
	private class AccumulationTraversal implements TraversalFunctor {
		private Set<T> set;
		
		protected AccumulationTraversal() {
			set = createIdentityHashSet();
		}
		
		public void process( int index ) {
			for (T t: core.get(index))
				set.add( t );
		}
		
		protected Set<T> getAccumulation() {
			return set;
		}
	}
	
	
	
	
	
	public static void main( String[] args ) {
		SpatialGrid<String> sg = new SpatialGrid<>( 640, 480, 16 );
		
		sg.add( new Vec2(197,261),           "Vec2" );
		sg.add( new Bbox2(64,129,  256,512), "BboxA" );
		sg.add( new Bbox2(199,219, 320,512), "BboxB" );
		sg.add( new Bbox2(199,229, 480,512), "BboxC" );
		
		for (String s: sg.findPotentials( new Vec2(256,256) ))
			System.out.println( "\t" + s );
		
		System.out.println( "Load: " + sg.debugLoadOnNonEmptyCells() );
		
		
		int[][] g = sg.debugGridLevels();
		for (int y=0; y<g.length; y++) {
			System.out.println();
			
			for (int x=0; x<g[0].length; x++) {
				int    count = g[y][x];
				String s;
					 if (count == 0) s = " ";
				else if (count <= 9) s = ""+count;
				else				 s = "+";
				System.out.print( s );
			}
		}
	}
}





























