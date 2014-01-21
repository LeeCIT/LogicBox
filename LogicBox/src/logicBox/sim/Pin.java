


package logicBox.sim;



/**
 * A pin represents the attachment points on a component.
 * @author Lee Coakley
 */
public class Pin extends Component
{
	protected Component partOf;
	protected Component connectedTo;
	protected boolean   isInput;
}
