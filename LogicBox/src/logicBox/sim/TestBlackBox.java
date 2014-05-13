


package logicBox.sim;

import java.util.IdentityHashMap;
import java.util.Map;
import logicBox.sim.component.BlackBox;
import logicBox.sim.component.BlackBoxPin;
import logicBox.sim.component.connective.*;
import logicBox.sim.component.simple.*;
import logicBox.sim.component.complex.*;



/**
 * Tests the black-box functionality.
 * @author Lee Coakley
 */
public class TestBlackBox
{
	public static void main( String[] args ) {
		SourceToggle tog = new SourceToggle( false );
		BlackBox     inv = createBlackBox();
		DisplayLed   led = new DisplayLed();
		
		Simulation.connect( tog, 0, inv, 0 );
		Simulation.connect( inv, 0, led, 0 );
		
		Simulation sim = new Simulation();
		sim.add( tog, inv, led );
		
		for (int i=0; i<16; i++) {
			System.out.println( led.isLit() ? "1": "0" );
			sim.simulate();
			tog.toggleState();
			
			if (i==7) {
				inv.optimise();
				System.out.println( "Opt: " + inv.isOptimised() );
			}
		}
	}
	
	
	
	public static BlackBox createBlackBox() {
		BlackBoxPin bbPinIn  = new BlackBoxPin( true );
		GateNot     gateNot  = new GateNot();
		BlackBoxPin bbPinOut = new BlackBoxPin( false );
		
		Simulation.connect( bbPinIn, 0, gateNot,  0 );
		Simulation.connect( gateNot, 0, bbPinOut, 0 );
		
		
		Simulation bbSim = new Simulation();
		
		bbSim.add(
			bbPinIn,
			gateNot,
			bbPinOut
		);
		
		
		BlackBox bb = new BlackBox( "Inverter", bbSim, 1, 1 );
		
		Map<Pin,BlackBoxPin> bbPinMap = new IdentityHashMap<>();
		bbPinMap.put( bb.getPinInput (0), bbPinIn  );
		bbPinMap.put( bb.getPinOutput(0), bbPinOut );
		
		bb.setPinMap( bbPinMap );
		
		return bb;
	}
}
