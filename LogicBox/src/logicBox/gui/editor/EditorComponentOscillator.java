


package logicBox.gui.editor;

import logicBox.sim.component.SourceOscillator;
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
	
	
	
	public void onMouseClick() {
		System.out.println( "osc menu here" ); // TODO make osc menu
	}
}
