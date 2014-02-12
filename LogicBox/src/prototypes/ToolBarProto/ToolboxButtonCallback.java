package prototypes.ToolBarProto;

import javax.swing.*;

import logicBox.util.Callback;

/**
 * Create a button and a callback
 * A list can be added to the toolbox.
 * A list can also be added to the panel constructor
 * @author John
 *
 */
public class ToolboxButtonCallback
{
	
	private JButton  button;
	private Callback callback;
	
	
	
	public ToolboxButtonCallback(JButton button, Callback callback) {
		super();
		this.button = button;
		this.callback = callback;
	}



	public JButton getButton() {
		return button;
	}


	public void setButton(JButton button) {
		this.button = button;
	}


	public Callback getCallback() {
		return callback;
	}


	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	
	
	

	
	
	
	
}
