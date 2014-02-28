


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import logicBox.gui.Gfx;
import logicBox.util.Bbox2;
import logicBox.util.BinaryFunctor;
import logicBox.util.Geo;
import logicBox.util.SpatialGrid;
import logicBox.util.Vec2;



/**
 * Stores and queries the "world" of the editor.
 * @author Lee Coakley
 */
public class EditorWorld
{
	private SpatialGrid<EditorComponent> grid;
	private List       <EditorComponent> ecoms;
	
	
	
	public EditorWorld() {
		grid  = new SpatialGrid<>( 2048, 2048, 128 );
		ecoms = new ArrayList<>(); 
	}
	
	
	
	/**
	 * Add a component to the world.
	 * To remove it you have to use remove().
	 */
	public void add( EditorComponent ecom ) {
		Bbox2 bbox = ecom.graphic.getBbox();
		
		ecoms.add( ecom );
		grid.add( bbox, ecom );
		ecom.linkToWorld( this );
	}
	
	
	
	/**
	 * Remove a component from the world.
	 * This doesn't actually remove it from the simulation or anything.
	 * Only the world stops knowing about it.
	 * @param ecom
	 */
	public void remove( EditorComponent ecom ) {
		ecoms.remove( ecom );
		grid .remove( ecom );
		ecom.unlinkFromWorld();
	}
	
	
	
	/**
	 * Update the underlying world structures in response to
	 * a component orientation or position change.
	 * EditorComponents should call this method automatically.
	 */
	public void onComponentTransform( EditorComponent ecom ) {
		remove( ecom );
		add   ( ecom );
	}
	
	
	
	/**
	 * Find one component at the given position.
	 * It is always the most recently modified or moved component.
	 * Returns null if no component is found.
	 */
	public EditorComponent findTopmostAt( Vec2 pos ) {
		List<EditorComponent> list = find( pos );
		
		if ( ! list.isEmpty())
			 return list.get( list.size() - 1 ); 
		else return null;
	}
	
	
	
	/**
	 * Find all components which contain the given position.
	 */
	public List<EditorComponent> find( Vec2 pos ) {
		List<EditorComponent> list = new ArrayList<>();
		
		for (EditorComponent ecom: grid.findPotentials( pos ))
			if (ecom.graphic.contains( pos ))
				list.add( ecom );
		
		return list;
	}
	
	
	
	/**
	 * Find all components which overlap the given bounding box.
	 */
	public List<EditorComponent> find( Bbox2 bbox ) {
		List<EditorComponent> list = new ArrayList<>(); 
		
		for (EditorComponent ecom: grid.findPotentials( bbox ))
			if (ecom.graphic.overlaps( bbox ))
				list.add( ecom );
		
		return list;
	}
	
	
	
	/**
	 * Get a list of all components.
	 * @return
	 */
	public List<EditorComponent> getComponents() {
		return ecoms;
	}
	
	
	
	/**
	 * Find the extent of the world occupied by components.
	 * This is the union of all component bounding boxes.
	 */
	public Bbox2 getOccupiedWorldExtent() {
		List<Bbox2> bboxes = new ArrayList<>();
		
		for (EditorComponent ecom: ecoms)
			bboxes.add( ecom.graphic.getBbox() );
		
		return Geo.reduce( bboxes, new BinaryFunctor<Bbox2>() {
			public Bbox2 call( Bbox2 a, Bbox2 b ) {
				return Bbox2.union( a, b );
			}
		});
	}
	
	
	
	/**
	 * Returns only the components whose bounding boxes lie within (or close to) the view boundary. 
	 */
	public List<EditorComponent> getViewableComponents( Camera cam, double tolerance ) {
		Bbox2 bbox = cam.getWorldViewableArea().expand( new Vec2(tolerance) );
		
		List<EditorComponent> list = new ArrayList<>();
		
		for (EditorComponent ecom: grid.findPotentials( cam.getWorldViewableArea() ))
			if (ecom.graphic.getBbox().overlaps( bbox ))
				list.add( ecom );
		
		return list;
	}
	
	
	
	public RepaintListener getSpatialGridDebugRepainter() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				int[][] array = grid.debugGridLevels();
				double  size  = grid.getCellSize();
				
				for (int y=0; y<array   .length; y++)			
				for (int x=0; x<array[0].length; x++) {
					double count = array[y][x];
					
					if (count <= 0)
						continue;
					
					double colF  = Geo.boxStep( count, 0, 6 );
					Color  col   = Geo.lerp( Color.green, Color.red, colF );
					Vec2   tl    = new Vec2(x,y).multiply( size );
					Bbox2  bbox  = new Bbox2( tl, tl.add(size) ); 
					
					Gfx.pushAntialiasingStateAndSet( g, false );
						Gfx.pushColorAndSet( g, col );
							Gfx.drawBbox( g, bbox, false );
							Gfx.drawOrientedRect( g, bbox.getCentre(), new Vec2(size*0.1), 0, false );
						Gfx.popColor( g );
					Gfx.popAntialiasingState( g );
				}
				
				Gfx.pushAntialiasingStateAndSet( g, false );
					Gfx.pushColorAndSet( g, Color.BLUE );
						Gfx.drawBbox( g, new Bbox2(0,0,grid.getCellsPerRow()*size,grid.getCellsPerColumn()*size), false );
					Gfx.popColor( g );
				Gfx.popAntialiasingState( g );
			}
		};
	}
}






























