


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
		this.cellSizePower  = log2i( cs );
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
	
	
	
	public Iterable<T> find( Vec2 pos ) {
		AccumulationTraversal atrx = new AccumulationTraversal();
		traverse( pos, atrx );
		return atrx.getAccumulation();
	}
	
	public Iterable<T> find( Bbox2 bbox ) {
		AccumulationTraversal atrx = new AccumulationTraversal();
		traverse( bbox, atrx );
		return atrx.getAccumulation();
	}
	
	public Iterable<T> find( Line2 line ) {
		AccumulationTraversal atrx = new AccumulationTraversal();
		traverse( line, atrx );
		return atrx.getAccumulation();
	}
	
	
	
	public void remove( T obj ) {
		for (Cell cell: core)
			cell.remove( obj );
	}
	
	
	
	public void clear() {
		for (Cell cell: core)
			cell.clear();
	}
	
	
	
	public double getLoadOnNonEmptyCells() {
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
	
	
	
	
	private int log2i( double x ) {
		return (int) (Math.log(x) / Math.log( 2.0 ));
	}
	
	
	
	private Set<T> createIdentityHashSet() {
		return Collections.newSetFromMap( new IdentityHashMap<T,Boolean>() );
	}
	
	
	
	private int coordToIndex( int x, int y ) {
		return ((y * cellsPerRow) + x) & indexWrapMask; // It even chops the sign bit. Magical.
	}
	
	
	
	private void traverse( Vec2 pos, TraversalFunctor trav ) {
		int x = ((int) pos.x) >> cellSizePower;
		int y = ((int) pos.y) >> cellSizePower;
		trav.process( coordToIndex(x,y) );
	}
	
	

	private void traverse( Bbox2 bbox, TraversalFunctor trav ) {
		int xb = ((int) bbox.tl.x           ) >> cellSizePower; // Snap low, div
		int yb = ((int) bbox.tl.y           ) >> cellSizePower; // 
		int xe = ((int) bbox.br.x + cellSize) >> cellSizePower; // Snap high, div
		int ye = ((int) bbox.br.y + cellSize) >> cellSizePower; // 

	    for (int y=yb; y<ye; y++)
	    for (int x=xb; x<xe; x++)
	    	trav.process( coordToIndex(x,y) );
	}
	
	
	
	private void traverse( Line2 line, TraversalFunctor trav ) {
		// TODO need DDA line algo here
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
		SpatialGrid<String> sg = new SpatialGrid<>( 256, 512, 64 );
		
		sg.add( new Vec2(297,261),           "Vec2" );
		sg.add( new Bbox2(129,129, 256,512), "Bbox" );
		
		for (String s: sg.find( new Vec2(256,256) ))
			System.out.println( "\t" + s );
		
		System.out.println( "Load: " + sg.getLoadOnNonEmptyCells() );
	}
}




























