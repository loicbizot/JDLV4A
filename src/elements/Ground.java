package elements;

import java.awt.Rectangle;

import diplayable.Displayable;
import diplayable.Sprite;

public class Ground extends Displayable{
	private static final String img_path = "ground.png";
	private static Sprite ground_sprite = null;
	private int etat;
	
	public Ground(int posX, int posY) {
		super(posX, posY);
		etat = 0;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public Sprite get_sprite_info(){
		
		if(Ground.ground_sprite == null)
			Ground.ground_sprite = new Sprite(Ground.img_path);
		
		return Ground.ground_sprite;
		
	}

	public Rectangle selection_sprite() {
		return new Rectangle(0,this.etat*32,32,32);
	}

	public int get_width() {
		return 32;
	}

	public int get_height() {
		return 32;
	}

	public int dWidth() {
		return 0;
	}

	public int dHeight() {
		return 0;
	}

	public boolean isTraversable() {
		return true;
	}

}
