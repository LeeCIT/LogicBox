


package logicBox.util;



/**
 * Takes two arbitrary arguments of the same type.
 * @author Lee Coakley
 */
public interface BinaryFunctor<T> {
	public T call( T a, T b );
}
