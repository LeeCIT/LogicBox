package prototypes.snappingProto;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import prototypes.frameShareProto.MainFrame;


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
		// Checks if already snapped to positions
		if (locked)
			return;


		// Get the position of the component
		Rectangle size =  GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int compPosX = evt.getComponent().getX();
		int compPosY = evt.getComponent().getY();


		// Top
		if (compPosY < snappingDistance) {
			compPosY = 0;
		}

		// Left
		if (compPosX < 0 + snappingDistance) {
			compPosX = 0;
		}

		// Right
		if (compPosX > size.getWidth() - evt.getComponent().getWidth() - snappingDistance) {
			compPosX = (int) size.getWidth() - evt.getComponent().getWidth();
		}

		// Bottom
		if (compPosY > size.getHeight() - evt.getComponent().getHeight() - snappingDistance) {
			compPosY = (int) size.getHeight() - evt.getComponent().getHeight();
		}

		// Snap to the main frame component if the main frame has a reference
		if (mainFrame != null) {

			// Position of the main frame
			mainFramePosX = mainFrame.getX();
			mainFramePosY = mainFrame.getY();

			// Debugging Need to add algorithm to snap to the main frame
			// System.out.println("MainFrame pos x" + mainFramePosX + " CompPos x" + compPosX);
			// System.out.println("MainFrame pos y" + mainFramePosY + " CompPos y" + compPosY);

			if (isComponentOnXAxisOfFrame(mainFramePosX, compPosX)) {
				// Snap to top of the main frame
				if (checkTopOfFrame(mainFramePosY, compPosY))  {
					compPosY = mainFramePosY - evt.getComponent().getHeight();
				}

				// Snap to bottom of the main frame
				if (checkBottomOfFrame(mainFramePosY, compPosY)) {
					compPosY = mainFramePosY + mainFrame.getHeight();
				}

			}

			if (isComponentOnYaxisOfFrame(mainFramePosY, compPosY)) {
				// Snap to the left of the main frame
				if (checkLeftside(mainFramePosX, compPosX)) {
					compPosX = mainFramePosX - evt.getComponent().getWidth();
				}

				// Snap to the right of the main frame
				if (checkRightSide(mainFramePosX, compPosX)) {
					compPosX = mainFramePosX + mainFrame.getWidth();
				}
			}			
		}

		// When snapping is done it generates other events
		// To avoid infinite loops lock the component, set the location and unlock
		locked = true;
		evt.getComponent().setLocation(compPosX, compPosY);
		locked = false;
	}



	/**
	 * Check the left hand side of the of the frame
	 * @param mainFramePosx
	 * @param compPosX
	 * @return
	 */
	private boolean checkLeftside(int mainFramePosx, int compPosX) {
		int difference = mainFramePosx - compPosX;

		if (difference >= 0 && difference <= snappingDistance) {
			return true;
		}
		return false;
	}
	
	
	
	private boolean checkRightSide(int mainFramePosX, int compPosX) {
		int mainFrameRightSidePos 	= mainFramePosX + mainFrame.getWidth();
		int difference 				= mainFrameRightSidePos - compPosX;
		System.out.println(difference);
		if (difference >= 0 && difference <= snappingDistance) {
			return true;
		}
		return false;
	}
	


	/**
	 * Check the top of the frame
	 * @param mainFramePosY
	 * @param compPosY
	 * @return
	 */
	private boolean checkTopOfFrame(int mainFramePosY, int compPosY) {
		int yDifference = mainFramePosY - compPosY;

		if (yDifference >= 0 && yDifference <= snappingDistance) {
			return true;
		}
		return false;
	}



	/**
	 * Check the bottom of the frame
	 * @param mainFramePosY
	 * @param compPosY
	 * @return
	 */
	private boolean checkBottomOfFrame(int mainFramePosY, int compPosY) {
		int mainFrameBottomPos = mainFramePosY + mainFrame.getHeight();
		int yDifference 	   = mainFrameBottomPos - compPosY;

		if (yDifference <= 0 && yDifference >= -snappingDistance) {	// To check the snapping distance must be minus as it going from - to + to check
			return true;
		}
		return false;
	}



	/**
	 * Checks is the component in the same region as the main frame in terms of the x axis
	 * @param mainFramePosX
	 * @param compPosX
	 * @return
	 */
	private boolean isComponentOnXAxisOfFrame(int mainFramePosX, int compPosX) {
		int mainFrameXLength = mainFramePosX + mainFrame.getWidth();

		if (compPosX >= mainFramePosX && compPosX <= mainFrameXLength) {
			return true;
		}
		return false;
	}



	/**
	 * Checks is the component in the same region as the main frame in terms of the Y axis
	 * @param mainFramePosY
	 * @param compPosY
	 * @return
	 */
	private boolean isComponentOnYaxisOfFrame(int mainFramePosY, int compPosY) {
		int mainFrameYLength = mainFramePosY + mainFrame.getHeight();

		if (compPosY >= mainFramePosY && compPosY <= mainFrameYLength) {
			return true;
		}
		return false;
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
		frame.setLocationRelativeTo(null);

		//Second frame
		secondDemo.getContentPane().add(secondLab);
		secondDemo.addComponentListener(new SnappingPrototype(frame));
		secondDemo.setSize(150, 300);
		secondDemo.setVisible(true);
		secondDemo.setLocationRelativeTo(null);
	}
}


