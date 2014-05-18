


package logicBox.gui.editor.controllers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Set;
import logicBox.gui.editor.components.EditorComponent;
import logicBox.gui.editor.tools.Selection;
import logicBox.sim.Simulation;
import logicBox.sim.component.Component;
import logicBox.util.Util;



/**
 * The global clipboard.
 * @author Lee Coakley
 */
public abstract class EditorClipboard
{
	private static TransferOwner transOwner = new TransferOwner();
	
	
	
	/**
	 * Set the current clipboard selection.
	 * There is no need to modify it in any way, this is all done internally.
	 */
	public static void set( Selection sel ) {
		if (sel.isEmpty()) {
			clear();
			return;
		}
		
		Selection selCopy = Util.deepCopy( sel );
		
		for (EditorComponent com: selCopy.ecoms) {
			com.unlinkFromWorld();
			com.getComponent().reset();
		}
		
		isolateSelection( selCopy );
		
		byte[]   raw   = Util.serialize( selCopy );
		Transfer trans = new Transfer( raw );
		transOwner.setContents( trans );
	}
	
	
	
	/**
	 * Get a copy of the clipboard selection.
	 */
	public static Selection get() {
		byte[]    raw = transOwner.getContents();
		Selection sel = Util.deserialize( raw );
		
		return sel;
	}
	
	
	
	public static void clear() {
		Selection blank = new Selection();
		byte[]    raw   = Util.serialize( blank );
		Transfer  trans = new Transfer( raw );
		transOwner.setContents( trans );
	}
	
	
	
	public static boolean isEmpty() {
		try {
			Selection sel = get();
			
			return sel == null
				|| sel.isEmpty();
		}
		catch (Exception ex) {
			return true;
		}
	}
	
	
	
	private static void isolateSelection( Selection sel ) {
		Set<Component> coms = Util.createIdentityHashSet();
		
		for (EditorComponent ecom: sel)
			coms.add( ecom.getComponent() );
		
		Simulation.isolate( coms );
	}
	
	
	
	
	
	private static class TransferOwner implements ClipboardOwner
	{
		private   static final Clipboard  clip   = Toolkit.getDefaultToolkit().getSystemClipboard();
		protected static final DataFlavor flavor = new DataFlavor( byte[].class, "lbx/selection" );
		
		
		
		public void lostOwnership( Clipboard clipboard, Transferable contents ) {
			// ???
		}
		
		
		
		public void setContents( Transfer trans ) {
			clip.setContents( trans, this );
		}
		
		
		
		public byte[] getContents() {
			Clipboard    clip  = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable trans = clip.getContents( null );
			
			try {
				return (byte[]) trans.getTransferData( flavor );
			}
			catch (IOException | UnsupportedFlavorException ex) {
				return null;
			}
		}
	}
	
	
	
	
	
	private static class Transfer implements Transferable
	{
		private byte[] contents; 
		
		
		
		public Transfer( byte[] contents ) {
			this.contents = contents;
		}
		
		
		
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { TransferOwner.flavor };
		}
		
		
		
		public boolean isDataFlavorSupported( DataFlavor flavor ) {
			return flavor.isMimeTypeEqual( TransferOwner.flavor );
		}
		
		
		
		public Object getTransferData( DataFlavor flavor ) throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported( flavor ))
				return contents;
			else throw new UnsupportedFlavorException( flavor );
		}
	}
}














