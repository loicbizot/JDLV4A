package init;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import display.KeyMovablePan;
import display.ScaledPan;
import elements.Ground;

public class Panel extends JPanel implements KeyMovablePan,ScaledPan{
	
	private SidePan sP;
	private PositionnedPicture pP;
	private ArrayList<PositionnedPicture> pictures;
	private ArrayList<Rectangle2D> rects;
	
	private int mousePosX;
	private int mousePosY;
	
	private int dimX;
	private int dimY;
	
	private int scale;
	private int view_pos_x;
	private int view_pos_y;
	
	public Panel() {
		
		mousePosX = -100;
		mousePosY = -100;
		
		dimX = 0;
		dimY = 0;
		
		scale = 32;
		view_pos_x = 50;
		view_pos_y = 50;
		
		this.setLayout(new BorderLayout());
		pictures = new ArrayList<>();
		rects = new ArrayList<>(); 
		
		sP = new SidePan();
		sP.getXField().getDocument().addDocumentListener(new DimensionsListener());
		sP.getYField().getDocument().addDocumentListener(new DimensionsListener());
		
		this.add(sP,BorderLayout.EAST);
		this.setBackground(Color.WHITE);
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D)g;	
		
		int begin_x = (this.view_pos_x*scale/8 - this.getWidth()/2)/scale - 1;
		int begin_y = (this.view_pos_y*scale/8 - this.getHeight()/2)/scale - 1;
		int end_x = begin_x + this.getWidth()/scale + 3;
		int end_y = begin_y + this.getHeight()/scale + 3;
		
		// grid
		
		g.drawString(Integer.toString(scale),20,20);

		Ground gr = new Ground(0,0);
		
		g.setColor(Color.BLACK);
		for(int i = begin_x; i < end_x; i ++) {
			for(int j = begin_y; j < end_y; j++) {
				
				if( 0 <= i && i < dimX && 0 <= j && j < dimY) {
					
					Rectangle2D rect = new Rectangle2D.Double(i*32,j*32,32,32);
					rect = transform(rect);
					
					g2D.drawImage(gr.get_img(), (int)rect.getX(), (int)rect.getY(),(int) (rect.getWidth() + rect.getX()),(int)(rect.getHeight() + rect.getY()),
							0,0,32,32,this);
					
				}
			}
		}
		if(0 <= mousePosX && mousePosX < dimX*32 && 0 <= mousePosY && mousePosY < dimY*32) {
			if(pP != null && pP.getPicture().getType() == Type.HOUSE) {
				Rectangle2D mouseRect = new Rectangle2D.Float((mousePosX - 5*16)/32*32, (mousePosY - 5*16)/32*32, 32*5, 32*5);
				mouseRect = transform(mouseRect);
				g.setColor(new Color(0,0,255,100));
				g2D.fill(mouseRect);
			}
			else {
				Rectangle2D mouseRect = new Rectangle2D.Float(mousePosX/32*32, mousePosY/32*32, 32, 32);
				mouseRect = transform(mouseRect);
				g.setColor(new Color(0,0,255,100));
				g2D.fill(mouseRect);
			}
		}
		
		Picture weed = ImageGenerator.getDaCorn();
		
		for(Rectangle2D r : rects) {
			
			for(int i = (int) r.getX(); i < r.getX() + r.getWidth(); i += 32 ) {
				for(int j = (int) r.getY(); j < r.getY() + r.getHeight(); j += 32) {
					if(weed != null) {
						
						Rectangle2D rect = new Rectangle2D.Double(i,j,32,32);
						rect = transform(rect);
						g2D.drawImage(weed.getImage(), (int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight(), this);
					
					}
				}
			}
			
			Rectangle2D rect = transform(r);
			if(r.getWidth()/32 < 3 || r.getHeight()/32 < 3)
				g2D.setPaint(new Color(255,50,50,100));
			else
				g2D.setPaint(new Color(0,169,255,100));
			g2D.fill(rect);
			
		}
		
		for(PositionnedPicture p: pictures) {
			
			Rectangle2D r = new Rectangle2D.Double(p.getX(),p.getY(),p.getWidth(),p.getHeight());
			r = transform(r);
			if(p.getPicture().getType() == Type.HOUSE)
				g.drawImage(p.getImage(), (int)r.getX() + p.getDeltaX()*scale/32, (int)r.getY() + p.getDeltaY()*scale/32, 5*scale, 5*scale, this);
			else
				g.drawImage(p.getImage(), (int)r.getX() + p.getDeltaX()*scale/32, (int)r.getY() + p.getDeltaY()*scale/32, (int)r.getWidth(), (int)r.getHeight(), this);
			
		}
		
		if(pP != null) {
			if(pP.getPicture().getType() == Type.HOUSE)
				g.drawImage(pP.getImage(), pP.getX(), pP.getY(), 5*scale, 5*scale, this);
			else	
				g.drawImage(pP.getImage(), pP.getX(), pP.getY(), pP.getWidth()*scale/32, pP.getHeight()*scale/32, this);
		}
		
	}
	
