package entity;

import java.awt.Rectangle;

import diplayable.Sprite;

public class Weed extends Non_human{
	
	public Weed(int posX, int posY) {
		super(posX, posY);
		this.maturity_age = (int) (12 + Math.random()*7);
		this.state = 0;
		this.nb_turns_before_spreding = 0;
	}
	
	private int maturity_age;

	private static final String img_path = "weed3state.png";
	private static Sprite weed_sprite = null;
	private int state;
	private boolean killed;
	public int nb_turns_before_spreding;

	// regles
	
	public void clock_tic() {
		
		this.entity_age ++;
		if(entity_age>=0 && entity_age < this.maturity_age) this.state = 0;
		else this.state = 2;
		
		this.nb_turns_before_spreding ++;
		
	}
	
	public boolean spread() {
		if(nb_turns_before_spreding > this.maturity_age*2)
			this.nb_turns_before_spreding = 0;
		return this.entity_age > this.maturity_age && nb_turns_before_spreding == 0;
	}

	public boolean isFunctional(){
		
		return true;
		
	}

	public boolean isDying(){
		
		return killed;
		
	}
	
	public void destroy() {
		this.killed = true;
	}
	
	// affichage
	
	public Sprite get_sprite_info(){
		
		if(Weed.weed_sprite == null)
			Weed.weed_sprite = new Sprite(Weed.img_path);
		
		return Weed.weed_sprite;
		
	}

	public Rectangle selection_sprite(){ // completer
		
		return new Rectangle(0,this.state*28,28,28);
		
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

	// pathfinding
	
	public boolean isSolid() {
		return false;
	}

}
