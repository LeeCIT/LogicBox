import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;


public class SnappingPrototype extends ComponentAdapter {
	private boolean locked = false;
	private int snappingDistance = 30;
	
	JFrame mainFrame;
	int mainFramePosX = 0;
	int mainFramePosY = 0;
	
	//Default constructor
	public SnappingPrototype() {}
	
	//Allow the main frame to be used
	public SnappingPrototype(JFrame frame) {
		mainFrame = frame;

		//Position of the main frame
		mainFramePosX = mainFrame.getX();
		mainFramePosY = mainFrame.getY();
	}


	/**
	 * The logic for the snapping
	 */
	public void componentMoved(ComponentEvent evt) 
	{
		//Checks if already snapped to positions
		if (locked)
			return;
		
		//Get the position of the component
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int compPosX = evt.getComponent().getX();
		int compPosY = evt.getComponent().getY();
				
		
		//Top
		if (compPosY < snappingDistance) {
			compPosY = 0;
		}
		
		//Left
		if (compPosX < 0 + snappingDistance) {
			compPosX = 0;
		}
		
		//Right
		if (compPosX > size.getWidth() - evt.getComponent().getWidth() - snappingDistance) {
			compPosX = (int) size.getWidth() - evt.getComponent().getWidth();
		}
		
		//Bottom
		if (compPosY > size.getHeight() - evt.getComponent().getHeight() - snappingDistance) {
			compPosY = (int) size.getHeight() - evt.getComponent().getHeight();
		}
		
		
		//Snap to the main frame component
		if (compPosX < compPosX - evt.getComponent().getHeight() - snappingDistance){
			
		}
		
		/*
		 *	When snapping is done it generates other events
		 *  To avoid infinite loops lock the component, set the location and unlock
		 */
		locked = true;
		evt.getComponent().setLocation(compPosX, compPosY);
		locked = false;
	}
	
	
	//Demo main, just to test the functionality
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame();
		JLabel label = new JLabel("Move to sides to snap.");
		
		JFrame secondDemo = new JFrame();
		JLabel secondLab  = new JLabel("Demo test, move towards the edge of the screen to snap");
		
		
		//First frame
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new SnappingPrototype());
		frame.pack();
		frame.setVisible(true);
		
		//Second frame
		secondDemo.getContentPane().add(secondLab);
		secondDemo.addComponentListener(new SnappingPrototype(frame));
		secondDemo.pack();
		secondDemo.setVisible(true);
	}
}


