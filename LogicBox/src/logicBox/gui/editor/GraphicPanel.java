


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import logicBox.util.Callback;
import logicBox.util.Util;



/**
 * A non-interactive version of the editor panel for showing graphics.
 * @author Lee Coakley
 */
public class GraphicPanel extends EditorPanel
{
	private Camera  cam;
	private Graphic graphic;
	
	
	
	public GraphicPanel() {
		super();
		
		this.cam = new Camera();
		setupCamera();
		setupEvents();
	}
	
	
	
	public void setGraphic( Graphic graphic ) {
		this.graphic = Util.deepCopy( graphic );
		autoZoom();
		repaint();
	}
	
	
	
	private void setupEvents() {
		addWorldRepaintListener( new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (graphic != null)
					graphic.draw( g );
			}
		});
		
		addComponentListener( new ComponentAdapter() {
			public void componentResized( ComponentEvent e ) {
				if (graphic != null)
					autoZoom();
			}
		});
	}
	
	
	
	private void setupCamera() {
		cam.attachTo( this );
		
		cam.addTransformCallback( new Callback() {
			public void execute() {
				repaint();
			}
		});
		
		setCamera( cam );
	}
	
	
	
	private void autoZoom() {
		cam.viewBbox( graphic.getBbox(), 32 );
	}
	
	
	
	
	
	public static void main( String[] args ) {
		JFrame frame = new JFrame();
		
		frame.add( new GraphicPanel() );
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		frame.setVisible( true );
	}
}
