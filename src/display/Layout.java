package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import diplayable.Displayable;
import elements.Ground;
import elements.Map;
import entity.Entity;
import entity.House;
import entity.HouseDoor;
import entity.Human;
import entity.Non_human;

public class Layout extends JPanel implements ScaledPan, KeyMovablePan{

	private static final long serialVersionUID = 1L;
	private Map content;
	
	private int layout_scale;
	private int view_pos_x;
	private int view_pos_y;
	
	public Layout(){
		
		this.setScale(32);
		this.view_pos_x = 75;
		this.view_pos_y = 75;
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		int begin_x = (this.view_pos_x*layout_scale/8 - this.getWidth()/2)/layout_scale - 1;
		int begin_y = (this.view_pos_y*layout_scale/8 - this.getHeight()/2)/layout_scale - 1;
		int end_x = begin_x + this.getWidth()/layout_scale + 3;
		int end_y = begin_y + this.getHeight()/layout_scale + 3;
		
		for(int i = begin_x ; i < end_x; i ++) {
			for(int j = begin_y; j < end_y; j++) {
				Ground gr = content.get_ground(i, j);
				Non_human nh = content.get_non_human(i, j);
				Human hu = content.get_human(i, j);
				
				if(gr != null)
					gr.setDisplayed(false);
				
				if(nh != null)
					nh.setDisplayed(false);
				
				if(hu != null)
					hu.setDisplayed(false);
				
			}
		}
				
		//affichage damier
		for(int i = begin_x ; i < end_x; i ++) {
			for(int j = begin_y; j < end_y; j++) {
				
				Ground gr = content.get_ground(i, j);
				
				if(gr != null)
					drawImageAt(gr,g);
				
			}
		}
		
		//affichage non_entites
		for(int i = begin_x ; i < end_x; i ++) {
			for(int j = begin_y; j < end_y; j++) {
				Non_human nh = content.get_non_human(i, j);
				if(nh != null) {
					if(!nh.isDisplayed()) {
						drawImageAt(nh,g);
						nh.setDisplayed(true);
					}
				}
			}
		}
		
		//affichage entites
		for(int i = begin_x ; i < end_x; i ++) {
			for(int j = begin_y; j < end_y; j++) {
				if(content.get_human(i, j) != null) {
					drawImageAt(content.get_human(i, j),g);
				}
			}
		}
		
		//affichage des statistiques
		Graphics2D g2 = (Graphics2D) g;
		
		Toolkit t = Toolkit.getDefaultToolkit();
		//g2.setPaint(new Color(0,0,0,50)); origine
		g2.setPaint(new Color(255,255,255,100));
		g2.fillRect(this.getWidth() - t.getScreenSize().width/5, 0,t.getScreenSize().width,t.getScreenSize().height/7);
		
		g2.setPaint(new Color(0,0,0,200));
		g2.drawString("Statistiques :", this.getWidth() - t.getScreenSize().width/5+20, 20);
		g2.drawString("Stock de nourriture : " , this.getWidth() - t.getScreenSize().width/5+20, 20+2*g2.getFontMetrics().getHeight());
		g2.drawString("Nombre d'hommes vivants : ",  this.getWidth() - t.getScreenSize().width/5+20, 20+3*g2.getFontMetrics().getHeight());
		g2.drawString("Nombre de femmes vivantes : ",  this.getWidth() - t.getScreenSize().width/5+20, 20+4*g2.getFontMetrics().getHeight());
		g2.drawString("Nombre de cases contaminees : ",  this.getWidth() - t.getScreenSize().width/5+20, 20+5*g2.getFontMetrics().getHeight());
		g2.drawString("Nombre d'unites de temps : ",  this.getWidth() - t.getScreenSize().width/5+20, 20+6*g2.getFontMetrics().getHeight());
		
		g2.drawString(String.valueOf(Human.getFoodAmount()), this.getWidth()-100, 20+2*g2.getFontMetrics().getHeight());
		g2.drawString(String.valueOf(content.getNb_of_man()), this.getWidth()-100, 20+3*g2.getFontMetrics().getHeight());
		g2.drawString(String.valueOf(content.getNb_of_woman()), this.getWidth()-100, 20+4*g2.getFontMetrics().getHeight());
		g2.drawString(String.valueOf(content.getNb_of_herb()), this.getWidth()-100, 20+5*g2.getFontMetrics().getHeight());
		g2.drawString(String.valueOf(content.getTime()), this.getWidth()-100, 20+6*g2.getFontMetrics().getHeight());
	}
	
	/* *
	 * function : drawImage
	 * description : dessine correctement le sprite de d selon le contexte dans lequel est le layout.
	 * 
	 * @param d : l'objet affichable a afficher
	 * @param x : la position x sur la grille
	 * @param y : la position y sur la grille 
	 * @param g : dessine sur le layout 
	 * */
	public void drawImageAt(Displayable d, Graphics g) {
		
		if(d != null){		
			int left_top_corner_posX = d.getPosX()*layout_scale/32 + d.dWidth()*layout_scale/32;
			int left_top_corner_posY = d.getPosY()*layout_scale/32 + d.dHeight()*layout_scale/32;
			int scaled_width = d.get_width()*layout_scale/32;
			int scaled_height = d.get_height()*layout_scale/32;

			//decalage du la position due la "camera"
			left_top_corner_posX += this.getWidth()/2 - this.view_pos_x*this.layout_scale/8;
			left_top_corner_posY += this.getHeight()/2 - this.view_pos_y*this.layout_scale/8;
			
			if(d.get_img() != null)
				drawImage(d.get_img(), new Rectangle(left_top_corner_posX, left_top_corner_posY, scaled_width, scaled_height), d.selection_sprite(), g);
			
		}
		
	}
	
	/* *
	 * function : drawImage
	 * description : dessine la section d'une image donnee, dans une zone donnee du Layout
	 * attention : compliquee, ne doit pas etre appelee ailleur que dans drawImageAt
	 * 
	 * @param img  : image a afficher 
	 * @param dest : zone cible de l'ecran ou sera afficher l'image
	 * @param src  : la section de l'image a afficher
	 * @param g    : dessine sur le layout 
	 * */
	public void drawImage(Image img, Rectangle dest, Rectangle src, Graphics g){
		
		g.drawImage(img, dest.x, dest.y, dest.x + dest.width, dest.y + dest.height, src.x, src.y, src.x + src.width, src.y + src.height, this);
		
	}

	public int getScale(){
		
		return layout_scale;
		
	}
	
	public void setScale(int layout_scale){
		
		this.layout_scale = layout_scale;
		
	}
	
	public void next_turn(int duration) throws Exception {
		this.content.next_turn(duration);
	}
	
	
	public void setMap(Map m) {
		this.content = m;
	}
	public void moveView(int x, int y) {
		this.view_pos_x += x;
		this.view_pos_y += y;
	}
	
}
