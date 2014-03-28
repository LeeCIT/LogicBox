


package prototype.mcq;



import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;



public class CreateMCQFrame extends JFrame
{
	
	
	
	public CreateMCQFrame() 
	{
		
		
		setTitle("Create MCQ"); //Setting the title.
		setLayout( new MigLayout() );
		add ( new CreateMCQPanel() ); //Add the panel to the frame.
		
		setVisible(true);
		pack();
		
	}
	
	
	public static void main(String[] args) {
		CreateMCQFrame mcq = new CreateMCQFrame();
	}
}
