


package logicBox.gui.editor;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.tools.Selection;
import logicBox.sim.Simulation;
import logicBox.sim.component.BlackBox;
import logicBox.sim.component.PinIoMode;
import logicBox.util.Vec2;



/**
 * Handles creating black-boxes out of selections.
 * @author Lee Coakley
 */
public abstract class BlackBoxCreator
{
	/**
	 * Create the black-box.  Selection should come from the clipboard (isolated and reset).
	 */
	public static EditorComponentActive create( Selection sel, Vec2 pos ) {
		List<EditorComponentBlackBoxPin> bbPins = new ArrayList<>();
		
		int countIn  = 0;
		int countOut = 0;
		
		for (EditorComponent ecom: sel) {
			if (ecom instanceof EditorComponentBlackBoxPin) {
				EditorComponentBlackBoxPin bbPin = (EditorComponentBlackBoxPin) ecom;
				bbPins.add( bbPin );
				
				if (bbPin.getComponent().isInput())
					 countIn++;
				else countOut++; 
			}
		}
		
		if (bbPins.isEmpty())
			throw new NoBlackBoxPinsException();
		
		List<EditorComponentBlackBoxPin> bbLeft   = findFacing( bbPins, 180-45, 180+45 );
		List<EditorComponentBlackBoxPin> bbRight  = findFacing( bbPins,   0-45,   0+45 );
		List<EditorComponentBlackBoxPin> bbTop    = findFacing( bbPins,  90-45,  90+45 );
		List<EditorComponentBlackBoxPin> bbBottom = findFacing( bbPins, 270-45, 270+45 );
		
		Simulation sim = new Simulation();
		for (EditorComponent ecom: sel)
			sim.add( ecom.getComponent() );
		
		BlackBox bbCom = new BlackBox( "Selection Black-Box", sim, countIn, countOut );
		
		GraphicComActive graphic = GraphicGen.generateBlackBox(
			toModes( bbLeft   ),
			toModes( bbRight  ),
			toModes( bbTop    ),
			toModes( bbBottom )
		);
		
		return new EditorComponentActive( bbCom, graphic, pos );
	}
	
	
	
	private static List<PinIoMode> toModes( List<EditorComponentBlackBoxPin> bbPins ) {
		List<PinIoMode> modes = new ArrayList<>();
		
		for (EditorComponentBlackBoxPin bbPin: bbPins)
			modes.add( bbPin.getComponent().getEquivalentPinIoMode() );
		
		return modes;
	}
	
	
	
	private static List<EditorComponentBlackBoxPin> findFacing( Iterable<EditorComponentBlackBoxPin> ecoms, double angleMin, double angleMaxEx ) {
		List<EditorComponentBlackBoxPin> list = new ArrayList<>();
		
		for (EditorComponentBlackBoxPin ecom: ecoms) {
			double angle = ecom.getAngle();
			
			if (ecom.getComponent().isOutput())
				angle += 180;
			
			angle %= 360.0;
			
			if (angle >= angleMin
			||  angle <  angleMaxEx)
				list.add( ecom );
		}
		
		return list;
	}
	
	
	
	public static class NoBlackBoxPinsException extends RuntimeException {}
}






