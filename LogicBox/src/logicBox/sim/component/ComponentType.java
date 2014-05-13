


package logicBox.sim.component;

import java.util.Scanner;
import logicBox.gui.editor.graphics.Graphic;
import logicBox.gui.editor.graphics.GraphicGen;



/**
 * Types of components the simulation implements.
 * @author Lee Coakley
 */
public enum ComponentType
{
	gateBuffer,
	gateNot,
	gateAnd,
	gateNand,
	gateOr,
	gateNor,
	gateXor,
	gateXnor,
	
	sourceFixed,
	sourceToggle,
	sourceOscillator,
	
	junction,
	trace,
	pin,
	
	displayLed,
	displaySevenSeg,
	displayLCD,
	
	encoder,
	decoder,
	mux,
	demux,
	
	flipFlopD,
	flipFlopJK,
	flipFlopT,
	
	comparator,
	shifter,
	counter,
	
	register,
	rom,
	
	blackBox,
	blackBoxPin;
	
	
	
	/**
	 * Get the default graphic used to represent this type.
	 * Intended for use with GraphicPanel
	 */
	public Graphic getGraphic() {
		switch (this) {
			case gateBuffer:       return GraphicGen.generateGateBuffer();
			case gateNot:          return GraphicGen.generateGateNot();
			case gateAnd:          return GraphicGen.generateGateAnd ( 2 );
			case gateNand:         return GraphicGen.generateGateNand( 2 );
			case gateOr:           return GraphicGen.generateGateOr  ( 2 );
			case gateNor:          return GraphicGen.generateGateNor ( 2 );
			case gateXor:          return GraphicGen.generateGateXor ( 2 );
			case gateXnor:         return GraphicGen.generateGateXnor( 2 );
			
			case sourceFixed:      return GraphicGen.generateSourceFixed( true );
			case sourceToggle:     return GraphicGen.generateSourceToggle();
			case sourceOscillator: return GraphicGen.generateSourceOscillator();
			
			case junction:         return GraphicGen.generateJunction();
			case trace:            return GraphicGen.generateTrace();
			
			case displayLed:       return GraphicGen.generateDisplayLed();
			case displaySevenSeg:  return GraphicGen.generateDisplaySevenSeg();
			//case displayLCD:       return GraphicGen.;
			
			case encoder:          return GraphicGen.generateEncoder( 4, 2 );
			case decoder:          return GraphicGen.generateDecoder( 2, 4 );
			case mux:              return GraphicGen.generateMux  ( 1, 1, 2 );
			case demux:            return GraphicGen.generateDemux( 2, 1, 1 );
			
			case flipFlopD:        return GraphicGen.generateFlipFlopD();
			case flipFlopJK:       return GraphicGen.generateFlipFlopJK();
			case flipFlopT:        return GraphicGen.generateFlipFlopT();
			
			case comparator:       return GraphicGen.generateComparator( 4 );
			case shifter:          return GraphicGen.generateShifter( 4, 2 );
			case counter:          return GraphicGen.generateCounter( 4 );
			
			case register:         return GraphicGen.generateRegister( 4 );
			//case rom:              return GraphicGen.;
			
			case blackBox:         return GraphicGen.generateGeneric( 3, null, 3, null, 3, null, 3, null );
			case blackBoxPin:      return GraphicGen.generateBlackboxPin( false );
			
			default: return GraphicGen.generatePlaceholder();
		}
	}
	
	
	
	public String getName() {
		switch (this) {
			case gateBuffer:       return "Buffer";
			case gateNot:          return "NOT gate";
			case gateAnd:          return "AND gate";
			case gateNand:         return "NAND gate";
			case gateOr:           return "OR gate";
			case gateNor:          return "NOR gate";
			case gateXor:          return "XOR gate";
			case gateXnor:         return "XNOR gate";
			
			case sourceFixed:      return "Fixed source";
			case sourceToggle:     return "Switchable source";
			case sourceOscillator: return "Oscillator";
			
			case junction:         return "Junction";
			case trace:            return "Trace";
			
			case displayLed:       return "Light";
			case displaySevenSeg:  return "Seven-segment display";
			case displayLCD:       return "LCD";
			
			case encoder:          return "Priority Encoder";
			case decoder:          return "Decoder";
			case mux:              return "Multiplexer";
			case demux:            return "Demultiplexer";
			
			case flipFlopD:        return "D flip-flop";
			case flipFlopJK:       return "JK flip-flop";
			case flipFlopT:        return "T flip-flop";
			
			case comparator:       return "Comparator";
			case shifter:          return "Shifter";
			case counter:          return "Up Counter";
			
			case register:         return "Register";
			case rom:              return "ROM";
			
			case blackBox:         return "Black-box";
			case blackBoxPin:      return "Black-box pin";
			
			default: return name();
		}
	}
	
	
	
	public String getDescription() {
		String details;
	
    	try {
    		details = new Scanner(
				ComponentType.class.getClassLoader()
				.getResourceAsStream("resources/help/" + name() + ".txt"), "UTF-8"
			)
			.useDelimiter("\\A")
			.next();
    	}
    	catch(Exception e) {
    		details = "No description for this component yet";
    	}
    
		return details;
	}
	
	
	
	public String toString() {
		return getName();
	}
}






















