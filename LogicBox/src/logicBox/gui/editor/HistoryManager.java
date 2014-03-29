


package logicBox.gui.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Stack;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import logicBox.util.Callback;
import logicBox.util.CallbackSet;
import logicBox.util.Vec2;



/**
 * Manages compressed undo/redo states arranged on a timeline.
 * It can apply changes to the associated history listener.
 * @author Lee Coakley
 */
public class HistoryManager<T extends Serializable>
{
	private final int maxSize;
	
	private HistoryListener<T> listener;
	private Stack<byte[]>      history;
	private int                index;
	private CallbackSet        onChange;
	private CallbackSet        onUndoRedo;
	
	
	
	public HistoryManager( HistoryListener<T> listener ) {
		this.history    = new Stack<>();
		this.onChange   = new CallbackSet();
		this.onUndoRedo = new CallbackSet();
		this.listener   = listener;
		this.index      = -1;
		this.maxSize    = 512; // With compression each step is quite small.
	}
	
	
	
	public void addOnChangeCallback( Callback cb ) {
		onChange.add( cb );
	}
	
	
	
	public void removeOnChangeCallback( Callback cb ) {
		onChange.remove( cb );
	}
	
	
	
	public void addOnUndoRedoCallback( Callback cb ) {
		onUndoRedo.add( cb );
	}
	
	
	
	public void removeOnUndoRedoCallback( Callback cb ) {
		onUndoRedo.remove( cb );
	}
	
	
	
	/**
	 * Add a point to the undo/redo timeline.
	 */
	public void markChange() {
		purgeRedo();
		history.push( getStreamerState() );
		index = history.size() - 1;
		cullHistory();
		onChange.execute();
	}
	
	
	
	/**
	 * Revert state to the previous point on the timeline.
	 * The undo itself can be undone by redo(), if no further changes are made.
	 */
	public void undo() {
		if ( ! canUndo())
			throw new RuntimeException( "Can't undo: already at beginning of history." );
		
		applyStateToStreamer( --index );
	}
	
	
	
	public void redo() {
		if ( ! canRedo())
			throw new RuntimeException( "Can't redo: already at end of history." );
		
		applyStateToStreamer( ++index );
	}
	
	
	
	public boolean canRedo() {
		return index < history.size() - 1;
	}
	
	
	
	public boolean canUndo() {
		return index > 0
			&& ! history.isEmpty();
	}
	
	
	
	public String toString() {
		String str = "HistoryManager: " + history.size() + " steps, " + getTotalBytes() + " bytes\n";
		
		for (int i=0; i<history.size(); i++) {
			T obj = decompress( history.get(i) );
			str += (i) + ": \t" + obj;
			str += (i==index) ? " < now" : "";
			str += "\n";
		}
		
		return str;
	}
	
	
	
	private long getTotalBytes() {
		long acc = 0;
		
		for (byte[] item: history)
			acc += item.length;
		
		return acc;
	}
	
	
	
	private void cullHistory() {
		while (history.size() > maxSize) {
			history.remove( 0 );
			index--;
		}
	}
	
	
	
	private void purgeRedo() {
		while (history.size()-1 > index)
			history.pop();
	}
	
	
	
	private byte[] getStreamerState() {
		return compress( listener.getHistoryState() );
	}
	
	
	
	private void applyStateToStreamer( int index ) {
		listener.setStateFromHistory( decompress( history.get(index) ) );
		onUndoRedo.execute();
	}
	
	
	
	
	private byte[] compress( T historyItem ) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			GZIPOutputStream      zipStream  = new GZIPOutputStream( byteStream );
			ObjectOutputStream    objStream  = new ObjectOutputStream( zipStream  );
			
			objStream.writeObject( historyItem );
			
			objStream .close();
			zipStream .close();
			byteStream.close();
			
			return byteStream.toByteArray();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	private T decompress( byte[] compressed ) {
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream( compressed );
			GZIPInputStream      zipStream  = new GZIPInputStream( byteStream );
			ObjectInputStream    objStream  = new ObjectInputStream( zipStream  );
			
			T obj = (T) objStream.readObject();
			
			objStream .close();
			zipStream .close();
			byteStream.close();
			
			return obj;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	
	
	private void debugPrint() {
		System.out.println( this );
	}
	
	
	
	
	
	public static void main( String[] args ) {
		final Vec2 v = new Vec2(0,0);
		
		HistoryListener<Vec2> streamer = new HistoryListener<Vec2>() {
			public void setStateFromHistory( Vec2 object ) {
				v.setLocation( object );
			}
			
			public Vec2 getHistoryState() {
				return v;
			}
		};
		
		HistoryManager<Vec2> stream = new HistoryManager<>( streamer );
		stream.markChange();
		stream.debugPrint();
		
		
		System.out.println( "MARK" );
		v.setLocation( new Vec2(-1,+1) );
		stream.markChange();
		stream.debugPrint();
		
		
		System.out.println( "UNDO" );
		stream.undo();
		stream.debugPrint();
		
		
		System.out.println( "REDO" );
		stream.redo();
		stream.debugPrint();
		
		
		System.out.println( "MARK" );
		v.setLocation( new Vec2(999,999) );
		stream.markChange();
		stream.debugPrint();
		
		System.out.println( "UNDO" );
		stream.undo();
		stream.debugPrint();
		
		
		System.out.println( "MARK" );
		v.setLocation( new Vec2(123) );
		stream.markChange();
		stream.debugPrint();
		
		
		System.out.println( "MARK" );
		v.setLocation( new Vec2(456) );
		stream.markChange();
		stream.debugPrint();
		
		
		System.out.println( "MARK" );
		v.setLocation( new Vec2(789) );
		stream.markChange();
		stream.debugPrint();
		
		
		System.out.println( "UNDO" );
		stream.undo();		
		stream.debugPrint();
		
		
		System.out.println( "UNDO" );
		stream.undo();		
		stream.debugPrint();
		
		
		System.out.println( "UNDO" );
		stream.undo();		
		stream.debugPrint();
		
		
		System.out.println( "MARK" );
		v.setLocation( new Vec2(363) );
		stream.markChange();
		stream.debugPrint();
	}
}




















