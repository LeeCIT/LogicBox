


package logicBox.gui.editor.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorComponentActive;
import logicBox.gui.editor.EditorComponentBlackboxPin;
import logicBox.gui.editor.SimMapper;
import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.gui.editor.graphics.GraphicPinMapping;
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
	 * @throws NoBlackBoxPinsException
	 */
	public static EditorComponentActive create( Selection sel, Vec2 pos ) {
		List<EditorComponentBlackboxPin> bbPins = new ArrayList<>();
		
		int countIn  = 0;
		int countOut = 0;
		
		for (EditorComponent ecom: sel) {
			if (ecom instanceof EditorComponentBlackboxPin) {
				EditorComponentBlackboxPin bbPin = (EditorComponentBlackboxPin) ecom;
				bbPins.add( bbPin );
				
				if (bbPin.getComponent().isComingIn())
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
		
		// Sort in spatial order
		sortLeftToRight( bbTop    );
		sortLeftToRight( bbBottom );
		sortTopToBottom( bbLeft   );
		sortTopToBottom( bbRight  );
		
		// Sort in GPM order
		bbPins.clear();
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
		bbCom.optimise();
		
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
			
			if (ecom.getComponent().isComingIn())
				angle += 180;
			
			angle %= 360.0;
			
			if (angle >= angleMin
			&&  angle <  angleMaxEx)
				list.add( ecom );
		}
		
		return list;
	}
	
	
	
	private static void sortLeftToRight( List<EditorComponentBlackboxPin> bbPins ) {
		Collections.sort( bbPins, new Comparator<EditorComponentBlackboxPin>() {
			public int compare(EditorComponentBlackboxPin a, EditorComponentBlackboxPin b) {
				boolean left = (a.getPos().x < b.getPos().x);
				return left ? -1 : +1;
			};
		});
	}
	
	
	
	private static void sortTopToBottom( List<EditorComponentBlackboxPin> bbPins ) {
		Collections.sort( bbPins, new Comparator<EditorComponentBlackboxPin>() {
			public int compare(EditorComponentBlackboxPin a, EditorComponentBlackboxPin b) {
				boolean top = (a.getPos().y < b.getPos().y);
				return top ? -1 : +1;
			};
		});
	}
	
	
	
	/**
	 * Signifies that an attempt to make a black-box failed because there were no IO pins.
	 */
	public static class NoBlackBoxPinsException extends RuntimeException {}
}






