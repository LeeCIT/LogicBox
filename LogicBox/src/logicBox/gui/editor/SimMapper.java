


package logicBox.gui.editor;

import logicBox.sim.component.Pin;
 


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
