


package logicBox.gui.help;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;
import logicBox.gui.editor.GraphicPanel;
import logicBox.sim.component.ComponentType;



/**
 * Shows component graphics and an explanation for it. 
 * @author Lee Coakley
 */
public class GraphicInfoPanel extends JPanel
{
	private JLabel       title;
	private GraphicPanel graphicPanel;
	private JTextPane    textPane;
	private JScrollPane  scrollPane;
	
	
	
	public GraphicInfoPanel() {
		setupComponents();
		setupLayout();
	}
	
	
	
	public void showInfoFor( ComponentType type ) {
		title       .setText   ( type.getName()        );
		graphicPanel.setGraphic( type.getGraphic()     );
		textPane    .setText   ( type.getDescription() );
	}
	
	
	
	private void setupComponents() {
		title = new JLabel();
		
		graphicPanel = new GraphicPanel();
		
		textPane = new JTextPane();
		textPane.setEditable( false );
		
		scrollPane = new JScrollPane( textPane );
	}
	
	
	
	private void setupLayout() {
		setLayout( new MigLayout( "", "[grow,fill]", "[][grow 20,fill][grow 80,fill]") );
		
		add( title,        "wrap" );
		add( graphicPanel, "wrap" );
		add( scrollPane,   ""     );
	}
}














