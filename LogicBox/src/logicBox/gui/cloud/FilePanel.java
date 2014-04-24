package logicBox.gui.cloud;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
import logicBox.web.DownloadInterface;
import logicBox.web.Request;
import logicBox.web.RequestInterface;
import logicBox.web.RequestInterface.status;

public class FilePanel extends JDialog {
	
	private static final long serialVersionUID = -7467219206621556151L;
	private static FilePanel instance = null;
	
	private File fileToOpen;
	private static DefaultListModel<String> 	files		= new DefaultListModel<String>();
	
	private JFrame 						parent;
	private JList<String> 				listFile			= new JList<String>(files);
	private JButton 					btnDelete 			= new JButton("Delete");
	private JButton 					btnOpen 			= new JButton("Open");
	
	public FilePanel(JFrame frame)
	{
		super(frame, "My Boxcloud Files", ModalityType.DOCUMENT_MODAL);
		parent = frame;
		
		setupComponents();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public static File openFile() {
		if(instance == null)
			instance = new FilePanel(GUI.getMainFrame());
		
		instance.fileToOpen = null;
		
		files.clear();
		
		for(String f : CloudController.getUser().getFiles())
			files.addElement(f);
		
		instance.setVisible(true);
		instance.btnOpen.setEnabled(true);
		
		return instance.fileToOpen;
	}
	
	private void setupComponents() {
		MigLayout 	layout 	= new MigLayout( "", "128px[][]128px", "[][]" );
		JPanel 		panel 	= new JPanel(layout);
		
		listFile.setBorder(BorderFactory.createLineBorder(Color.black));
		
		panel.add(listFile, "height 85%, width 90%, span 2, wrap");
		panel.add(btnDelete, "alignx left");
		panel.add(btnOpen, "alignx right");
		
		btnDelete.addActionListener(onDeleteClick());
		btnOpen.addActionListener(onOpenClick());
		
		setSize( 600, 400 );		
		add(panel);
	}
	
	private ActionListener onDeleteClick() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(listFile.getSelectedIndex() == -1)
					GUI.showError(parent, "Invalid Index", "Please select a file first!");
				else
				{
					Request r = new Request();
					
					btnDelete.setEnabled(false);
					btnOpen.setEnabled(false);
					listFile.setEnabled(false);
					
					r.setRequestInterface(handleDelete());
						
					r.delete(files.elementAt(listFile.getSelectedIndex()));
				}
			}
		};
	}
	
	private ActionListener onOpenClick() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(listFile.getSelectedIndex() == -1)
					GUI.showError(parent, "Invalid Index", "Please select a file first!");
				else
				{
					Request r = new Request();
					
					btnOpen.setEnabled(false);
					
					r.download(files.elementAt(listFile.getSelectedIndex()), handleOpen());
				}
			}
		};
	}
	
	private RequestInterface handleDelete() {
		return new RequestInterface() {
			@Override
			public void onRequestResponse(HttpResponse<JsonNode> res, Request req, status stat) {
				if(stat != status.COMPLETED)
					GUI.showError(parent, "Could not make request!", "Request Failure");
				else
				{
					if(req.hasErrors())
						GUI.showErrorList(parent, req.getErrors(), "Deletion Error");
					else
					{
						CloudController.getUser().getFiles().remove(listFile.getSelectedIndex());
						files.remove(listFile.getSelectedIndex());

						btnDelete.setEnabled(true);
						btnOpen.setEnabled(true);
						listFile.setEnabled(true);
						
						GUI.showMessage(parent, "File deleted!", "Your file has been deleted!");
					}
				}
			}
		};
	}
	
	private DownloadInterface handleOpen() {
		return new DownloadInterface() {
			@Override
			public void onDownloadResponse(HttpResponse<String> res, String file, Request req, status stat) {
				if(stat != status.COMPLETED)
					GUI.showError(parent, "Could not make request!", "Request Failure");
				else
				{
					OutputStream out = null;
					InputStream in = res.getRawBody();
					File f = new File(System.getProperty("java.io.tmpdir") + file);
					
					if(f != null) {
						try {
							out =  new FileOutputStream(f);
							
							int read = 0;
							byte[] bytes = new byte[1024];
					 
							while ((read = in.read(bytes)) != -1) {
								out.write(bytes, 0, read);
							}
							
							out.close();
							
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
			 
						fileToOpen = f;
						dispose();
					}
				}
			}
		};
	}
}
