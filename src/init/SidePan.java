package init;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SidePan extends JPanel{

	private ArrayList<Picture> pictures;
	
	private JTextField x;
	private JTextField y;
	
	public SidePan() {
		this.setBackground(Color.gray);
		this.pictures = ImageGenerator.generate();
		
		this.add(new JLabel("Dimensions : "));
		this.add(new JLabel("x = "));
		x = new JTextField(5);
		this.add(x);
		this.add(new JLabel("y = "));
		y = new JTextField(5);
		this.add(y);
		
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		int offSetX = 20;
		int offSetY = 100;
		
		for(Picture p : pictures) {
			
			if(p.getType() == Type.HOUSE) {
				
				g.drawImage(p.getImage(), offSetX, offSetY, 32, 32, this);
				
			}
			else {
				g.drawImage(p.getImage(), offSetX, offSetY, p.getWidth(), p.getHeight(), this);
				offSetY += p.getHeight() + 20;
			}
		}
		
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(100,0);
		
	}
	
	public Picture getPictureAt(int x, int y) {
		
		System.out.println(x + ", " + y);
		
		int offSetX = 20;
		int offSetY = 100;
		
		for(Picture p : pictures) {
			
			if(p.getType() == Type.HOUSE) {
				
				if(offSetX <= x && x <= offSetX + 32 && offSetY <= y && y <= offSetY + 32)
					return p.clone();
				
			}
			else {

				if(offSetX <= x && x <= offSetX + p.getWidth() && offSetY <= y && y <= offSetY + p.getHeight())
					return p.clone();
				
				offSetY += p.getHeight() + 20;
				
			}
			
		}
		
		System.out.println("nope");
		
		return null;
		
	}
	
	public JTextField getXField() {
		return x;
	}
	
	public JTextField getYField() {
		return y;
	}
	
}
