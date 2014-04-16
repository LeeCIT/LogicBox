


package logicBox.gui.editor;

import logicBox.sim.component.Pin;
import logicBox.sim.component.PinIoMode;
 


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
		int index = gpm.index;
		
		if (gpm.mode == PinIoMode.input)
			 return ecom.getComponent().getPinInput ( index );
		else return ecom.getComponent().getPinOutput( index );
	}
}
