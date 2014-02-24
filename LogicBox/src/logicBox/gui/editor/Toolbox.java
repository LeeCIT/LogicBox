


package logicBox.gui.editor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import logicBox.gui.GUI;
import logicBox.sim.component.ComponentActive;
import logicBox.sim.component.GateAnd;
import logicBox.sim.component.GateBuffer;
import logicBox.sim.component.GateNand;
import logicBox.sim.component.GateNor;
import logicBox.sim.component.GateNot;
import logicBox.sim.component.GateOr;
import logicBox.sim.component.GateXnor;
import logicBox.sim.component.GateXor;
import logicBox.util.Evaluator;



/**
 * The editor toolbox, where components are displayed in a palette for easy creation.
 * @author John Murphy
 * @author Lee Coakley
 */
public class Toolbox extends JToolBar
{
	private EditorPanel            activeEditorPanel;
	private Evaluator<EditorPanel> evaluator;
	
	
	
	public Toolbox() {
		super( "Toolbox" );
		setMargin( new Insets(0, 2, 0, 0) );
		setOrientation( JToolBar.VERTICAL );
		
		setupEvaluator();
		addGateButtons();
	}
	
	
	
	private void setupEvaluator() {
		evaluator = new Evaluator<EditorPanel>() {
			public EditorPanel evaluate() {
				return activeEditorPanel;
			}
		};
	}



	private void addGateButtons() {
		ToolboxButton[] gateButts = {
			genButton( new GateBuffer() ),
			genButton( new GateNot   () ),
			genButton( new GateAnd   () ),
			genButton( new GateNand  () ),
			genButton( new GateOr    () ),
			genButton( new GateNor   () ),
			genButton( new GateXor   () ),
			genButton( new GateXnor  () )
		};
		
		addCategory( "Gates", gateButts );
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
				ed.initiateComponentCreation( genCommand(com) );
			}
		});
	}
	
	
	
	private EditorCreationCommand genCommand( ComponentActive com ) {
		return new EditorCreationCommand( com, com.getGraphic() );
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
		addCategory( title );
		JPanel panel = new ToolboxCategoryPanel( buttons );
		add( panel );
	}
	
	
	
	private void setButtonEvaluators( ToolboxButton...buttons ) {
		for (ToolboxButton butt: buttons)
			butt.setEditorPanelEvaluator( evaluator );
	}
	
	
	
	/**
	 * Add a the category heading and separator.
	 * @param heading
	 * @param items
	 */
	private void addCategory( String title ) {
		add( createCategoryLabel( title ) );
		add( new JToolBar.Separator() );
	}
	
	
	
	private JLabel createCategoryLabel( String title ) {
		JLabel label = new JLabel( title );
		Font   font  = label.getFont();
		Font   bold  = new Font( font.getName(), Font.BOLD, font.getSize() );
		
		label.setFont( bold );
		
		return label;
	}
	
	
	
	
	
	public static void main( String[] args ) {
		GUI.setNativeStyle();
		
		final JFrame      frame = new EditorFrame();
		final EditorPanel panel = new EditorPanel();
		
		Toolbox box = new Toolbox();
		box.setActiveEditorPanel( panel );
		
		frame.setSize( new Dimension(600,600) );
		frame.add( box, "west" );
		frame.add( panel );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}






























