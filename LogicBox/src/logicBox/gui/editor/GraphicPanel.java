


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import logicBox.util.Callback;
import logicBox.util.Util;



/**
 * A non-interactive version of the editor panel, for showing graphics.
 * @author Lee Coakley
 */
public class GraphicPanel extends EditorPanel
{
	private Camera  cam;
	private Graphic graphic;
	
	
	
	public GraphicPanel( Graphic graphic ) {
		super();
		
		this.cam     = new Camera();
		this.graphic = Util.deepCopy( graphic );
		
		setupCamera();
		setupGraphic();
		autoZoom();
	}
	
	
	
	private void setupGraphic() {
		addWorldRepaintListener( new RepaintListener() {
			public void draw( Graphics2D g ) {
				graphic.draw( g );
			}
		});
		
		addComponentListener( new ComponentAdapter() {
			public void componentResized( ComponentEvent e ) {
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
		
		frame.add( new GraphicPanel( GraphicGen.generateGateAnd(2) ) );
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		frame.setVisible( true );
	}
}
