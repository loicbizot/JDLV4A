package init;

import java.awt.image.BufferedImage;

public class PositionnedPicture implements Comparable<PositionnedPicture>{

	private Picture picture;
	private int x;
	private int y;
	
	public PositionnedPicture(Picture picture, int x, int y) {
		
		this.picture = picture;
		this.x = x;
		this.y = y;
		
	}

	public BufferedImage getImage() {
		return picture.getImage();
	}
	
	public Picture getPicture() {
		return picture;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return picture.getWidth();
	}
	
	public int getHeight() {
		return picture.getHeight();
	}
	
	public int getDeltaX() {
		return picture.getDeltaX();
	}
	
	public int getDeltaY() {
		return picture.getDeltaY();
	}
	
	public void move(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int compareTo(PositionnedPicture o) {

		if(this.getY() < o.getY())
			return -1;
		else
			return 1;
	}
	
	public boolean equals(Object o) {
		
		if(o instanceof PositionnedPicture) {
			
			PositionnedPicture p = (PositionnedPicture)o;
			if(this.x == p.x && this.y == p.y)
				return true;
			else
				return false;
			
		}
		else return false;
		
	}
	
}
