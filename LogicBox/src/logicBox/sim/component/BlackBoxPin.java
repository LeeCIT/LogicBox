


package logicBox.sim.component;



/**
 * A special type of pin which connects a black-box to the outside world.
 * TODO The way the sim works, this really has to be an active component with a graphic.
 * @author Lee Coakley
 */
public class BlackBoxPin extends Pin
{
	public BlackBoxPin( PinIoMode mode ) {
		super( null, mode );
	}
	
	
	
	public Component getAttachedComponent() {
		throw new UnsupportedOperationException();
	}
}
