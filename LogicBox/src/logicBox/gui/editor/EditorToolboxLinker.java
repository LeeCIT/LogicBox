


package logicBox.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import logicBox.sim.component.*;



/**
 * Interfaces an EditorPanel with a Toolbox.
 * The expected usage is multiple EditorPanels linked to a single Toolbox.
 * @author Lee Coakley
 */
public abstract class EditorToolboxLinker
{
	private static Map<ComponentType,ComponentActive>       mapComp    = genMapComp();
	private static Map<ComponentType,ToolboxButton>         mapButton  = genMapButton();
	//private static Map<ComponentType,EditorCreationCommand> mapCommand = genMapCommand();
	
	
	
	public static Toolbox createLinkedToolbox( EditorPanel ed ) {
		Toolbox box = new Toolbox();
		link( box );
		box.setActiveEditorPanel( ed );
		return box;
	}
	
	
	
	public static void link( final Toolbox box ) {
		final ToolboxButton butt = mapButton.get( ComponentType.gateBuffer );
		
		butt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {		
				EditorPanel ed = butt.getEditorPanelEvaluator().evaluate();
				ed.initiateComponentCreation( genCommand( new GateBuffer()) );
			}
		});
		
		box.addCategory( "Gates", butt );
	}
	
	
	
	private static EditorCreationCommand genCommand( ComponentActive com ) {
		return new EditorCreationCommand( com, com.getGraphic() );
	}
	
	
	
	public static void unlink( EditorPanel ed, Toolbox box ) {
		
	}
	
	
	
	private static Map<ComponentType,ToolboxButton> genMapButton() {
		Map<ComponentType,ToolboxButton> map = new HashMap<>();
		
		for (ComponentType type: ComponentType.values()) {
			if (mapComp.containsKey( type )) {
				ComponentActive comp   = mapComp.get( type );
				ToolboxButton   button = new ToolboxButton( comp.getGraphic(), comp.getName(), null );
				map.put( type, button );
			}
		}
		
		return map;
	}
	
	
	
	// These are the components used as references for the button graphics.
	private static Map<ComponentType,ComponentActive> genMapComp() {
		Map<ComponentType,ComponentActive> map = new HashMap<>();
		
		map.put( ComponentType.gateBuffer, new GateBuffer() );
		map.put( ComponentType.gateNot   , new GateNot   () );
		map.put( ComponentType.gateAnd   , new GateAnd   () );
		map.put( ComponentType.gateNand  , new GateNand  () );
		map.put( ComponentType.gateOr    , new GateOr    () );
		map.put( ComponentType.gateNor   , new GateNor   () );
		map.put( ComponentType.gateXor   , new GateXor   () );
		map.put( ComponentType.gateXnor  , new GateXnor  () );
		
		return map;
	}
}


































