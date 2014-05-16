


package logicBox.gui.editor.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;
import java.util.Map;
import logicBox.gui.editor.components.*;
import logicBox.gui.editor.graphics.*;
import logicBox.sim.component.complex.*;
import logicBox.sim.component.connective.*;
import logicBox.sim.component.memory.*;
import logicBox.sim.component.simple.*;
import logicBox.util.StringUtil;



/**
 * Maps old class names to new ones, enabling backwards compatibility when refactoring.
 * I can have my cake and eat it too... with horrible hacks! :D
 * 
 * Another alternative would be to manually rewrite the serialised data at the byte level.
 * The name format is fairly simple: 2-byte BE offset, then name bytes
 * 
 * @author Lee Coakley
 */
public class RemappableObjectInputStream extends ObjectInputStream
{
	private static Map<String,Class<?>> classMap;
	
	
	
	public RemappableObjectInputStream( InputStream in ) throws IOException {
		super( in );
	}
	
	
	
	protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        if (classMap == null)
        	classMap = genClassMap();
		
		ObjectStreamClass classDesc = super.readClassDescriptor();
        
        String   sourceName  = classDesc.getName();
        Class<?> mappedClass = classMap.get( sourceName );
        
        if (mappedClass != null)
            classDesc = ObjectStreamClass.lookup( mappedClass );

        return classDesc;
    }
	
	
	
	private static Map<String,Class<?>> genClassMap() throws ClassNotFoundException {
		Map<String,Class<?>> map = new HashMap<>();
		
		addSimComponents( map );
		addEdComponents( map );		
		debugMap( map, true );
		
		return map;
	}
	
	
	
	private static void debugMap( Map<String, Class<?>> map, boolean enable ) {
		if ( ! enable)
			return;
		
		String align = " -> ";
		String str   = "";
		
		for (Map.Entry<String,Class<?>> en: map.entrySet()) {
			str += en.getKey() + align;
			str += en.getValue().getCanonicalName() + "\n";
		}
		
		str = StringUtil.align( str, align );
		
		System.out.println( "Map:\n\n" + str );
	}
	
	
	
	private static void add( Map<String,Class<?>> map, String oldPackage, Class<?>...classes ) throws ClassNotFoundException {
		for (Class<?> c: classes) {
			String oldName = oldPackage + "." + nameOf( c );
			map.put( oldName, c );
		}
	}
	
	
	
	private static String nameOf( Class<?> c ) {
		return c.getCanonicalName().replaceFirst( "(\\w+\\.)+", "" );
	}
	
	
	
	private static void addSimComponents( Map<String,Class<?>> map ) throws ClassNotFoundException {
		add( map, "logicBox.sim.component",
			Comparator     .class,
			Decoder        .class,
			Demux          .class,
			Display        .class,
			DisplayLcd     .class,
			DisplayLed     .class,
			DisplaySevenSeg.class,
			Encoder        .class,
			Mux            .class,
			Plexer         .class,

			Junction.class,
			Pin     .class,
			Trace   .class,

			Counter      .class,
			EdgeTriggered.class,
			FlipFlop     .class,
			FlipFlopD    .class,
			FlipFlopJK   .class,
			FlipFlopT    .class,
			Register     .class,

			Gate            .class,
			GateAnd         .class,
			GateBuffer      .class,
			GateNand        .class,
			GateNor         .class,
			GateNot         .class,
			GateOr          .class,
			GateXnor        .class,
			GateXor         .class,
			Source          .class,
			SourceFixed     .class,
			SourceOscillator.class,
			SourceToggle    .class
		);
	}
	
	
	
	private static void addEdComponents( Map<String, Class<?>> map ) throws ClassNotFoundException {
		add( map, "logicBox.gui.editor",
			EditorComponent           .class,
			EditorComponentActive     .class,
			EditorComponentBlackboxPin.class,
			EditorComponentJunction   .class,
			EditorComponentLed        .class,
			EditorComponentOscillator .class,
			EditorComponentSevenSeg   .class,
			EditorComponentToggle     .class,
			EditorComponentTrace      .class,

			Drawable          .class,
			Graphic           .class,
			GraphicComActive  .class,
			GraphicComment    .class,
			GraphicGen        .class,
			GraphicIntersector.class,
			GraphicJunction   .class,
			GraphicPinMapping .class,
			GraphicSevenSeg   .class,
			GraphicTrace      .class,
			GraphicTransHint  .class,
			RepaintListener   .class
		);
	}
}




































