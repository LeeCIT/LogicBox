


package logicBox.gui.editor;

import java.util.ArrayList;
import java.util.List;
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
		grid  = new SpatialGrid<>( 1024, 1024, 64 );
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
	}
	
	
	
	/**
	 * Update the underlying world structures in response to
	 * a component orientation or position change.
	 */
	public void onComponentTransform( EditorComponent ecom ) {
		remove( ecom );
		add   ( ecom );
	}
	
	
	
	/**
	 * Find the component most recently added at the given position.
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
		
		for (EditorComponent ecom: ecoms) 
			if (ecom.graphic.getBbox().overlaps( bbox ))
				list.add( ecom );
		
		return list;
	}
}






























