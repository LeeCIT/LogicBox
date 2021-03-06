


package logicBox.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import logicBox.gui.editor.components.EditorComponentOscillator;
import logicBox.sim.component.simple.SourceOscillator;
import logicBox.util.CallbackParam;



/**
 * Dialogue for configuring the frequency of an oscillator.
 * @author Lee Coakley
 */
public class OscillatorDialogue extends JDialog
{
	private EditorComponentOscillator osc;
	private EditorWorld               world;
	private int                       freqDiv;
	private CallbackParam<String>     onMod;
	
	private JLabel  label;
	private JSlider slider;
	private JButton buttApply;
	private JButton buttOk;
	private JButton buttCancel;
	
	
	
	public OscillatorDialogue( EditorFrame parent, EditorComponentOscillator osc, EditorWorld world, CallbackParam<String> onMod ) {
		super( parent, "Oscillator Setup", true );
		
		this.osc     = osc;
		this.world   = world;
		this.freqDiv = osc.getComponent().getFrequencyDivisor();
		this.onMod   = onMod;
		
		setupComponents();
		setupActions();
		setupLayout();
		
		pack();
		setResizable( false );
		setLocationRelativeTo( parent );
		setVisible( true );
	}
	
	
	
	private void setupComponents() {
		int sliderPos = SourceOscillator.baseFrequencyHz - osc.getComponent().getFrequencyDivisor();
		
		label      = new JLabel();
		slider     = new JSlider( 1, SourceOscillator.baseFrequencyHz-1, sliderPos );
		buttApply  = new JButton( "Apply"  );
		buttOk     = new JButton( "OK"     );
		buttCancel = new JButton( "Cancel" );
		
		buttApply.setEnabled( false );
		
		updateLabelText();
	}
	
	
	
	private void setupActions() {
		slider.addChangeListener( new ChangeListener() {
			public void stateChanged( ChangeEvent e ) {
				freqDiv = SourceOscillator.baseFrequencyHz - slider.getValue();
				updateLabelText();
				buttApply.setEnabled( isDifferent(freqDiv) );
			}
		});
		
		buttApply.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				applyChanges();
				buttApply.setEnabled( false );
			}
		});
		
		buttOk.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				applyChanges();
				dispose();
			}
		});
		
		buttCancel.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				dispose();
			}
		});
	}
	
	
	
	protected void applyChanges() {
		if (isDifferent( freqDiv )) {		
			osc.getComponent().setFrequencyDivisor( freqDiv );
			world.resyncOscillators();
			onMod.execute( "Change oscillator frequency" );
		}
	}
	
	
	
	private boolean isDifferent( int freqDiv ) {
		int currentFreqDiv = osc.getComponent().getFrequencyDivisor();
		return currentFreqDiv != freqDiv;
	}
	
	
	
	private void setupLayout() {
		setLayout( new MigLayout("", "[grow]", "[][grow,fill][]") );
		
		add( label,      "wrap"    );
		add( slider,     "wrap"    );
		add( buttApply,  "split 3" );
		add( buttOk,     ""        );
		add( buttCancel, ""        );
		
		setSize( 320, 128 );
	}
	
	
	
	private void updateLabelText() {
		int freq = SourceOscillator.baseFrequencyHz / freqDiv;
		label.setText( "Frequency: " + freq + "hz" );
	}
}

















