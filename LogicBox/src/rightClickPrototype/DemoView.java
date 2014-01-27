package rightClickPrototype;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

public class DemoView extends JFrame{

	JPopupMenu popup;
	JMenuItem addPin;
	JMenuItem removePin;
	JLabel demoGate;
	
	int pins = 2;

	public DemoView() {
		//Set the native look and feel, IT will be in a different class when finished
		try 
		{
	        //Set System L&F
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
	    } 
		catch (Exception e) {
				e.printStackTrace();		
		}
	
		
		//Setup
		popup = new JPopupMenu();
		demoGate = new JLabel("pins = " + pins);
		
		//Make the popup menu item
		addPin = new JMenuItem("Add Pin");
		popup.add(addPin);
		
		//Add another menu item
		removePin = new JMenuItem("Remove pin");
		popup.add(removePin);

		//Add listener to components that can bring up popup menus.
		MouseListener popupListener = new PopupListener();
		
		//Add the listener to the gate
		demoGate.addMouseListener(popupListener);
		
		//Add Label to the frame
		add(demoGate);
		
		//Add some crappy actionLister
		addPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pins++;
				resetLabel();
			}
		});
		
		//Get rid of a pin
		removePin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( pins<=2 ) {
					//Do nothing
					System.out.println("Must have atleast two pins");
				}
				else {
					pins--;
					resetLabel();
				}
			}
		});
		

		
		//Frame stuff
		setTitle("Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);			//Set the frame to visible				
		setSize(700, 700);			//Set the size of the frame
		setLocationRelativeTo(null);//Loads the window in the center
	}


	//This will all be fixed up later. I just wanted to get it working.
	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(),
						e.getX(), e.getY());
			}
		}

	}
	
	
	// I know, this is awful, but I want a small demo to be working
	private void resetLabel() {
		demoGate.setText("Pins = " + pins);
	}
}

