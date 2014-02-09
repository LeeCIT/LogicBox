package prototypes.frameShareProto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import prototypes.frameShareProto.ToolSelectionEnum.ToolSelection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ToolBar extends JFrame{
	
	public static ToolBar currentInstanceTool;
	
	//Add some buttons
	private JButton andGate;
	private JButton orGate;
	private JButton xorGate;
	private JButton bulb;
	private JButton oscillator;
	private JPanel buttonHolder;
	
	//The current Selection
	public static ToolSelection selection;
	
	public ToolBar() {
		
		andGate 	 = new JButton("And Gate");
		orGate 		 = new JButton("Or Gate");
		xorGate 	 = new JButton("xor Gate");
		bulb 		 = new JButton("Bulb");
		oscillator   = new JButton("oscillator");
		buttonHolder = new JPanel();
		
		//Assigns the selection to the button
		setupButtonActions();
		
		//Add gates to toolbar panel
		buttonHolder.add(andGate);
		buttonHolder.add(orGate);
		buttonHolder.add(xorGate);
		buttonHolder.add(bulb);
		buttonHolder.add(oscillator);
		
		add(buttonHolder);
		
		setTitle("Demo ToolBar");
		setSize(200, 600);
		setVisible(true);
		setLocationRelativeTo(null);//Loads the window in the centre	
	}
	
	
	
	
	/**
	 * Returns the selected item on the toolbar
	 */
	public ToolSelection getSelectedToolBarItem() { 
		return selection;
	}
	
	
	
	/**
	 * Assigns selection depending on button pressed
	 */
	private void setupButtonActions() {
		
		andGate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {System.out.println(selection);
				selection = ToolSelection.AND_GATE;		
			}
		});
		
		
		orGate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selection = ToolSelection.OR_GATE;
			}
		});
		
		
		xorGate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selection = ToolSelection.XORGATE;				
			}
		});
		
		
		bulb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selection = ToolSelection.BULB;
			}
		});
		
		
		oscillator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selection = ToolSelection.OSCILATOR;		
			}
		});
		
	}
	
	
	
}
