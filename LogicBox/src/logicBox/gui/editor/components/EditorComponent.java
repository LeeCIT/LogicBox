


package logicBox.gui.editor.components;
import java.io.Serializable;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.controllers.EditorCreationParam;
import logicBox.gui.editor.graphics.Graphic;
import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicPinMapping;
import logicBox.sim.component.BlackBoxPin;
import logicBox.sim.component.Component;
import logicBox.sim.component.ComponentActive;
import logicBox.sim.component.complex.DisplayLed;
import logicBox.sim.component.complex.DisplaySevenSeg;
import logicBox.sim.component.simple.SourceOscillator;
import logicBox.sim.component.simple.SourceToggle;
import logicBox.util.CallbackParam;
import logicBox.util.Vec2;



/**
 * An object which can be manipulated by the editor.
 * @author Lee Coakley
 */
public abstract class EditorComponent implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Component   com;
	private EditorWorld world;
	
	
	
	protected EditorComponent( Component com ) {
		this.com = com;
	}
	
	
	
	public static EditorComponent create( ComponentActive scom, GraphicComActive gca, EditorCreationParam param ) {
	         if (scom instanceof DisplayLed)       return new EditorComponentLed        ( (DisplayLed)       scom, gca, param.pos, param.angle );
	    else if (scom instanceof DisplaySevenSeg)  return new EditorComponentSevenSeg   ( (DisplaySevenSeg)  scom,      param.pos, param.angle );
		else if (scom instanceof SourceOscillator) return new EditorComponentOscillator ( (SourceOscillator) scom, gca, param.pos, param.angle );
		else if (scom instanceof BlackBoxPin)      return new EditorComponentBlackboxPin( (BlackBoxPin)      scom, gca, param.pos, param.angle );
		else if (scom instanceof SourceToggle)     return new EditorComponentToggle     ( (SourceToggle)     scom, gca, param.pos, param.angle );
		else                                       return new EditorComponentActive     (                    scom, gca, param.pos, param.angle );
	}
	
	
	
	/**
	 * Perform some action in response to being clicked on with the left mouse button.
	 * onMod is called if the action would cause a sim change (undo/redo)
	 * Returns whether the sim changed as a result.
	 */
	public boolean onMouseClick( CallbackParam<String> onMod ) {
		synchronized (world) {
			if (getComponent().interactClick()) {
				world.simUpdate();
				return true;
			} else {
				return false;
			}
		}
	}
	
	
	
	/**
	 * Do something if the world state changes (deleted component or whatever).
	 * Do not modify the world in this method.
	 */
	public void onWorldChange() {
		// Default impl does nothing
	}
	
	
	
	public abstract void setPos( Vec2 pos );
	public abstract Vec2 getPos();
	
	public abstract void   setAngle( double angle );
	public abstract double getAngle();
	
	
	
	/**
	 * Get the simulation component represented by this instance.
	 */
	public Component getComponent() {
		return com;
	}
	
	
	
	/**
	 * Get the name of the component represented by this object.
	 */
	public String getComponentName() {
		return com.getName();
	}
	
	
	
	/**
	 * Get the graphical representation of the component.
	 * Use return type covariance to return the appropriate graphic type.
	 */
	public abstract Graphic getGraphic();
	
	
	
	/**
	 * Find the pin nearest to POS within the given radius.
	 * Returns null if no pins meet the criterion.
	 */
	public abstract GraphicPinMapping findPinNear( Vec2 pos, double radius );
	
	
	
	/**
	 * Link with a world.  Only one world can be linked at any one time. 
	 */
	public void linkToWorld( EditorWorld world ) {
		this.world = world;
	}
	
	
	
	public void unlinkFromWorld() {
		this.world = null;
	}
	
	
	
	protected EditorWorld getWorld() {
		return world;
	}
	
	
	
	protected void signalTransformChange() {
		if (world != null)
			world.onComponentTransform( this );
	}
	
	
	
	protected boolean getWorldPowerState() {
		if (world == null)
			return false;
		
		return world.isPowerOn();
	}
}
