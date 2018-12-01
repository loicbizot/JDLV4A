package entity;

import java.awt.Rectangle;

import diplayable.Sprite;

public class HouseDoor extends Non_human{

	House concernedHouse;
	
	public HouseDoor(House concernedHouse) {
		
		super(concernedHouse.getPosX(), concernedHouse.getPosY());
		this.concernedHouse = concernedHouse;
		
	}
	
	public int getNb_of_men() {
		return concernedHouse.getNb_of_men();
	}

	public int getNb_of_women() {
		return concernedHouse.getNb_of_women();
	}

	public void clock_tic() {
		this.concernedHouse.clock_tic();
		this.concernedHouse.updateHouse();
	}

	public boolean isFunctional() {
		return concernedHouse.isFunctional();
	}

	public boolean isDying() {
		return false;
	}
	
	public boolean enterHouse(Human human) throws Exception {
		return this.concernedHouse.enterHouse(human);
	}
	
	public Human exitHouse() {
		return this.concernedHouse.exitHouse();
	}

	public Sprite get_sprite_info() {
		return this.concernedHouse.get_sprite_info();
	}

	public Rectangle selection_sprite() {
		return concernedHouse.selection_sprite();
	}

	public int get_width() {
		return concernedHouse.get_width();
	}

	public int get_height() {
		return concernedHouse.get_height();
	}

	public int dWidth() {
		return concernedHouse.dWidth();
	}

	public int dHeight() {
		return concernedHouse.dHeight();
	}

	public boolean spread() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public void destroy() {
		
	}
	
	public boolean isDisplayed(){
		return this.concernedHouse.isDisplayed();
	}
	
	public void setDisplayed(boolean displayed){
		this.concernedHouse.setDisplayed(displayed);
	}
	
	public boolean isMarked() {
		return this.concernedHouse.isMarked();
	}
	
	public void mark() {
		this.concernedHouse.mark();
	}
	
	public void unmark() {
		this.concernedHouse.unmark();
	}

}
