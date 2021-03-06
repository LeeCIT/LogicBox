


package logicBox.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;



/**
 * Generic utility functions.
 */
public abstract class Util
{
	public static <T> Set<T> createIdentityHashSet() {
		return Collections.newSetFromMap( new IdentityHashMap<T,Boolean>() );
	}
	
	
	
	public static <T> Set<T> createIdentityHashSet( Collection<? extends T> addThese ) {
		Set<T> set = createIdentityHashSet();
		set.addAll( addThese );
		return set;
	}
	
	
	
	/**
	 * Deep copy a serialisable object using loopback serialization.
	 * This is a lot easier than making tons of classes cloneable.
	 * It's probably not very fast, but this won't be frequently called.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deepCopy( T object ) {
		try {
			ByteArrayOutputStream osByte = new ByteArrayOutputStream();
			ObjectOutputStream    osObj  = new ObjectOutputStream( osByte );
			
			osObj.writeObject( object );
			
			ByteArrayInputStream isByte = new ByteArrayInputStream( osByte.toByteArray() );
			ObjectInputStream    isObj  = new ObjectInputStream( isByte );
			
			osByte.close();
			isByte.close();
			osObj .close();
			isObj .close();
			
			return (T) isObj.readObject();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	
	
	/**
	 * Serialise an object.
	 * If it fails the returned array will be null.
	 */
	public static byte[] serialize( Serializable ser ) {
		try {
			ByteArrayOutputStream osByte = new ByteArrayOutputStream();
			ObjectOutputStream    osObj  = new ObjectOutputStream( osByte );
			
			osObj.writeObject( ser );
			byte[] bytes = osByte.toByteArray();
			
			osByte.close();
			osObj .close();
			
			return bytes;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	
	
	/**
	 * Deserialise a byte array back to an object.
	 * If it fails the returned object will be null.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deserialize( byte[] bytes ) {
		try {
			ByteArrayInputStream isByte = new ByteArrayInputStream( bytes );
			ObjectInputStream    isObj  = new ObjectInputStream( isByte );
			
			isByte.close();
			isObj .close();
			
			return (T) isObj.readObject();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}







