


package logicBox.gui.editor.toolbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import logicBox.gui.editor.EditorCreationCommand;
import logicBox.gui.editor.EditorPanel;
import logicBox.sim.component.*;
import logicBox.util.Evaluator;
import logicBox.util.Util;



/**
 * The editor toolbox, where components are displayed in a palette for easy creation.
 * @author John Murphy
 * @author Lee Coakley
 */
public class Toolbox extends JDialog
{
	private EditorPanel            activeEditorPanel;
	private Evaluator<EditorPanel> evaluator;
	
	
	
	public Toolbox( JFrame parent ) {
		super( parent, "Toolbox" );
		setDefaultCloseOperation( Toolbox.DISPOSE_ON_CLOSE );
		setLayout( new MigLayout( "insets 0, flowy" ) );
		setupEvaluator();
		addButtons();
		pack();
		setResizable( false );
		setVisible( true );
	}
	
	
	
	private void addButtons() {
		addGateButtons();
		addDisplayButtons();
		addSourceButtons();
		addComplexButtons();
	}



	private void setupEvaluator() {
		evaluator = new Evaluator<EditorPanel>() {
			public EditorPanel evaluate() {
				return activeEditorPanel;
			}
		};
	}



	private void addGateButtons() {
		ToolboxButton[] butts = {
			genButton( new GateBuffer() ),
			genButton( new GateNot   () ),
			genButton( new GateAnd   () ),
			genButton( new GateNand  () ),
			genButton( new GateOr    () ),
			genButton( new GateNor   () ),
			genButton( new GateXor   () ),
			genButton( new GateXnor  () )
		};
		
		addCategory( "Gates", butts );
	}
	
	
	
	private void addDisplayButtons() {
		ToolboxButton[] butts = {
			genButton( new DisplayLed()      ),
			genButton( new DisplaySevenSeg() )
		};
		
		addCategory( "Displays", butts );
	}
	
	
	
	private void addSourceButtons() {
		ToolboxButton[] butts = {
			genButton( new SourceFixed(false)  ),
			genButton( new SourceFixed(true)   ),
			genButton( new SourceToggle(false) ),
			genButton( new SourceOscillator(1) )
		};
		
		addCategory( "Sources", butts );
	}
	
	
	
	private void addComplexButtons() {
		ToolboxButton[] butts = {
			genButton( new Decoder   (2) ),
			genButton( new Mux       (2) ),
			genButton( new Demux     (2) ),
			genButton( new FlipFlopD ()  ),
			genButton( new FlipFlopJK()  ),
			genButton( new FlipFlopT ()  )
		};
		
		addCategory( "Components", butts );
	}
	
	
	
	private ToolboxButton genButton( final ComponentActive com ) {
		ToolboxButton butt = new ToolboxButton( com.getGraphic(), com.getName(), null );
		attachListener( butt, com );
		return butt;
	}
	
	
	
	private void attachListener( final ToolboxButton butt, final ComponentActive com ) {
		butt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {		
				EditorPanel ed = butt.getEditorPanelEvaluator().evaluate();
				ed.getToolManager().initiateComponentCreation( genCommand(com) );
			}
		});
	}
	
	
	
	private EditorCreationCommand genCommand( ComponentActive com ) {
		return new EditorCreationCommand( Util.deepCopy(com), com.getGraphic() );
	}
	
	
	
	/**
	 * Set the focused editor panel.
	 * Should be called whenever the user focuses on an editor panel.
	 * It determines where the button events will be sent. 
	 */
	public void setActiveEditorPanel( EditorPanel ed ) {
		this.activeEditorPanel = ed;
	}
	
	
	
	/**
	 * Add a logical group of buttons.  IE gates, components, etc.
	 * @param category
	 * @param items
	 */
	public void addCategory( String title, ToolboxButton...buttons ) {
		setButtonEvaluators( buttons );
		JPanel panel = new ToolboxCategoryPanel( title, buttons );
		add( panel );
	}
	
	
	
	private void setButtonEvaluators( ToolboxButton...buttons ) {
		for (ToolboxButton butt: buttons)
			butt.setEditorPanelEvaluator( evaluator );
	}
}






























