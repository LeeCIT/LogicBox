package prototypes.printingProto;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.print.*;

import javax.swing.*;


public class Printing implements Printable{

	Component comp;


	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
		// Important. If this is not here then it spools forever
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}

		createImage();
		setDoubleBuffereing(false);
		
		Graphics2D g2d = (Graphics2D)graphics;
	    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	    
		// Print the frame
	    comp.printAll(g2d);
		
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


	private BufferedImage createImage() {
		Dimension     size    = comp.getPreferredSize();
		BufferedImage image   = new BufferedImage( size.width, size.height, BufferedImage.TYPE_INT_RGB );
		Graphics2D    g2d     = image.createGraphics();
		comp.paint(g2d);
		g2d.dispose();
		
		return image;
	}
	
	
	
	private void setDoubleBuffereing(Boolean bool) {
		RepaintManager currentManager = RepaintManager.currentManager(comp);
	    currentManager.setDoubleBufferingEnabled(bool);
	}
}
