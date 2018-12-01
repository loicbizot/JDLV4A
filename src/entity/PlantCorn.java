package entity;

import java.awt.Rectangle;

import diplayable.Sprite;

public class PlantCorn extends Non_human{

	public PlantCorn(int posX, int posY) {
		super(posX, posY);
	}

	public boolean spread() {
		return false;
	}

	public boolean isSolid() {
		return false;
	}

	public void destroy() {
		
	}

	public void clock_tic() {
		
	}

	public boolean isFunctional() {
		return true;
	}

	public boolean isDying() {
		return false;
	}

	public Sprite get_sprite_info() {
		return null;
	}

	public Rectangle selection_sprite() {
		return new Rectangle(0,0,0,0);
	}

	public int get_width() {
		return 0;
	}

	public int get_height() {
		return 0;
	}

	public int dWidth() {
		return 0;
	}

	public int dHeight() {
		return 0;
	}

}
