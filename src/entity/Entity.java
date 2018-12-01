package entity;

import diplayable.Displayable;

public abstract class Entity extends Displayable{
	
	protected int entity_age;
	
	public Entity(int posX, int posY){
		super(posX,posY);
		this.entity_age = 0;
		
	}

	public abstract void clock_tic();
	public abstract boolean isFunctional();
	public abstract boolean isDying();
	
}
