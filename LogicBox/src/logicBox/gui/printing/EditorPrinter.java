package logicBox.gui.printing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.*;
import javax.swing.*;


public class EditorPrinter implements Printable{
	
	JFrame frame;


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
