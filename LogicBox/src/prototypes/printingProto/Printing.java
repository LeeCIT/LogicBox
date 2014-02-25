package prototypes.printingProto;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;


public class Printing implements Printable{

	Component comp;


	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
		// Important. If this is not here then it spools forever
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}
	    
		Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		
		// Turn of double buffering for better image quality
		setDoubleBuffereing(false);
		
		comp.printAll(g2d);
	    
	    // Turn it back on
	    setDoubleBuffereing(true);
		
		return PAGE_EXISTS;
	}



	/**
	 * Set up a print job, handles the dialog
	 * @param frame
	 */
	public void setUpPrintJob(Component comp) {
		this.comp = comp;

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
	
	
	
	private void setDoubleBuffereing(Boolean bool) {
		RepaintManager currentManager = RepaintManager.currentManager(comp);
	    currentManager.setDoubleBufferingEnabled(bool);
	}
}
