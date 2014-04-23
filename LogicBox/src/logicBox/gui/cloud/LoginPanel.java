
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
 * Makes the log in panel
 * @author John, Lee, Robert
 *
 */
public class LoginPanel extends JDialog implements RequestInterface
{
	private static final long serialVersionUID = 8403323633746161165L;
	
	private JTextField 		txtEmail 			= new JTextField( 16 );
	private JPasswordField 	txtPassword 		= new JPasswordField( 16 );
	private JButton 		btnLogin 			= new JButton("Login");
	
	private Request r = new Request();
	private JFrame parent;
	
	private static LoginPanel instance = null;
	
	public LoginPanel(JFrame frame)
	{
		super(frame, "Login to BoxCloud");
		parent = frame;
		
		setupComponents();		
		HandleLoginAttempt();
		r.setRequestInterface(this);		
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	
	
	
	private void setupComponents() {
		MigLayout 	layout 	= new MigLayout( "", "128px[][]128px", "128px[][][]128px" );
		JPanel 		panel 	= new JPanel(layout);
		
		panel.add(new JLabel("Email:"), 	"alignx right");
		panel.add(txtEmail, "wrap");
		
		panel.add(new JLabel("Password:"), 	"alignx right");
		panel.add(txtPassword, "wrap");
		
		panel.add(btnLogin, "skip 1, alignx right");		
		setSize( 600, 400 );		
		add(panel);
	}

	
	
	
	public static LoginPanel getInstance()
	{
		if(instance == null)
			instance = new LoginPanel(GUI.getMainFrame());
		
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
				btnLogin.setEnabled(false);

				r.login(
						txtEmail.getText(), 
						String.valueOf(txtPassword.getPassword())
				);
			}
		};
		
		btnLogin.addActionListener(al);
		txtEmail.addActionListener(al);
		txtPassword.addActionListener(al);
	}
	
	public static void main(String args[])
	{
		new LoginPanel(new JFrame());
	}

	@Override
	public void onRequestResponse(HttpResponse<JsonNode> res, Request req, status state) 
	{
		if(state == status.FAILED)
			GUI.showError(parent, "Could not make request!", "Request Failure");
		else
		{
			if(req.hasErrors())
				GUI.showErrorList(parent, req.getErrors(), "Login Failure");
			else
			{
				//EditorMenuBar.getInstance().setAuthState(true);
				
				dispose();
			}
		}
		
		btnLogin.setEnabled(true);
	}
}
