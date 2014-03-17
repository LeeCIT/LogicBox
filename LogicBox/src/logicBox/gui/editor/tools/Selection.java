


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
	
	public Set<EditorComponent> ecoms;
	public double angle;
	
	
	
	public Selection() {
		ecoms = Util.createIdentityHashSet();
		angle = 0;
	}
	
	
	
	public Vec2 getCentre() {
		return EditorWorld.getExtent( ecoms ).getCentre();
	}
	
	
	
	public Vec2 getPos() {
		return getCentre();
	}
	
	
	
	/**
	 * Set the centre positon of the selection.
	 */
	public void setPos( Vec2 pos ) {
		Vec2 centre = getCentre();
		Vec2 delta  = pos.subtract( centre );
		
		for (EditorComponent ecom: ecoms) {
			Vec2 newPos = ecom.getPos().add(delta);
			ecom.setPos( Geo.snapNear(newPos, 16) );
		}
	}
	
	
	
	public double getAngle() {
		return 0;
	}
	
	
	
	/**
	 * Rotate the selection to the given angle.
	 * This moves and rotates the ecoms around the centre.
	 */
	public void setAngle( double angle ) {
		System.out.println( "setangle" );
	}
	
	
	
	public Selection copy() { // TODO it ain't quite this simple...
		return Util.deepCopy( this );
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













