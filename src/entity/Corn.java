package entity;

import java.awt.Rectangle;

import diplayable.Sprite;

public class Corn extends Non_human{
	
	public Corn(int posX, int posY) {
		super(posX, posY);
		this.state = 0;
		this.killed = false;
	}

	private static final String img_path = "corn.png";
	private static Sprite corn_sprite = null;
	private int state;
	private boolean killed;

	// regles
	
	public void clock_tic(){
		
		this.entity_age ++;
		if(entity_age>=0 && entity_age <= 7) this.state = 0;
		else if(entity_age>=8 && entity_age <= 14) this.state = 1;
		else if(entity_age>=25 && entity_age <= 30) this.state = 2;
		
	}

	public boolean isFunctional(){
		
		return this.entity_age >= 25;
		
	}

	public boolean isDying(){
		
		return /*this.entity_age >= 100 ||*/ killed;
		
	}
	
	public boolean spread() {
		return false;
	}
	
	public void destroy() {
		this.killed = true;
	}
	
	public void init() {
		this.entity_age = 25;
		this.state = 2;
	}

	// affichage
	
	public Sprite get_sprite_info(){
		
		if(Corn.corn_sprite == null)
			Corn.corn_sprite = new Sprite(Corn.img_path);
		
		return Corn.corn_sprite;
		
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
