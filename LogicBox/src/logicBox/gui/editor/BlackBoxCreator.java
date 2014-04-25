


package logicBox.gui.editor;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import logicBox.gui.editor.tools.Selection;
import logicBox.sim.Simulation;
import logicBox.sim.component.BlackBox;
import logicBox.sim.component.BlackBoxPin;
import logicBox.sim.component.Pin;
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
		List<EditorComponentBlackboxPin> bbPins = new ArrayList<>();
		
		int countIn  = 0;
		int countOut = 0;
		
		for (EditorComponent ecom: sel) {
			if (ecom instanceof EditorComponentBlackboxPin) {
				EditorComponentBlackboxPin bbPin = (EditorComponentBlackboxPin) ecom;
				bbPins.add( bbPin );
				
				if (bbPin.getComponent().isInput())
					 countIn++;
				else countOut++; 
			}
		}
		
		if (bbPins.isEmpty())
			throw new NoBlackBoxPinsException();
		
		
		List<EditorComponentBlackboxPin> bbLeft   = findFacing( bbPins, 180-45, 180+45 );
		List<EditorComponentBlackboxPin> bbRight  = findFacing( bbPins,   0-45,   0+45 );
		List<EditorComponentBlackboxPin> bbTop    = findFacing( bbPins,  90-45,  90+45 );
		List<EditorComponentBlackboxPin> bbBottom = findFacing( bbPins, 270-45, 270+45 );
		
		bbPins.clear(); // Sort in GPM order
		bbPins.addAll( bbLeft   );
		bbPins.addAll( bbRight  );
		bbPins.addAll( bbTop    );
		bbPins.addAll( bbBottom );
		
		
		Simulation sim = new Simulation();
		for (EditorComponent ecom: sel)
			sim.add( ecom.getComponent() );
		
		GraphicComActive graphic = GraphicGen.generateBlackBox(
			toModes( bbLeft   ),
			toModes( bbRight  ),
			toModes( bbTop    ),
			toModes( bbBottom )
		);
		
		String   name  = JOptionPane.showInputDialog( "Enter name" );
		BlackBox bbCom = new BlackBox( name, sim, countIn, countOut );
		bbCom.setGraphic( graphic );
		
		
		EditorComponentActive ecom = new EditorComponentActive( bbCom, graphic, pos );
		
		
		Map<Pin,BlackBoxPin>    bbMap = new IdentityHashMap<>();
		List<GraphicPinMapping> gpms  = graphic.getGraphicPinMappings();
		
		for (int i=0; i<gpms.size(); i++) {
			GraphicPinMapping gpm = gpms.get( i );
			bbMap.put( SimMapper.getMappedPin(ecom,gpm), bbPins.get(i).getComponent() );
		}
		
		bbCom.setPinMap( bbMap );
		
		return ecom;
	}
	
	
	
	private static List<PinIoMode> toModes( List<EditorComponentBlackboxPin> bbPins ) {
		List<PinIoMode> modes = new ArrayList<>();
		
		for (EditorComponentBlackboxPin bbPin: bbPins)
			modes.add( bbPin.getComponent().getEquivalentPinIoMode() );
		
		return modes;
	}
	
	
	
	private static List<EditorComponentBlackboxPin> findFacing( Iterable<EditorComponentBlackboxPin> ecoms, double angleMin, double angleMaxEx ) {
		List<EditorComponentBlackboxPin> list = new ArrayList<>();
		
		for (EditorComponentBlackboxPin ecom: ecoms) {
			double angle = ecom.getAngle();
			
			if (ecom.getComponent().isInput())
				angle += 180;
			
			angle %= 360.0;
			
			if (angle >= angleMin
			&&  angle <  angleMaxEx)
				list.add( ecom );
		}
		
		return list;
	}
	
	
	
	public static class NoBlackBoxPinsException extends RuntimeException {}
}






