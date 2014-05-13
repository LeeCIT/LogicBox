


package logicBox.gui.editor.controllers;

import logicBox.gui.editor.components.EditorComponentActive;
import logicBox.gui.editor.graphics.GraphicPinMapping;
import logicBox.sim.component.connective.Pin;
 


/**
 * Maps GUI stuff onto the simulator.
 * @author Lee Coakley
 */
public abstract class SimMapper
{
	/**
	 * Get the sim-level pin object modelled by the graphic.
	 */
	public static Pin getMappedPin( EditorComponentActive ecom, GraphicPinMapping gpm ) {		
		return ecom.getComponent().getPin( gpm.mode, gpm.index );
	}
}
