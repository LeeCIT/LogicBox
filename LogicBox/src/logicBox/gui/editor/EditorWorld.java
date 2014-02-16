


package logicBox.gui.editor;

import java.util.ArrayList;
import java.util.List;
import logicBox.util.Bbox2;
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
		Vec2  pos       = ecom.pos;
		Bbox2 bboxSrc   = ecom.graphic.computeBbox();
		Bbox2 bboxTrans = new Bbox2( bboxSrc.tl.add(pos), bboxSrc.br.add(pos) );
		
		ecoms.add( ecom );
		grid.add( bboxTrans, ecom );
	}
	
	
	
	/**
	 * Remove a component from the world.
	 * This doesn't destroy it or detach it from the simulation or anything.
	 * @param ecom
	 */
	public void remove( EditorComponent ecom ) {
		ecoms.remove( ecom );
		grid .remove( ecom );
	}
	
	
	
	/**
	 * Move a component.
	 * Never move an ECom around without calling this function.
	 */
	public void move( EditorComponent ecom, Vec2 to ) {
		ecom.pos = to;
		
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
			if (ecom.graphic.contains( transformToComLocalSpace(pos,ecom) ))
				list.add( ecom );
		
		return list;
	}
	
	
	
	/**
	 * Find all components which overlap the given bounding box.
	 */
	public List<EditorComponent> find( Bbox2 bbox ) {
		List<EditorComponent> list = new ArrayList<>();
		
		// TODO
		//for (EditorComponent ecom: grid.findPotentials( bbox ))
		//	if (ecom.graphic.contains( transformToComLocalSpace(pos,ecom) ))
		//		list.add( ecom );
		
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
		return null; // TODO getOccupiedWorldExtent
	}
	
	
	
	// TODO this should be cached when components change, which is relatively infrequent
	// TODO this won't work for Bbox2... shit.  Gotta implement Poly2 now.
	// TODO actaully, just make graphics be transformed already.  damn son
	private Vec2 transformToComLocalSpace( Vec2 pos, EditorComponent ecom ) {
		return pos.subtract( ecom.pos ).rotate( -ecom.angle );
	}
}
