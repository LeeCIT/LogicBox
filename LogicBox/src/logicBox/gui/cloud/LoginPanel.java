package logicBox.gui.cloud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import logicBox.gui.GUI;
import logicBox.gui.menubar.EditorMenuBar;
import logicBox.web.*;
import net.miginfocom.swing.MigLayout;

public class LoginPanel extends JDialog implements RequestInterface
{
	private static final long serialVersionUID = 8403323633746161165L;
	
	private JTextField txtEmail = new JTextField("");
	private JPasswordField txtPassword = new JPasswordField("");
	private JButton btnLogin = new JButton("Login");
	
	private Request r = new Request();
	
	private JFrame parent;
	
	private static LoginPanel instance = null;
	
	public LoginPanel(JFrame frame)
	{
		super(frame, "Login to BoxCloud");
		parent = frame;

		MigLayout layout = new MigLayout("fillx", "[right]rel[grow,fill]", "[]10[]");
		
		setSize(400, 135);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel(layout);
		
		panel.add(new JLabel("Email:"), "");
		panel.add(txtEmail, "wrap");
		panel.add(new JLabel("Password:"), "");
		panel.add(txtPassword, "wrap");
		panel.add(btnLogin, "span 2,align 50% 50%");

		add(panel);
		
		HandleLoginAttempt();
		r.setRequestInterface(this);
		
		setVisible(true);
	}
	
	public static LoginPanel getInstance()
	{
		if(instance == null)
			instance = new LoginPanel(GUI.getMainFrame());
		
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
			Dialog.showError(parent, "Could not make request!", "Request Failure");
		else
		{
			if(req.hasErrors())
				Dialog.showErrorList(parent, req.getErrors(), "Login Failure");
			else
			{
				EditorMenuBar.getInstance().setAuthState(true);
				
				dispose();
			}
		}
		
		btnLogin.setEnabled(true);
	}
}
