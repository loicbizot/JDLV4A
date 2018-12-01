package init;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class MouseDragNDrop implements MouseListener,MouseMotionListener{
	
	private Panel pan;
	private PositionnedPicture pictRef;
	private Rectangle2D rectRef;
	private Corner selectedCorner;
	
	public MouseDragNDrop(Panel pan) {
		
		this.pan = pan;
		
	}

	public void mouseDragged(MouseEvent e) {
		
		if(pictRef != null) {
			
			int scale = pan.getScale();
			if(pictRef.getPicture().getType() == Type.HOUSE)
				pictRef.move(e.getX() - 5*scale/2, e.getY()- 5*scale/2);
			else
				pictRef.move(e.getX() - pictRef.getWidth()*scale/32/2, e.getY()- pictRef.getHeight()*scale/32/2);
			
		}
		
		if(selectedCorner != null && rectRef != null) {
			
			Point2D pe = new Point2D.Double(e.getX(),e.getY());
			pe = pan.realMousePos(pe);
			
			if(!pan.verify(pe))
				return;
			
			double oriX = rectRef.getX();
			double oriY = rectRef.getY();
			double oriW = rectRef.getWidth();
			double oriH = rectRef.getHeight();
			
			double x = e.getX();
			double y = e.getY();
			
			Point2D p = new Point2D.Double(x,y);
			p = pan.realMousePos(p);
			x = (int)p.getX()/32*32;
			y = (int)p.getY()/32*32;
			
			switch(selectedCorner) {
			
			case DOWN_LEFT:
				rectRef.setRect(x, oriY, - x + oriX + oriW, y - oriY + 32);
				break;
			case DOWN_RIGHT:
				rectRef.setRect(oriX, oriY, x - oriX + 32, y - oriY + 32); // ok
				break;
			case TOP_LEFT:
				rectRef.setRect(x, y, - x + oriX + oriW, - y + oriY + oriH); // ok
				break;
			case TOP_RIGHT:
				rectRef.setRect(oriX, y, x - oriX + 32 , - y + oriY + oriH);
				break;
			default:
				break;
			
			}
			
		}
		
		pan.setMousePos(e.getX(), e.getY());
		pan.repaint();
		
	}

	public void mouseMoved(MouseEvent e) {
		
		pan.setMousePos(e.getX(), e.getY());
		pan.repaint();
		
	}

	public void mouseClicked(MouseEvent e) {
		
		if(e.getButton() == MouseEvent.BUTTON3) {
			
			pan.removeRect(e.getX(), e.getY());
			
		}
		
		pan.requestFocus();
		
	}

	public void mousePressed(MouseEvent e) {
		
		if(e.getButton() == MouseEvent.BUTTON1) {
			
			int translateX = e.getX() - pan.getWidth() + pan.getSP().getPreferredSize().width;
			int translateY = e.getY();
			Picture picture = null;
			
			if(e.getX() - pan.getWidth() + pan.getSP().getPreferredSize().width >= 0)
				picture = pan.getSP().getPictureAt(translateX, translateY);
			else {
				picture = pan.getPictureAt(e.getX(),e.getY());
			}
			
			if(picture != null) {
				
				int scale = pan.getScale();
				if(picture.getType() == Type.HOUSE)
					pictRef = new PositionnedPicture(picture , e.getX() - 5*scale/2, e.getY()- 5*scale/2);
				else
					pictRef = new PositionnedPicture(picture , e.getX() - picture.getWidth()*scale/32/2, e.getY()- picture.getHeight()*scale/32/2);
				pan.setPP(pictRef);
				
			} 
			else {
				
				Point2D pe = new Point2D.Double(e.getX(),e.getY());
				pe = pan.realMousePos(pe);
				
				if(!pan.verify(pe))
					return;
				
				selectedCorner = pan.getRectangleCornerAt(e.getX(), e.getY());
				if(selectedCorner != null)
					rectRef = pan.getRectangleAt(e.getX(), e.getY());
				else {
					
					Rectangle2D r = new Rectangle2D.Double(e.getX(), e.getY(),32,32);
					rectRef = r;
					selectedCorner = Corner.DOWN_RIGHT;
					pan.addRect(r);
					pan.repaint();
					
				}
				
			}
			
			pan.repaint();
			
		}

	}

	public void mouseReleased(MouseEvent e) {
		
		if(pictRef != null ) {
			
			if(e.getX() - pan.getWidth() + pan.getSP().getPreferredSize().width < 0) {
				pictRef.move(e.getX(), e.getY());
				pan.addPositionnedPicture(pictRef);
			}
			
		}
			
		pan.setPP(null);
		this.pictRef = null;
		
		if(this.rectRef != null) {
			if(rectRef.getHeight()/32 < 3 || rectRef.getWidth()/32 < 3)
				pan.removeRect(rectRef);
		}
		
		this.selectedCorner = null;
		this.rectRef = null;
		
		pan.repaint();
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
		pan.setMousePos(-100, -100);
		
	}

}
