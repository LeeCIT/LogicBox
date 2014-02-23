package prototypes.printingProto;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.*;

import javax.swing.*;


public class Printing implements Printable{
	
	JFrame frame;


	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
		// Important. If this is not here then it spools forever
		if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
		
		BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = image.createGraphics();
		//g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		
		// Print the frame
		frame.print(graphics);
		
		return PAGE_EXISTS;
	}
	
	
	
	/**
	 * Set up a print job, handles the dialog
	 * @param frame
	 */
	 public void setUpPrintJob(JFrame frame) {
		 this.frame = frame;
		 
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

}
