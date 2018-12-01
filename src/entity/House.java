package entity;

import java.awt.Rectangle;

import diplayable.Sprite;

public class House extends Non_human{
	
	public House(int posX, int posY) {
		super(posX, posY);
		father = null;
		mother = null;
		child = null;
		this.full = false;
		this.active = false;
		this.nb_of_men = 0;
		this.nb_of_women = 0;
	}

	private static final String img_path = "house.png";
	private static Sprite house_sprite = null;
	
	private Man father;
	private Woman mother;
	private Human child;
	
	private boolean full;
	private boolean active;

	private int nb_of_men;
	private int nb_of_women;
	
	public int getNb_of_men() {
		return nb_of_men;
	}

	public int getNb_of_women() {
		return nb_of_women;
	}

	// affichage
	
	public Sprite get_sprite_info(){
		
		if(House.house_sprite == null)
			House.house_sprite = new Sprite(House.img_path);
		
		return House.house_sprite;
		
	}
	
	public Rectangle selection_sprite() {
		if(!full) {
			return new Rectangle(0,0,216,218);
		}
		else {
			return new Rectangle(0,218,216,218);
		}
		
	}

	public int get_width() {
		return 32*5;
	}

	public int get_height() {
		return 32*5;
	}

	public int dWidth() {
		return 0;
	}

	public int dHeight() {
		return 0;
	}

	public void clock_tic() {
		if(this.father != null && this.mother != null)
			this.full = true;
		if(father != null)
			father.checkPartner();
		if(mother != null)
			mother.checkPartner();
	}
	
	// regles
	
	public boolean spread() {
		return false;
	}
	
	public boolean isFunctional() {
		return !this.isMarked() && !active;
	}

	public boolean isDying() {
		return false;
	}
	
	public boolean enterHouse(Human enteringHuman) throws Exception {
		
		this.active = true;
		
		if(full) {
			System.err.println("Erreur, entree alors que la maison est pleine marked=" + isMarked());
			throw new Exception();
		}
		
		if(enteringHuman.getClass() == Man.class) {
			
			if(this.father != null)
				return false;
			else {
				this.father = (Man)enteringHuman;
				nb_of_men ++;
				return true;
			}
				
		}
		else {
			
			if(this.mother != null)
				return false;
			else {
				this.mother = (Woman)enteringHuman;
				nb_of_women ++;
				return true;
			}
				
		}
		
	}
	
	public boolean isFull() {
		return full;
	}
	
	public Human exitHouse() {
		
		Human result = null;
		
		// si tout le monde est sorti 
		if(this.mother == null && this.father == null && this.child == null && !active && full) {
			this.full = false;
			this.unmark();
		}

		if((this.entity_age > 1 && father != null) || (father != null && !father.hasPartner())) {
			
			if(!father.hasPartner())
				this.active = false;
			
			result = this.father;
			this.father.setPartner(null);
			this.father = null;
			nb_of_men -- ;
			
		}
		else if((this.entity_age > 5 && mother != null) || (mother != null && !mother.hasPartner())) {

			if(!mother.hasPartner())
				this.active = false;
			
			result = this.mother;
			this.mother.setPartner(null);
			this.mother.resetCount();
			this.mother = null;
			nb_of_women --;
			
		}
		else if(this.entity_age > 10 && child != null) {
			
			result = this.child;
			active = false;
			this.child = null;
			nb_of_men = 0;
			nb_of_women = 0;
			this.entity_age = 0;
			
		}
		
		return result;
		
	}
	
	public void updateHouse() {
	
		// naissance humain
		if(this.entity_age == 4 && this.child == null) {
		
			int rand = (int) (Math.random()*2);
			
			if( rand == 0) {
				child = new Man(this.posX/32 + 2, this.posY/32 + 4,"Fils");
				nb_of_men ++;
			}
			else {
				child = new Woman(this.posX/32 + 2, this.posY/32 + 4,"Fille");
				nb_of_women ++;
			}
			
		}
		
		if(this.full)
			this.entity_age ++;
		else
			this.entity_age = 0;
		
	}
	
	public void destroy() {
		
	}
	
	// pathfinding

	public boolean isSolid() {
		return true;
	}
	
	public void unmark() {
		
		if(this.isMarked()) {
			System.err.println("La maison est deja demarquee");
		}
		
		super.unmark();
		
		System.out.println("Demarque maison");
		
	}

}
