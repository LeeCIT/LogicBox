package logicBox.gui.help;


import java.awt.Graphics2D;
import javax.swing.JTextPane;
import logicBox.sim.component.ComponentType;

public class HelpPane extends JTextPane
{
	private ComponentType compType;
	
	public HelpPane() {}
	
	
	
	public void drawComponent ( ComponentType compType ) {
		this.compType = compType;
		repaint();
	}
	
	
	
	private void drawComponent ( ComponentType compType, Graphics2D g ) {
		compType.getGraphic();
		
	}
	
	
	protected void paintComponent( Graphics2D g ){
		super.paintComponent(g);
		drawComponent(compType, g);
	}
}
