


package logicBox.util;



/**
 * Interface that takes two arbitrary arguments of the same type.
 * @author Lee Coakley
 */
public interface BinaryFunctor<T>
{
	public void call( T a, T b );
}
