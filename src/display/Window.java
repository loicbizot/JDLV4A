package display;

import javax.swing.JFrame;

public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public Window(int width, int height){
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setTitle("Jeu de la vie v1.0");
		
	}
	
	public void reload() {
		
		this.revalidate();
		this.requestFocus();
		
	}
	
}
