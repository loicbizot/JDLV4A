package init;

import java.awt.Toolkit;

import javax.swing.JFrame;

public class Window extends JFrame{
	
	public Window() {
		this.setTitle("TestDragNDrop");
		
		Toolkit t = Toolkit.getDefaultToolkit();
		
		this.setSize(t.getScreenSize());
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		Panel p = new Panel();
		MouseDragNDrop mDND = new MouseDragNDrop(p);
		
		p.addMouseListener(mDND);
		p.addMouseMotionListener(mDND);
		p.requestFocus();
		
		this.add(p);
	}

}
