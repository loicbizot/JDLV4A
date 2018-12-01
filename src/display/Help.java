package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Help extends JPanel{
	
	private Image background;
	private Image back_text;
	private Image button_img;
	private Image produced;
	
	public Help(){
				
		try {
			background = ImageIO.read(getClass().getResource("/paysage.jpg"));
			button_img = ImageIO.read(getClass().getResource("/wooden_plank.png"));
			back_text = ImageIO.read(getClass().getResource("/play_text.png"));
			produced = ImageIO.read(getClass().getResource("/producedby.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g.drawImage(background,0,0,this.getWidth(), this.getHeight(),this);
		
		/*Affichage de deux textes sur deux lignes consecutives*/
		String pres = "Bienvenue sur la page qui va vous donner tous les elements pour pouvoir jouer au Jeu de la vie !";
		String s1 = "	Lorsque vous lancez le jeu en appuyant sur \"Play\" un ecran d'initialisation apparait."
				+ " Vous devez ensuite placer les champs de ble en fesant des cliquer-glisser. Le nombre maximal de champs est limite a  3. Vous devez,"
				+ " de la meme maniere, placer les maisons et les humains. Enfin il vous sera demander de placer le point de depart de la mauvaise herbe.";
		String s2 = "Voici maintenant quelques commandes utiles : ";
		String s3 = "Pause : ";
		String s4 = "Deplacement sur la carte : ";
		String s5 = "Zoom : ";		
		
		String indic_s3 = "Touche P";
		String indic_s4 = "Touches directionnelles";
		String indic_s4_2 = "Cliquer-glisser";
		String indic_s5 = "Molette de la souris";
		
		FontMetrics fm = g2.getFontMetrics();
		int longueur = fm.stringWidth(s4);
		int height = fm.getHeight();
	
		
		g2.setPaint(Color.BLACK);
		int marge = 50;
		g2.drawString(pres,marge,100);
		
		ArrayList<String> s1_bis = cut_string(s1,7);
		int line = 1;
		for(String s : s1_bis){
			g2.drawString(s, marge, 100+(2+line)*height);
			line++;
		}		
		
		g2.drawString(s2, marge, 108+(line+4)*height);
		g2.drawString(s3, marge, 108+(line+6)*height);
		g2.drawString(s4, marge, 108+(line+8)*height);
		g2.drawString(s5, marge, 108+(line+11)*height);
		
		g2.drawString(indic_s3,400+longueur,108+(line+6)*height);
		g2.drawString(indic_s4,400+longueur,108+(line+8)*height);
		g2.drawString(indic_s4_2,400+longueur,108+(line+9)*height);
		g2.drawString(indic_s5,400+longueur,108+(line+11)*height);
		
		g2.setPaint(Color.BLACK);
		
		Rectangle2D actions = new Rectangle2D.Double(marge-10, 103+(line+5)*height, 500, 7*height);
		g2.draw(actions);
		
		Rectangle2D touches = new Rectangle2D.Double(marge-10+500, 103+(line+5)*height, 200, 7*height);
		g2.draw(touches);
		
		Line2D line1 = new Line2D.Double(marge-10, 103+(line+7)*height, marge-10+700, 103+(line+7)*height);
		g2.draw(line1);
		
		Line2D line2 = new Line2D.Double(marge-10, 103+(line+10)*height, marge-10+700, 103+(line+10)*height);
		g2.draw(line2);
		
		g.drawImage(produced,marge,100+(line+15)*height,410,28,this);
		
	}
	
	public ArrayList<String> cut_string(String s, int width_font){
		//Recuperation des dimensions de la fenetre
		Dimension dimension = this.getSize();
		int height_screen = (int)dimension.getHeight();
		int width_screen  = (int)dimension.getWidth();

		ArrayList<String> res = new ArrayList<String>();

		String[] tab_s1 = s.split(" ");
		int nb_char = 0;
		int nb_line = 0;
		int j = 0;
		String res_aux = "        ";

		for(int i = 0; i<tab_s1.length;i++){
			for(char c : tab_s1[i].toCharArray()){
				nb_char++;
			}
			nb_char++; // espace entre chaque mots
			if(nb_char*width_font > width_screen-50){ //-50 pour la marge
				while(j<i){
					res_aux = res_aux + tab_s1[j] + " ";
					j++;
				}
				res.add(res_aux);
				nb_line++;
				res_aux = "";
				nb_char = 0;
			}else if(i == tab_s1.length-1){
				while(j<=i){
					res_aux= res_aux + tab_s1[j] + " ";
					j++;
				}
				res.add(res_aux);
			}
		}
		
		return res;
	}
	

}