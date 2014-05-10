


package logicBox.gui.editor;

import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;



/**
 * Shows the about dialogue.
 * @author Lee Coakley
 */
public class AboutDialogue extends JDialog
{
	public AboutDialogue( JFrame parent ) {
		super( parent, "About LogicBox" );
		
		setModalityType( ModalityType.APPLICATION_MODAL );
		
		setupComponents();
		
		pack();
		setLocationRelativeTo( parent );
		setVisible( true );
	}
	
	
	
	private void setupComponents() {
		JTextArea text = new JTextArea(
			"LogicBox was created by:\n\n" +
			"Lee Coakley\n" +
			"John Murphy\n" +
			"Robert O' Leary\n" +
			"Shaun O' Donovan"
		);
		
		text.setEditable( false );
		text.setFont( new Font( getFont().getName(), Font.BOLD, 12 ) );
		text.setBackground( getBackground() );
		
		setLayout( new MigLayout("", "128[]128", "128[]128") );
		add( text );
	}
}
