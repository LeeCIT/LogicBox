


package logicBox.gui.editor.toolbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import org.gpl.JSplitButton.action.SplitButtonActionListener;
import net.miginfocom.swing.MigLayout;
import logicBox.gui.IconEnum;
import logicBox.gui.IconLoader;
import logicBox.gui.editor.EditorCreationCommand;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.sim.component.*;
import logicBox.util.Evaluator;
import logicBox.util.Util;
import logicBox.util.Vec2;



/**
 * The editor toolbox, where components are displayed in a palette for easy creation.
 * There can only be one at a time.
 * @author John Murphy
 * @author Lee Coakley
 */
public class Toolbox extends JDialog
{
	private static Vec2    lastPosition;
	private static Toolbox instance;
	
	private ToolManager            activeToolManager;
	private Evaluator<ToolManager> evaluator;
	
	
	
	public Toolbox( JFrame parent ) {
		super( parent, "Toolbox" );
		setDefaultCloseOperation( Toolbox.DISPOSE_ON_CLOSE );
		setLayout( new MigLayout( "insets 0, flowy" ) );
		setupEvaluator();
		addButtons();
		pack();
		setResizable( false );
		setVisible( true );
		
		if (lastPosition != null)
			setLocation( (int) lastPosition.x, (int) lastPosition.y );
		
		if (instance != null)
			throw new RuntimeException( "Trying to create duplicate Toolbox" );
		
		instance = this;
		
		addComponentListener( new ComponentAdapter() {
			public void componentMoved( ComponentEvent ev ) {
				lastPosition = new Vec2( getLocation() );
			}
		});
	}
	
	
	
	public static Toolbox getInstance() {
		return instance;
	}
	
	
	
	public void dispose() {
		super.dispose();
		instance = null;
	}
	
	
	
	/**
	 * Set the focused editor tool manager.
	 * Should be called whenever the user focuses on an editor panel.
	 * It determines where the button events will be sent.
	 */
	public void setActiveToolManager( ToolManager manager ) {
		this.activeToolManager = manager;
	}
	
	
	
	private void setupEvaluator() {
		evaluator = new Evaluator<ToolManager>() {
			public ToolManager evaluate() {
				return activeToolManager;
			}
		};
	}
	
	
	
	private void addButtons() {
		addPowerButtons();
		addToolButtons();
		addGateButtons();
		addDisplayButtons();
		addSourceButtons();
		addComplexButtons();
		addFlipFlopButtons();
		addBlackBoxButtons();
	}
	
	
	
	private void addPowerButtons() {
		ButtTargetable[] butts = {
			genButtonPowerOn(),
			genButtonPowerReset(),
			genButtonPowerOff()
		};
		
		addCategory( "Power", 3, butts );
	}
	
	
	
	private void addToolButtons() {
		ButtTargetable[] butts = {
			genButtonTrace(),
			genButtonJunction(),
			genButtonText() // TODO comment graphic
		};
		
		addCategory( "Tools", 3, butts );
	}
	
	
	
	private void addGateButtons() {
		ButtTargetable[] butts = {
			genButton( new GateBuffer()  ),
			genButton( new GateNot   ()  ),
			genButton( new GateAnd(2),
					   new GateAnd(3),
					   new GateAnd(4),
					   new GateAnd(5),
					   new GateAnd(6),
					   new GateAnd(7),
					   new GateAnd(8)
			),
			genButton( new GateNand(2),
					   new GateNand(3),
					   new GateNand(4),
					   new GateNand(5),
					   new GateNand(6),
					   new GateNand(7),
					   new GateNand(8)
			),
			genButton( new GateOr(2),
					   new GateOr(3),
					   new GateOr(4),
					   new GateOr(5),
					   new GateOr(6),
					   new GateOr(7),
					   new GateOr(8)
			),
			genButton( new GateNor(2),
					   new GateNor(3),
					   new GateNor(4),
					   new GateNor(5),
					   new GateNor(6),
					   new GateNor(7),
					   new GateNor(8)
			),
			genButton( new GateXor(2),
					   new GateXor(3),
					   new GateXor(4),
					   new GateXor(5),
					   new GateXor(6),
					   new GateXor(7),
					   new GateXor(8)
			),
			genButton( new GateXnor(2),
					   new GateXnor(3),
					   new GateXnor(4),
					   new GateXnor(5),
					   new GateXnor(6),
					   new GateXnor(7),
					   new GateXnor(8)
			),
		};
		
		addCategory( "Gates", 2, butts );
	}
	
	
	
	private void addDisplayButtons() {
		ButtTargetable[] butts = {
			genButton( new DisplayLed()      ),
			genButton( new DisplaySevenSeg() )
		};
		
		addCategory( "Displays", 3, butts );
	}
	
	
	
	private void addSourceButtons() {
		ButtTargetable[] butts = {
			genButton( new SourceFixed(false)  ),
			genButton( new SourceFixed(true)   ),
			genButton( new SourceToggle(false) ),
			genButton( new SourceOscillator(SourceOscillator.baseFrequencyHz/8) )
		};
		
		addCategory( "Sources", 3, butts );
	}
	
	
	
