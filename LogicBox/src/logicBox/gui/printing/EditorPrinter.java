


package logicBox.gui.printing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.*;
import logicBox.gui.editor.EditorFrame;



/**
 * Creates a printing dialogue which prints the editor panel associated with the given frame.
 * @author John Murphy
 * @author Lee Coakley
 */
public class EditorPrinter implements Printable
{
	private EditorFrame	frame;
	
	
	
	public EditorPrinter( EditorFrame frame ) {
		this.frame = frame;
		createPrintJob();
	}
	
	
	
	public int print( Graphics graphics, PageFormat pageFormat, int pageIndex ) throws PrinterException {
		if (pageIndex > 0) // Important. If this is not here then it spools forever
			return NO_SUCH_PAGE;
		
		Graphics2D g2d = (Graphics2D) graphics;
		g2d.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
		
		frame.getEditorPanel().print( graphics );
		
		return PAGE_EXISTS;
	}
	
	
	
	private void createPrintJob() {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable( this );
		boolean userSelectedOkay = job.printDialog();
		
		if (userSelectedOkay) {
			try {
				job.print();
			}
			catch (PrinterException ex) {
				ex.printStackTrace();
			}
		}
	}
}
