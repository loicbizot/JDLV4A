package entity;

import diplayable.Sprite;

public class Man extends Human{
	
	public Man(int posX, int posY, String name) {
		super(posX, posY, name);
	}

	private static final String img_path = "man.png";
	private static Sprite man_sprite = null;
	private Woman wife;
	
	// affichage
	
	public Sprite get_sprite_info(){
		
		if(Man.man_sprite == null)
			Man.man_sprite = new Sprite(Man.img_path);
		
		return Man.man_sprite;
		
	}

	public int get_width() {
		return 32;
	}

	public int get_height() {
		return 48;
	}

	// regles
	
	public boolean checkPartner() {
		if(!this.hasPartner())
			return true;
		if(this.wife.isDying()){
			this.wife = null;
			this.path = null;
			this.unTarget();
			return false;
		}
		else
			return true;
	}
	
	public boolean hasPartner() {
		return this.wife != null;
	}

	public void setPartner(Human partner) {
		this.wife = (Woman)partner;
	}

	@Override
	public void rapture() {
		if(this.hasPartner()) {
			
			Human partner = this.wife;
			this.setPartner(null);
			partner.rapture();
			
			path = null;
			setTarget(null);
			
		}
		
	}

}
