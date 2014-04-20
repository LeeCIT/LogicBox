


package logicBox.sim.component;

import logicBox.gui.editor.Graphic;
import logicBox.gui.editor.GraphicGen;



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
	blackBoxPin; // Need IoMode and which side of the box they'll be on
	
	
	
	/**
	 * Get the default graphic used to represent this 
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
			//case displaySevenSeg:  return GraphicGen.;
			//case displayLCD:       return GraphicGen.;
			
			case decoder:          return GraphicGen.generateDecoder( 2, 4 );
			case mux:              return GraphicGen.generateMux  ( 1, 1, 2 );
			case demux:            return GraphicGen.generateDemux( 2, 1, 1 );
			
			case flipFlopD:        return GraphicGen.generateFlipFlopD();
			case flipFlopJK:       return GraphicGen.generateFlipFlopJK();
			case flipFlopT:        return GraphicGen.generateFlipFlopT();
			
			//case comparator:       return GraphicGen.;
			//case shifter:          return GraphicGen.;
			//case counter:          return GraphicGen.;
			
			//case register:         return GraphicGen.;
			//case rom:              return GraphicGen.;
			
			//case blackBox:         return GraphicGen.;
			//case blackBoxPin:      return GraphicGen.;
			
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
			
			case displayLed:       return "LED";
			case displaySevenSeg:  return "Seven-segment display";
			case displayLCD:       return "LCD";
			
			case decoder:          return "Decoder";
			case mux:              return "Multiplexer";
			case demux:            return "Demultiplexer";
			
			case flipFlopD:        return "D flip-flop";
			case flipFlopJK:       return "JK flip-flop";
			case flipFlopT:        return "T flip-flop";
			
			case comparator:       return "Comparator";
			case shifter:          return "Shifter";
			case counter:          return "Counter";
			
			case register:         return "Register";
			case rom:              return "ROM";
			
			case blackBox:         return "Black-box";
			case blackBoxPin:      return "Black-box pin";
			
			default: return "<undefined name>";
		}
	}
}






















