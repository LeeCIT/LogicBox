


package logicBox.gui.editor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.*;
import logicBox.gui.GUI;
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
		
		evaluator = new Evaluator<EditorPanel>() {
			public EditorPanel evaluate() {
				return activeEditorPanel;
			}
		};
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
		
		Toolbox box = EditorToolboxLinker.createLinkedToolbox( panel );
		
		frame.setSize( new Dimension(600,600) );
		frame.add( box, "west" );
		frame.add( panel );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}






























