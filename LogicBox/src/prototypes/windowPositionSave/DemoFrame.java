package prototypes.windowPositionSave;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.Properties;
import java.io.*;

import logicBox.gui.GUI;

class DemoFrame {
	 
    
    
    /**
     * A small demo
     * @param args
     */
    public static void main(String[] args) {
    	
    	GUI.setNativeStyle();
    	
        final JFrame f = new JFrame("Good Location & Size");
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.addWindowListener( new WindowPositionManager(f) );
        
        JTextArea ta = new JTextArea(20,50);
        f.add(ta);
        f.pack();

        WindowPositionManager.restoreWindowPosition(f);
        f.setVisible(true);
    }
}