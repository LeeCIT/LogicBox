


package logicBox.gui;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import logicBox.util.Region;
import logicBox.util.Vec2;



public class EditorPanel extends JPanel
{
	protected void paintComponent( Graphics g ) {
		super.paintComponent( g );
		
		g.setColor( EditorColours.background );
		g.fillRect( getX(), getY(), getWidth(), getHeight() );
		
		Gfx.pushColorAndSet( g, EditorColours.grid );
		Gfx.drawGrid( g, new Region(this), new Vec2(64), 3 );
		Gfx.popColor( g );
	}
	
	
	
	public static void main( String[] args ) {
		EditorFrame frame = new EditorFrame();
		
		frame.setSize( new Dimension(600,600) );
		frame.setContentPane( new EditorPanel() );
		frame.setVisible( true );
	}
}