	private void addComplexButtons() {
		ButtTargetable[] butts = {
			genButton( new Mux(2),
					   new Mux(3),
					   new Mux(4),
					   new Mux(5),
					   new Mux(6),
					   new Mux(7),
					   new Mux(8)
			),
			genButton( new Demux(2),
					   new Demux(3),
					   new Demux(4),
					   new Demux(5),
					   new Demux(6),
					   new Demux(7),
					   new Demux(8)
			),
			genButton( new Decoder(1),
					   new Decoder(2),
					   new Decoder(3),
					   new Decoder(4)
			)
		};
		
		addCategory( "Components", 2, butts );
	}
	
	
	
	private void addFlipFlopButtons() {
		ButtTargetable[] butts = {
			genButton( new FlipFlopD () ),
			genButton( new FlipFlopJK() ),
			genButton( new FlipFlopT () )
		};
		
		addCategory( "Flip-Flops", 3, butts );
	}
	
	
	
	private void addBlackBoxButtons() {
		ButtTargetable[] butts = {
			genButton( new BlackBoxPin( true  ) ),
			genButton( new BlackBoxPin( false ) ),
		};
		
		addCategory( "Black-box", 3, butts );
	}
	
	
	
	private ToolboxButton genButtonPowerOn() {
		final ToolboxButton butt = new ToolboxButton( "On" );
		butt.setToolTipText( "Power on the circuit." );
		
		butt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				butt.getTargetToolManager().powerOn();
			}
		});
		
		return butt;
	}
	
	
	
	private ToolboxButton genButtonPowerReset() {
		final ToolboxButton butt = new ToolboxButton( "RST" );
		butt.setToolTipText( "Reset the circuit and power it back on." );
		
		butt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				butt.getTargetToolManager().powerReset();
			}
		});
		
		return butt;
	}
	
	
	
	private ToolboxButton genButtonPowerOff() {
		final ToolboxButton butt = new ToolboxButton( "Off" );
		butt.setToolTipText( "Power off the circuit." );
		
		butt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				butt.getTargetToolManager().powerOff();
			}
		});
		
		return butt;
	}
	
	
	
	private ToolboxButton genButtonTrace() {
		final ToolboxButton butt = new ToolboxButtonTrace();
		
		butt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				butt.getTargetToolManager().initiateTraceCreation();
			}
		});
		
		return butt;
	}
	
	
	
	private ToolboxButton genButtonJunction() {
		final ToolboxButton butt = new ToolboxButtonJunction();
		
		butt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				butt.getTargetToolManager().initiateJunctionCreation();
			}
		});
		
		return butt;
	}
	
	
	
	private ToolboxButton genButtonText() {
		ToolboxButton butt = new ToolboxButton( IconLoader.load(IconEnum.editText) );
		
		// TODO implement
		
		return butt;
	}
	
	
	
	private ButtTargetable genButton( final ComponentActive...coms ) {
		ComponentActive first = coms[0];
		
		if (coms.length <= 1) {
			ToolboxButton butt = new ToolboxButtonCom( first.getGraphic(), first.getName() );
			attachListener( butt, first );
			return butt;
		}
		else {
			ToolboxButtonSplit butt = new ToolboxButtonSplit( first.getGraphic(), first.getName() );
			JPopupMenu         menu = new JPopupMenu();
			
			final ActionListener al = genListener( butt, first );
			
			butt.addSplitButtonActionListener( new SplitButtonActionListener() {
				public void splitButtonClicked( ActionEvent ev ) {}
				public void buttonClicked( ActionEvent ev ) {
					al.actionPerformed( ev );
				}
			});
			
			for (ComponentActive com: coms)
				menu.add( genMenuItem(butt, com) );
			
			butt.setPopupMenu( menu );
			return butt;
		}
	}
	
	
	
	private JMenuItem genMenuItem( final ButtTargetable butt, ComponentActive com ) {
		JMenuItem item = new JMenuItem();
		item.setText( com.getName() );
		item.addActionListener( genListener(butt, com) );
		return item;
	}
	
	
	
	private void attachListener( final ButtTargetable butt, final ComponentActive com ) {
		butt.getButton().addActionListener( genListener(butt,com) );
	}
	
	
	
	private ActionListener genListener( final ButtTargetable butt, final ComponentActive com ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				butt.getTargetToolManager().initiateComponentCreation( genCommand(com) );
			}
		};
	}
	
	
	
	private EditorCreationCommand genCommand( ComponentActive com ) {
		return new EditorCreationCommand( Util.deepCopy(com), com.getGraphic() );
	}
	
	
	
	/**
	 * Add a logical group of buttons.  IE gates, components, etc.
	 */
	private void addCategory( String title, int wrapAfter, ButtTargetable...buttons ) {
		setButtonEvaluators( buttons );
		JPanel panel = new ToolboxCategoryPanel( title, wrapAfter, buttons );
		add( panel );
	}
	
	
	
	private void setButtonEvaluators( ButtTargetable...buttons ) {
		for (ButtTargetable butt: buttons)
			butt.setToolManagerEvaluator( evaluator );
	}
}






























