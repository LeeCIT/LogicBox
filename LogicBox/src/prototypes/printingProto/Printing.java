package prototypes.printingProto;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import prototypes.ToolBarProto.ToolbarFrame;

public class Printing implements Printable, ActionListener{
	
	static JFrame frame;


	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		
		// Important. If this is not here then it spools forever
		if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
		
		Graphics2D g2d = (Graphics2D) graphics;
		
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		
		// Print the frame
		frame.print(graphics);
		
		return PAGE_EXISTS;
	}
	
	
	
	 public void actionPerformed(ActionEvent e) {
         PrinterJob job = PrinterJob.getPrinterJob();
         job.setPrintable(this);
         boolean printOk = job.printDialog();
         if (printOk) {
             try {
                  job.print();
             } catch (PrinterException ex) {
              ex.printStackTrace();
             }
         }
    }
 
	 
    public static void main(String args[]) {
        frame = new JFrame("Hello World Printer");
        
        JButton printButton = new JButton("Demo printing");
        printButton.addActionListener(new Printing());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add("Center", printButton);
        frame.pack();
        frame.setVisible(true);
    }

}
