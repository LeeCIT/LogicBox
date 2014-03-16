


package logicBox.gui.editor.tools;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
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
	
	
	
	public void set( Iterable<EditorComponent> iter ) {
		clear();
		addAll( iter );
	}
	
	
	
	public EditorComponent add( EditorComponent ecom ) {
		ecoms.add( ecom );
		setGraphicSelectStates( ecoms.size() > 1 );
		return ecom;
	}
	
	
	
	public void addAll( Iterable<EditorComponent> iter ) {
		for (EditorComponent e: iter)
			add( e );
	}
	
	
	
	public boolean remove( EditorComponent ecom ) {
		ecom.getGraphic().setSelected( false );
		
		boolean rem = ecoms.remove( ecom );
		setGraphicSelectStates( ecoms.size() > 1 );
		return rem;
	}
	
	
	
	public void removeAll( Iterable<EditorComponent> iter ) {
		for (EditorComponent e: iter)
			remove( e );
	}
	
	
	
	public void clear() {
		setGraphicSelectStates( false );
		ecoms.clear();
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
	
	
	
	private void setGraphicSelectStates( boolean state ) {
		for (EditorComponent ecom: this)
			ecom.getGraphic().setSelected( state );
	}
}













