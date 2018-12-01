package entity;

import diplayable.Sprite;

public class Woman extends Human{
	
	public Woman(int posX, int posY, String name) {
		super(posX, posY, name);
		this.count = 0;
	}

	private static final String img_path = "woman.png";
	private static Sprite woman_sprite = null;
	private Man husband;
	private int count;

	// affichage
	
	public Sprite get_sprite_info(){
		
		if(Woman.woman_sprite == null)
			Woman.woman_sprite = new Sprite(Woman.img_path);
		
		return Woman.woman_sprite;
		
	}

	public int get_width() {
		return 32;
	}

	public int get_height() {
		return 48;
	}

	// regles
	
	public void clock_tic() {
		super.clock_tic();
		this.count ++;
	}
	
	public boolean isFunctionnal() {
		return true;
	}
	
	public void resetCount() {
		this.count = 0;
	}
	
	public boolean checkPartner() {
		if(this.husband == null)
			return true;
		if(this.husband.isDying()){
			this.husband = null;
			this.path = null;
			this.unTarget();
			return false;
		}
		else
			return true;
	}
	
	public boolean hasPartner() {
		return this.husband != null;
	}

	public void setPartner(Human partner) {
		this.husband = (Man)partner;
	}

	
	public void rapture() {
		if(this.hasPartner()) {
			
			Human partner = this.husband;
			this.setPartner(null);
			partner.rapture();
			
			path = null;
			setTarget(null);
			
		}
	}
	
}