	public Rectangle2D transform(Rectangle2D r) {
		
		double left_top_corner_posX = r.getX()*scale/32;
		double left_top_corner_posY = r.getY()*scale/32;
		
		double width = r.getWidth()*scale/32;
		double height = r.getHeight()*scale/32;

		//decalage du la position due la "camera"
		left_top_corner_posX += this.getWidth()/2 - this.view_pos_x*this.scale/8;
		left_top_corner_posY += this.getHeight()/2 - this.view_pos_y*this.scale/8;
		
		return new Rectangle2D.Double(left_top_corner_posX,left_top_corner_posY,width,height);
		
	}
	
	public SidePan getSP() {
		return sP;
	}
	
	public void setPP(PositionnedPicture pP) {
		this.pP = pP;
	}
	
	public boolean intersect(PositionnedPicture p) {
		
		boolean found = false;
		int i = 0;
		
		while(!found && i < pictures.size()) {
			
			PositionnedPicture pict = pictures.get(i);
			
			double x1 = p.getX();
			double y1 = p.getY();
			double w1 = p.getPicture().gridWidth()*32;
			double h1 = p.getPicture().gridHeight()*32;
			
			double x2 = pict.getX();
			double y2 = pict.getY();
			double w2 = pict.getPicture().gridWidth()*32;
			double h2 = pict.getPicture().gridHeight()*32;
			
			Rectangle2D r1 = new Rectangle2D.Double(x1,y1,w1,h1);
			Rectangle2D r2 = new Rectangle2D.Double(x2,y2,w2,h2);
			
			if(r1.intersects(r2))
				found = true;
			
			i++;
			
		}
		
		return found;
		
	}
	
	public boolean verify(PositionnedPicture p) {
		
		int x1 = p.getX();
		int y1 = p.getY();
		int x2 = p.getX() + p.getPicture().gridWidth()*32;
		int y2 = p.getY() + p.getPicture().gridHeight()*32;
		
		boolean res = 0 <= x1 && x2 <= dimX*32 && 0 <= y1 && y2 <= dimY*32;
		
		return res;
		
	}
	
	public boolean verify(Point2D p) {
		return 0 <= p.getX() && p.getX() < dimX*32 && 0 <= p.getY() && p.getY() < dimY*32;
	}
	
	public void addPositionnedPicture(PositionnedPicture p) {
		
		int x = p.getX();
		int y = p.getY();
		
		Point2D po = new Point2D.Double(x,y);
		po = realMousePos(po);
		
		if(p.getPicture().getType() == Type.HOUSE) {
			x = ((int)po.getX() - 5*16)/32*32;
			y = ((int)po.getY() - 5*16)/32*32;
		}
		else {
			x = (int)po.getX()/32*32;
			y = (int)po.getY()/32*32;
		}
		
		p.move(x, y);
		
		if(!verify(p))
			return;
		
		if(!intersect(p)) {
			pictures.add(p);
			Collections.sort(pictures);
		}
		else
			System.out.println("Deja quelqu'un");
	}
	
	public Picture getPictureAt(int x, int y) {
		
		Point2D mouse = new Point2D.Double(x,y);
		mouse = realMousePos(mouse);
		
		x = (int)mouse.getX();
		y = (int)mouse.getY();
		
		for(PositionnedPicture p: pictures) {
			if(p.getX() <= x && x <= p.getX() + p.getPicture().gridWidth()*32 && p.getY() <= y && y <= p.getY() + p.getPicture().gridHeight()*32) {
				pictures.remove(p);
				return p.getPicture();
			}
		}
		
		return null;
		
	}
	
	public void addRect(Rectangle2D r) {
		
		if(rects.size() == 3)
			return;
		
		double x = r.getX();
		double y = r.getY();
		
		Point2D mouse = new Point2D.Double(x,y);
		mouse = realMousePos(mouse);
		
		x = (int)mouse.getX()/32*32;
		y = (int)mouse.getY()/32*32;
		
		r.setRect(x, y, 32, 32);
		
		rects.add(r);
		
	}
	
	public ArrayList<PositionnedPicture> getPictures() {
		return pictures;
	}

	public ArrayList<Rectangle2D> getRects() {
		return rects;
	}

	public int getDimX() {
		return dimX;
	}

	public int getDimY() {
		return dimY;
	}

