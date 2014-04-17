


package logicBox.gui.editor.tools;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorWorld;
import logicBox.util.Geo;
import logicBox.util.Util;
import logicBox.util.Vec2;



/**
 * A selection of editor components.
 * @author Lee Coakley
 */
public class Selection implements Serializable, Iterable<EditorComponent>
{
	private static final long serialVersionUID = 1L;
	
	public final Set<EditorComponent> ecoms;
	public 		 Vec2   pivot;
	public 		 double angle;
	
	
	
	public Selection() {
		ecoms = Util.createIdentityHashSet();
		angle = 0;
	}
	
	
	
	public Vec2 getCentre() {
		return EditorWorld.getExtent( ecoms ).getCentre();
	}
	
	
	
	public void setPivot() {
		pivot = getCentre();
	}
	
	
	
	public Vec2 getPos() {
		return getCentre();
	}
	
	
	
	/**
	 * Set the centre positon of the selection.
	 */
	public void setPos( Vec2 pos ) {
		Vec2 centre = getCentre();
		Vec2 delta  = Geo.delta( pos, centre );
		
		for (EditorComponent ecom: ecoms) {
			Vec2 newPos = ecom.getPos().subtract( delta );
			Vec2 snap   = newPos;//Geo.snapNear( newPos, 16 );
			ecom.setPos( snap );
		}
	}
	
	
	
	public double getAngle() {
		return angle;
	}
	
	
	
	/**
	 * Rotate the selection to the given angle.
	 * This moves and rotates the ecoms around the centre.
	 */
	public void setAngle( double angleTo ) {
		if (size() == 1) {
			iterator().next().setAngle( angleTo );
			return;
		}
		
		System.out.println( "setAngle not implemented for multiple selects" );
	}
	
	
	
	public Selection cut() {
		// TODO
		return null;
	}
	
	
	
	public Selection copy() { // TODO it ain't quite this simple...
		return Util.deepCopy( this );
	}
	
	
	
	public void delete( EditorWorld world ) {
		for (EditorComponent ecom: ecoms) {
			ecom.getComponent().disconnect();
			world.remove( ecom );
		}
		
		clear();
	}
	
	
	
	public void set( EditorComponent ecom ) {
		clear();
		add( ecom );
	}
	
	
	
	public void set( Iterable<EditorComponent> iter ) {
		clear();
		addAll( iter );
	}
	
	
	
	public EditorComponent add( EditorComponent ecom ) {
		ecoms.add( ecom );
		ecom.getGraphic().setSelected( true );
		return ecom;
	}
	
	
	
	public void addAll( Iterable<EditorComponent> iter ) {
		for (EditorComponent e: iter)
			add( e );
	}
	
	
	
	public boolean remove( EditorComponent ecom ) {
		ecom.getGraphic().setSelected( false );
		return ecoms.remove( ecom );
	}
	
	
	
	public void removeAll( Iterable<EditorComponent> iter ) {
		for (EditorComponent e: iter)
			remove( e );
	}
	
	
	
	public void clear() {
		for (EditorComponent ecom: this)
			ecom.getGraphic().setSelected( false );
		
		ecoms.clear();
	}
	
	
	
	public boolean contains( EditorComponent ecom ) {
		return ecoms.contains( ecom );
	}
	
	
	
	public boolean isEmpty() {
		return ecoms.isEmpty();
	}
	
	
	
	public int size() {
		return ecoms.size();
	}
	
	
	
	public Iterator<EditorComponent> iterator() {
		return ecoms.iterator();
	}
}













