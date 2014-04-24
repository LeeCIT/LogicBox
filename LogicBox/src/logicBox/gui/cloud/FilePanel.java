package logicBox.gui.cloud;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import net.miginfocom.swing.MigLayout;
import logicBox.gui.GUI;
import logicBox.web.Request;
import logicBox.web.RequestInterface;

public class FilePanel extends JDialog {
	
	private static final long serialVersionUID = -7467219206621556151L;
	private static FilePanel instance = null;
	
	private JFrame 						parent;
	
	private DefaultListModel<String> 	files				= new DefaultListModel<String>();
	
	private JList<String> 				listFile			= new JList<String>(files);
	
	private JButton 					btnOpen 			= new JButton("Open");
	
	public FilePanel(JFrame frame)
	{
		super(frame, "Register on BoxCloud");
		parent = frame;
		
		setupComponents();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public static FilePanel getInstance() {
		if(instance == null)
			instance = new FilePanel(GUI.getMainFrame());
		
		instance.setVisible(true);
		
		return instance;
	}
	
	private void setupComponents() {
		MigLayout 	layout 	= new MigLayout( "", "128px[][]128px", "[][]" );
		JPanel 		panel 	= new JPanel(layout);
		
		listFile.setBorder(BorderFactory.createLineBorder(Color.black));
		
		for(String f : CloudController.getUser().getFiles())
			files.addElement(f);
		
		panel.add(listFile, "height 85%, width 90%, span 2, wrap");
		panel.add(btnOpen, "alignx right");
		
		btnOpen.addActionListener(onOpenClick());
		
		setSize( 600, 400 );		
		add(panel);
	}
	
	public static void main(String args[]) {
		FilePanel f = new FilePanel(new JFrame());
		
		f.setVisible(true);
	}
	
	private ActionListener onOpenClick() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Request r = new Request();
				
				r.setRequestInterface(handleOpen());
			}
		};
	}
	
	private RequestInterface handleOpen() {
		return new RequestInterface() {
			@Override
			public void onRequestResponse(HttpResponse<JsonNode> res, Request req, status stat) {
				if(stat != status.COMPLETED)
					GUI.showError(parent, "Could not make request!", "Request Failure");
				else
				{
					if(req.hasErrors())
						GUI.showErrorList(parent, req.getErrors(), "Registration Failure");
					else
					{
						
					}
				}
			}
		};
	}
}
