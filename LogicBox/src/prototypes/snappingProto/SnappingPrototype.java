package prototypes.snappingProto;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class SnappingPrototype extends ComponentAdapter {
	private boolean locked;
	private int     snappingDistance;
	private int     defaultSnap = 10;
	private boolean snappedToFrame;

	private JFrame mainFrame;



	/**
	 * Default constructor. The default snapping distance is 10
	 * The snap will only snap to the screen edges
	 */
	public SnappingPrototype() {
		snappingDistance = defaultSnap;
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
		snappingDistance = defaultSnap;
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
	
	
	
	public void componentMoved(ComponentEvent evt) {
		if (locked) // Checks if already snapped to positions
			return;

		Rectangle desktop = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		Component comp    = evt.getComponent();
		int compPosX      = comp.getX();
		int compPosY      = comp.getY();
		int compWidth     = comp.getWidth();
		int compHeight    = comp.getHeight();
		
		if (compPosY < snappingDistance && compPosY > 0) 
			compPosY = 0;

		if (compPosX < snappingDistance && compPosX > 0)
			compPosX = 0;

		if (compPosX > desktop.width - compWidth - snappingDistance && compPosX + compWidth < desktop.width)
			compPosX = desktop.width - compWidth;
		
		if (compPosY > desktop.height - compHeight - snappingDistance && compPosY + compHeight < desktop.height)
			compPosY = desktop.height - compHeight;

		// Snap to the main frame component if the main frame has a reference
		if (mainFrame != null) {
			int mainFramePosX = mainFrame.getX();
			int mainFramePosY = mainFrame.getY();

			if (isComponentOnXAxisOfFrame(mainFramePosX, comp)) {
				if (checkTopOfFrame(mainFramePosY, comp)) {
					compPosY = mainFramePosY - compHeight;
					snappedToFrame = true;
				}

				if (checkBottomOfFrame(mainFramePosY, compPosY)) {
					compPosY = mainFramePosY + mainFrame.getHeight();
					snappedToFrame = true;
				}
			}

			if (isComponentOnYaxisOfFrame(mainFramePosY, comp)) {
				if (checkLeftside(mainFramePosX, comp)) {
					compPosX = mainFramePosX - compWidth;
					snappedToFrame = true;
				}

				// Snap to the right of the main frame
				if (checkRightSide(mainFramePosX, compPosX)) {
					compPosX = mainFramePosX + mainFrame.getWidth(); 
					snappedToFrame = true;
				}
			}			
		}
System.out.println(snappedToFrame);
		// When snapping is done it generates other events
		// To avoid infinite loops lock the component, set the location and unlock
		locked = true;
		comp.setLocation(compPosX, compPosY);
		locked = false;
		snappedToFrame = false;
	}


	
	private boolean checkLeftside(int mainFramePosx, Component comp) {
		int difference = mainFramePosx - (comp.getX() + comp.getWidth());
		return (difference >= 0 && difference <= snappingDistance);
	}
	
	
	
	private boolean checkRightSide(int mainFramePosX, int compPosX) {
		int mainFrameRightSidePos = mainFramePosX + mainFrame.getWidth();
		int difference 			  = mainFrameRightSidePos - compPosX;
		return (difference <= 0 && difference >= -snappingDistance);
	}

	
	
	private boolean checkTopOfFrame(int mainFramePosY, Component comp) {
		int yDifference = mainFramePosY - (comp.getY() + comp.getHeight());
		return (yDifference >= 0 && yDifference <= snappingDistance);
	}
	
	
	
	private boolean checkBottomOfFrame(int mainFramePosY, int compPosY) {
		int mainFrameBottomPos = mainFramePosY + mainFrame.getHeight();
		int yDifference 	   = mainFrameBottomPos - compPosY;
		return yDifference <= 0
			&& yDifference >= -snappingDistance; // To check the snapping distance must be minus as it going from - to + to check
	}



	/**
	 * Checks is the component in the same region as the main frame in terms of the x axis
	 */
	private boolean isComponentOnXAxisOfFrame(int mainFramePosX, Component comp) {
		int mainFrameXLength = mainFramePosX + mainFrame.getWidth();
		int compX = comp.getX();
		int compLength = compX + comp.getWidth();
		return (compX >= mainFramePosX && compX <= mainFrameXLength) || (compLength >= mainFramePosX && compLength <= mainFrameXLength);
	}



	private boolean isComponentOnYaxisOfFrame(int mainFramePosY, Component comp) {
		int mainFrameYLength = mainFramePosY + mainFrame.getHeight();
		int compY = comp.getY();
		int compLength = compY + comp.getHeight();
		return (compY >= mainFramePosY && compY <= mainFrameYLength) || (compLength >= mainFramePosY && compLength <= mainFrameYLength);
	}

	
	
	

	//Demo main, just to test the functionality
	public static void main(String[] args) {
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


