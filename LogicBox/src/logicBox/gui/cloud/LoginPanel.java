package logicBox.gui.cloud;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class LoginPanel extends JDialog
{
	private static final long serialVersionUID = 8403323633746161165L;
	
	public LoginPanel(JFrame frame)
	{
		super(frame, "Login to BoxCloud");
		
		MigLayout layout = new MigLayout("fillx", "[right]rel[grow,fill]", "[]10[]");
		
		setSize(400, 135);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel(layout);
		
		panel.add(new JLabel("Email:"), "");
		panel.add(new JTextField(""), "wrap");
		panel.add(new JLabel("Password:"), "");
		panel.add(new JPasswordField(""), "wrap");
		panel.add(new JButton("Login"), "span 2,align 50% 50%");
		
		add(panel);
		
		setVisible(true);
	}
	
	public static void main(String args[])
	{
		LoginPanel lp = new LoginPanel(new JFrame());
	}
}
