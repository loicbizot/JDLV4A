package init;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Picture{

	private BufferedImage image;
	private int width;
	private int height;
	private int deltaX;
	private int deltaY;
	private Type type;
	private int gW;
	private int gH;
	
	public Picture(String path){
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResource("/"+path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(image != null) {
			
			this.image = image;
			this.width = image.getWidth();
			this.height = image.getHeight();
			
		}
		
	}
	
	public Picture(Picture picture, int x, int y, int w, int h, int dx, int dy, Type t){
		
		BufferedImage image = picture.getImage();
		
		if(image != null) {
			
			this.image = image.getSubimage(x, y, w, h);
			this.width = w;
			this.height = h;
			this.deltaX = dx;
			this.deltaY = dy;
			type = t;
			
		}
		
		gW = 1;
		gH = 1;
		
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getDeltaX() {
		return deltaX;
	}
	
	public int getDeltaY() {
		return deltaY;
	}
	
	public Picture clone() {
		Picture clone = new Picture(this, 0, 0, width, height, deltaX, deltaY, type);
		clone.setGridWidth(gW);
		clone.setGridHeight(gH);
		return clone;
	}
	
	public Type getType() {
		return type;
	}
	
	public int gridWidth() {
		return gW;
	}
	
	public int gridHeight() {
		return gH;
	}
	
	public void setGridWidth(int w) {
		this.gW = w;
	}
	
	public void setGridHeight(int h) {
		this.gH = h;
	}
	
}
