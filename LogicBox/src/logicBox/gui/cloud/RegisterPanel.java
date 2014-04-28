
package logicBox.gui.cloud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import logicBox.gui.GUI;
import logicBox.web.*;
import net.miginfocom.swing.MigLayout;

/**
 * Makes the registration panel
 * @author Robert
 *
 */
public class RegisterPanel extends JDialog implements RequestInterface
{
	private static final long serialVersionUID = -2847175536208134240L;
	
	private JTextField 		txtEmail 			= new JTextField( 16 );
	private JPasswordField 	txtPassword 		= new JPasswordField( 16 );
	private JPasswordField 	txtPasswordConfirm 	= new JPasswordField( 16 );
	private JButton 		btnRegister 		= new JButton("Register");
	
	private Request r = new Request();
	private JFrame parent;
	
	private static RegisterPanel instance = null;
	
	public RegisterPanel(JFrame frame)
	{
		super(frame, "Register on BoxCloud");
		parent = frame;
		
		setupComponents();		
		HandleLoginAttempt();
		r.setRequestInterface(this);		
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo( parent );
	}
	
	
	
	private void setupComponents() {
		MigLayout 	layout 	= new MigLayout( "", "128px[][]128px", "128px[][][]128px" );
		JPanel 		panel 	= new JPanel(layout);
		
		panel.add(new JLabel("Email:"), 			"alignx right");
		panel.add(txtEmail,    "wrap");
		
		panel.add(new JLabel("Password:"), 			"alignx right");
		panel.add(txtPassword, "wrap");
		
		panel.add(new JLabel("Confirm Password:"), 	"alignx right");
		panel.add(txtPasswordConfirm, "wrap");
		
		panel.add(btnRegister, "skip 1, alignx right");		
		setSize( 600, 400 );		
		add(panel);
	}

	
	
	public static RegisterPanel getInstance()
	{
		if(instance == null)
			instance = new RegisterPanel(GUI.getMainFrame());
		
		instance.setVisible(true);
		
		return instance;
	}
	
	private void HandleLoginAttempt()
	{
		ActionListener al = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae) 
			{	
				btnRegister.setEnabled(false);

				r.register(
						txtEmail.getText(), 
						String.valueOf(txtPassword.getPassword())
				);
			}
		};
		
		btnRegister.addActionListener(al);
		txtEmail.addActionListener(al);
		txtPassword.addActionListener(al);
		txtPasswordConfirm.addActionListener(al);
	}
	
	public static void main(String args[])
	{
		new RegisterPanel(new JFrame());
	}

	@Override
	public void onRequestResponse(HttpResponse<JsonNode> res, Request req, status state) 
	{
		if(state == status.FAILED)
			GUI.showError(parent, "Could not make request!", "Request Failure");
		else
		{
			if(req.hasErrors())
				GUI.showErrorList(parent, req.getErrors(), "Registration Failure");
			else
			{
				CloudController.setAuthState(true);
				CloudController.authUser(res.getBody().getObject());
				
				dispose();
			}
		}
		
		btnRegister.setEnabled(true);
	}
}
