


package logicBox.gui.editor.components;

import logicBox.gui.GUI;
import logicBox.gui.editor.OscillatorDialogue;
import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.sim.component.simple.SourceOscillator;
import logicBox.util.CallbackParam;
import logicBox.util.Vec2;



/**
 * Specialisation for oscillators.
 * @author Lee Coakley
 */
public class EditorComponentOscillator extends EditorComponentActive
{
	private static final long serialVersionUID = 1L;
	
	
	
	public EditorComponentOscillator( SourceOscillator com, GraphicComActive gca, Vec2 pos, double angle ) {
		super( com, gca, pos, angle );
	}
	
	
		
	public EditorComponentOscillator( SourceOscillator com, GraphicComActive gca, Vec2 pos ) {
		super( com, gca, pos );
	}
	
	
	
	public SourceOscillator getComponent() {
		return (SourceOscillator) super.getComponent();
	}
	
	
	
	public boolean onMouseClick( CallbackParam<String> onMod ) {
		new OscillatorDialogue( GUI.getMainFrame(), this, getWorld(), onMod );
		return false;
	}
}
