package diplayable;

import java.awt.Image;
import java.awt.Rectangle;

public abstract class Displayable{ // affichable

	private boolean displayed;

	// en pixels utilisés pour l'affichage
	protected int posX;
	protected int posY;
	
	// les positions sont ici en cases
	public Displayable(int posX, int posY){
		this.posX = posX * 32;
		this.posY = posY * 32;
		this.displayed = false;
	}
	/* *
	 * fuction : get_sprite_info
	 * 
	 * @return le sprite associe a l'entite
	 * */
	public abstract Sprite get_sprite_info();
	
	public Image get_img(){
		
		if(get_sprite_info() != null)
			return get_sprite_info().get_texture();
		else
			return null;
	}
	
	/* *
	 * @return
	 * */
	public boolean isDisplayed() {
		return displayed;
	}
	
	/* *
	 * @return
	 * */
	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}
	
	/* *
	 * function : selection_sprite
	 * 
	 * @return la section de l'image chargee dans le sprite a afficher 
	 * */
	public abstract Rectangle selection_sprite();
	
	public int getPosX(){
		return this.posX;
	}
	
	public int getPosY(){
		return this.posY;
	}
	
	// dimensions du personnage sur la grille
	public abstract int get_width();
	public abstract int get_height();
	
	// decaler les sprites pour qu'ils aient les pieds sur la bonne case
	public abstract int dWidth();
	public abstract int dHeight();
	
}
