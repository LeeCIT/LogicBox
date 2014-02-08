package prototypes.snappingProto;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class SnappingPrototype extends ComponentAdapter {
	private boolean locked = false;
	private int snappingDistance;

	JFrame mainFrame;
	int mainFramePosX;
	int mainFramePosY;



	/**
	 * Default constructor. The default snapping distance is 10
	 * The snap will only snap to the screen edges
	 */
	public SnappingPrototype() {
		snappingDistance = 10;
	}



	/**
	 * Adds window edge snapping at the specified snapping distance
	 * @param snappingDistance		Snapping distance
	 */
	public SnappingPrototype(int snappingDistance) {
		this.snappingDistance = snappingDistance;
	}



	/**
	 * Will snap to the window edge and the Frame reference passed in with
	 * a default snapping distance of 10
	 * @param frame		The frame to snap to
	 */
	public SnappingPrototype(JFrame frame) {
		mainFrame = frame;
		snappingDistance = 10;
	}



	/**
	 * Will snap to the frame passed in and the window at the specified snapping distance
	 * @param frame		The frame to snap to
	 * @param snappingDistance		The distance to snap
	 */
	public SnappingPrototype(JFrame frame, int snappingDistance) {
		mainFrame 				= frame;
		this.snappingDistance 	= snappingDistance;
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
		Rectangle size =  GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
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

		//Snap to the main frame component if the main frame has a reference
		if (mainFrame != null) {

			//Position of the main frame
			mainFramePosX = mainFrame.getX();
			mainFramePosY = mainFrame.getY();

			//Debugging Need to add algorithm to snap to the main frame
			System.out.println("MainFrame pos x" + mainFramePosX + " CompPos x" + compPosX);
			//System.out.println("MainFrame pos y" + mainFramePosY + " CompPos y" + compPosY);
			System.out.println(mainFramePosX - compPosX);

			//Snap to bottom of the main frame

			//Snap to top of the main frame

			//Snap to the left of the main frame
			if (mainFramePosX - compPosX <= snappingDistance && mainFramePosX - compPosX >= snappingDistance) {
				compPosX = mainFramePosX - evt.getComponent().getWidth();
			}

			//Snap to the right of the main frame
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
		JLabel label = new JLabel("Move to sides to snap. Main Frame");

		JFrame secondDemo = new JFrame();
		JLabel secondLab  = new JLabel("Demo test, move towards the edge of the screen to snap, Second frame");


		//First frame
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new SnappingPrototype());
		frame.setSize(400, 400); //Just simulating demo size
		frame.setVisible(true);

		//Second frame
		secondDemo.getContentPane().add(secondLab);
		secondDemo.addComponentListener(new SnappingPrototype(frame));
		secondDemo.setSize(100, 300);
		secondDemo.setVisible(true);
	}
}


