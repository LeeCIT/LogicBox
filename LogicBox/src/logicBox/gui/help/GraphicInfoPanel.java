


package logicBox.gui.help;

import java.awt.Font;
import javax.swing.JEditorPane;
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
		title = new JLabel( "_" );
		title.setFont( new Font( "Ariel", Font.BOLD, 24 ) );
		
		graphicPanel = new GraphicPanel();
		
		textPane = new JTextPane();
		textPane.setEditable( false );
		textPane.setContentType( "text/html" );
		textPane.putClientProperty( JEditorPane.HONOR_DISPLAY_PROPERTIES, true );
		textPane.setFont( getFont() );
		
		scrollPane = new JScrollPane( textPane );
	}
	
	
	
	private void setupLayout() {
		setLayout( new MigLayout( "wrap 1", "[grow,fill]", "[][grow,fill][grow,fill]") );
		
		add( title,        "height 10%" );
		add( graphicPanel, "height 25%" );
		add( scrollPane,   "height 65%" );
	}
}














