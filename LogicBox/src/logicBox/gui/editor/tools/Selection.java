


package logicBox.gui.editor.tools;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorWorld;
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
	public Vec2   pos;
	public double angle;
	
	
	
	public Selection() {
		ecoms = Util.createIdentityHashSet();
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
		
		for (EditorComponent ecom: ecoms)
			ecom.setPos( ecom.getPos().add(delta) );
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
	
	
	
	public EditorComponent add( EditorComponent ecom ) {
		ecoms.add( ecom );
		return ecom;
	}
	
	
	
	public boolean remove( EditorComponent ecom ) {
		return ecoms.remove( ecom );
	}
	
	
	
	public void clear() {
		ecoms.clear();
	}
	
	
	
	public Iterator<EditorComponent> iterator() {
		return ecoms.iterator();
	}
}
