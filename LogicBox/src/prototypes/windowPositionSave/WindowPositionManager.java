package prototypes.windowPositionSave;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;

public class WindowPositionManager extends WindowAdapter{
	
	 // Might want to save somewhere else
    public static final String fileName = "options.prop";
    private JFrame frame;
    
    
    public WindowPositionManager(JFrame frame) {
    	this.frame = frame;
    }
    
    
   /**
    * Override the window closing event to save the details
    */
    public void windowClosing(WindowEvent we) {
        try {
           storeOptions(frame);
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }    

    /**
     * Store location of the window
     * @param frame 	The frame to save
     */
    private void storeOptions(Frame f){
        File       file       = new File(fileName);
        Properties properties = new Properties();
        
        // restore the frame from 'full screen' first!
        f.setExtendedState(Frame.NORMAL);
        Rectangle r = f.getBounds();
        int x = (int)r.getX();
        int y = (int)r.getY();
        int w = (int)r.getWidth();
        int h = (int)r.getHeight();

        properties.setProperty("x", "" + x);
        properties.setProperty("y", "" + y);
        properties.setProperty("w", "" + w);
        properties.setProperty("h", "" + h);

        BufferedWriter br;
		try {
			br = new BufferedWriter(new FileWriter(file));
			properties.store(br, "Properties of the user frame");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to save window pref");
		}
    }

    
    /**
     * Restores the window position if the file is found otherwise it doesn't.
     */
    public static void restoreWindowPosition(JFrame frame) {
    	File optionsFile = new File(WindowPositionManager.fileName);
    	
    	 if (optionsFile.exists()) {
             WindowPositionManager.restoreOptions(frame);
         } else {
        	 frame.setLocationByPlatform(true);
         }
    }
    
    
    
    /** Restore location & size of UI */
    private static void restoreOptions(Frame f) {
        File           file = new File(fileName);
        Properties     prop = new Properties();
        BufferedReader br   = null;
		
        try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
        try {
			prop.load(br);
		} catch (IOException e) {
			e.printStackTrace();
		}

        int x = Integer.parseInt(prop.getProperty("x"));
        int y = Integer.parseInt(prop.getProperty("y"));
        int w = Integer.parseInt(prop.getProperty("w"));
        int h = Integer.parseInt(prop.getProperty("h"));

        Rectangle r = new Rectangle(x,y,w,h);

        f.setBounds(r);
    }

}