	public Corner getRectangleCornerAt(int x, int y) {
		
		int convertedX = x;
		int convertedY = y;
		
		Point2D mouse = new Point2D.Double(convertedX,convertedY);
		mouse = realMousePos(mouse);
		
		convertedX = (int)mouse.getX();
		convertedY = (int)mouse.getY();
		
		convertedX = convertedX /32 * 32; 
		convertedY = convertedY /32 * 32;
		
		for(Rectangle2D r: rects) {
			
			if(r.getX() == convertedX && r.getY() == convertedY)
				return Corner.TOP_LEFT;
			
			if(r.getX() + r.getWidth() -32 == convertedX && r.getY() == convertedY)
				return Corner.TOP_RIGHT;
			
			if(r.getX() == convertedX && r.getY() + r.getHeight() - 32== convertedY)
				return Corner.DOWN_LEFT;
			
			if(r.getX() + r.getWidth() - 32 == convertedX && r.getY() + r.getHeight() - 32 == convertedY)
				return Corner.DOWN_RIGHT;
			
		}
		
		return null;
		
	}
	
	public Rectangle2D getRectangleAt(int x, int y) {
		
		Point2D mouse = new Point2D.Double(x,y);
		mouse = realMousePos(mouse);
		
		x = (int)mouse.getX();
		y = (int)mouse.getY();
		
		for(Rectangle2D r: rects) {
			
			if(r.contains(new Point2D.Double(x,y)))
				return r;
			
		}
		
		return null;
		
	}
	
	public void removeRect(int x, int y) {
		
		Rectangle2D r = getRectangleAt(x,y);
		if(r != null)
			rects.remove(r);
		
	}
	
	public void removeRect(Rectangle2D r) {
		rects.remove(r);
	}
	
	public void setMousePos(int x, int y) {
		
		this.mousePosX = this.view_pos_x*4 + x*32/scale - this.getWidth()*16/scale;
		this.mousePosY = this.view_pos_y*4 + y*32/scale - this.getHeight()*16/scale;
		
	}
	
	public Point2D realMousePos(Point2D mousePos) {
		
		double x = mousePos.getX();
		double y = mousePos.getY();
		
		double vX = this.view_pos_x*4 + x*32/scale - this.getWidth()*16/scale;
		double vY = this.view_pos_y*4 + y*32/scale - this.getHeight()*16/scale;
		
		return new Point2D.Double(vX, vY);
		
	}
	
	public void update() {
		
		int n = pictures.size();
		
		for(int i = 0 ; i < n; i++) {
			PositionnedPicture p = pictures.get(i);
			if(p.getX()/32 > dimX || p.getY()/32 > dimY ) {
				pictures.remove(p);
				i--;
				n--;
			}
		}
		
		n = rects.size();
		for(int i = 0 ; i < n ; i++) {
			
			Rectangle2D r = rects.get(i);
			
			if(r.getX()/32 > dimX || r.getY()/32 > dimY ) {
				rects.remove(r);
				i--;
				n--;
			}
			else if(r.getX()/32 + r.getWidth()/32 > dimX || r.getY()/32 + r.getHeight()/32 > dimY) {
				
				r.setRect(r.getX(), r.getY(), Math.min(r.getWidth(), dimX*32 - r.getX()), Math.min(r.getHeight(), dimY*32 - r.getY()));
				
			}
			
		}
			
		repaint();
			
	}
	
	private class DimensionsListener implements DocumentListener{
		
		private boolean isAnInteger(String s) {
			
			boolean found = false;
			int i = 0;
			
			while (!found && i < s.length()) {
				
				char c = s.charAt(i);
				if(!('0' <= c && c <= '9'))
					found = true;
				
				i++;
			}
			
			return !found;
			
		}
		
		public void update(DocumentEvent e) {
			Document source = e.getDocument();
			int stringLenght = source.getLength();
			try {
				
				String sourceContent = source.getText(0, stringLenght);
				if(sourceContent != null && sourceContent.length() != 0) {
					
					if(isAnInteger(sourceContent)) {
						int value = Integer.parseInt(sourceContent);
						if(e.getDocument() == sP.getXField().getDocument())
							dimX = value;
						else
							dimY = value;
					}
					
				}
				
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			Panel.this.update();
		}
		
		public void insertUpdate(DocumentEvent e) {
			update(e);
		}

		public void removeUpdate(DocumentEvent e) {
			update(e);
		}

		public void changedUpdate(DocumentEvent e) {
			
		}
		
	}

	public void moveView(int x, int y) {
		this.view_pos_x += x;
		this.view_pos_y += y;
		repaint();
	}

	public int getScale() {
		return this.scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
		repaint();
	}
	
	public boolean verif() {
		
		boolean result = this.rects.size() != 0;
		
		int nb_of_man = 0;
		int nb_of_woman = 0;
		int nb_of_houses = 0;
		int nb_of_weed = 0;
		 
		for(PositionnedPicture p:pictures) {
			
			switch(p.getPicture().getType()) {
			case HOUSE:
				nb_of_houses ++;
				break;
			case MAN:
				nb_of_man ++;
				break;
			case WEED:
				nb_of_weed ++;
				break;
			case WOMAN:
				nb_of_woman ++;
				break;
			default:
				break;
			
			}
			
		}
		
		result = result && nb_of_houses * nb_of_man * nb_of_weed * nb_of_woman != 0;
		
		return result;
		
	}

}
