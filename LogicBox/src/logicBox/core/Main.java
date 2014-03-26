


package logicBox.core;

import logicBox.gui.GUI;



/**
 * Program entrypoint.  This where it all begins.
 * @author Lee Coakley
 * @author John Murphy
 */
public abstract class Main
{
	public static void main(String[] args) {
		GUI.setNativeStyle();
		GUI.create();
	}
	
	
	
	public static void onStartup() {
		// TODO load settings and stuff.
	}
	
	
	
	public static void onShutdown() {
		// TODO Save settings, log out, etc
	}
}
