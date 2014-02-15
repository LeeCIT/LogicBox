


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
	
	
	
	public void add( EditorComponent ecom ) {
		Vec2  pos       = ecom.pos;
		Bbox2 bboxSrc   = ecom.graphic.getBbox();
		Bbox2 bboxTrans = new Bbox2( bboxSrc.tl.add(pos), bboxSrc.br.add(pos) );
		
		ecoms.add( ecom );
		grid.add( bboxTrans, ecom );
	}
	
	
	
	public void remove( EditorComponent ecom ) {
		ecoms.remove( ecom );
		grid .remove( ecom );
	}
	
	
	
	public void move( EditorComponent ecom, Vec2 to ) {
		ecom.pos = to;
		
		remove( ecom );
		add   ( ecom );
	}
	
	
	
	public EditorComponent findTopmostAt( Vec2 pos ) {
		List<EditorComponent> list = find( pos );
		
		if ( ! list.isEmpty())
			 return list.get( list.size() - 1 ); 
		else return null;
	}
	
	
	
	public List<EditorComponent> find( Vec2 pos ) {
		List<EditorComponent> list = new ArrayList<>();
		
		for (EditorComponent ecom: grid.findPotentials( pos ))
			if (ecom.graphic.contains( transformToComLocalSpace(pos,ecom) ))
				list.add( ecom );
		
		return list;
	}
	
	
	
	public List<EditorComponent> getComponents() {
		return ecoms;
	}
	
	
	
	public Bbox2 getOccupiedWorldExtent() {
		return null; // TODO getOccupiedWorldExtent
	}
	
	
	
	// TODO this should be cached when components change, which is relatively infrequent
	// TODO this won't work for Bbox2... shit.  Gotta implement Poly2 now.
	private Vec2 transformToComLocalSpace( Vec2 pos, EditorComponent ecom ) {
		return pos.subtract( ecom.pos ).rotate( -ecom.angle );
	}
}
