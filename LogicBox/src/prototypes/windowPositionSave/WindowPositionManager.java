package prototypes.windowPositionSave;

import java.awt.Frame;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class WindowPositionManager {
	
	 // Might want to save somewhere else
    public static final String fileName = "options.prop";

    /**
     * Store location of the window
     * @param frame the frame to save
     */
    public static void storeOptions(Frame f){
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

    
    
    /** Restore location & size of UI */
    public static void restoreOptions(Frame f) throws IOException {
        File           file = new File(fileName);
        Properties     prop = new Properties();
        BufferedReader br   = new BufferedReader(new FileReader(file));
        prop.load(br);

        int x = Integer.parseInt(prop.getProperty("x"));
        int y = Integer.parseInt(prop.getProperty("y"));
        int w = Integer.parseInt(prop.getProperty("w"));
        int h = Integer.parseInt(prop.getProperty("h"));

        Rectangle r = new Rectangle(x,y,w,h);

        f.setBounds(r);
    }

}
