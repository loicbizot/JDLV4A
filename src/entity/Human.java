package entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import elements.Map.Coord;

public abstract class Human extends Entity{
	
	public enum Orientation{
		DOWN,
		LEFT,
		RIGHT,
		TOP
	}
	
	private String name;
	
	// regles du jeu
	private int age_of_death;
	private static int food_amount = 2000;
	
	// affichage
	public static int getFoodAmount(){
		return food_amount;
	}
	
	// remise a zero
	public static void initFoodAmount() {
		food_amount = 2000;
	}
	
	private int saciety;
	
	// pathfinding
	private Non_human target;
	protected ArrayList<Coord> path;
	
	// bloquage
	private int time_bloqued;
	
	// variables animation
	private Thread animation;
	private int animation_step;
	private Orientation caracter_orientation;
	private Action caracter_action;
	
	// temps d'animations
	private static final int animation_time_cut_corn = 2;
	private static final int animation_time_cut_weed = 2;
	private static final int animation_time_plant_corn = 2;
	
	// occupation a faire une action
	private boolean busy;
	private int step_busy;
	
	// Constructeur
	// posX et posY a donner en cases
	public Human(int posX,int posY, String name) {
		super(posX,posY);
		
		this.name = name;
		
		this.saciety = 100;
		
		// calcul de l age de la mort aleatoire
		int random_age = 450 + (int)(Math.random()*101);
		this.age_of_death = random_age;
		
		// orientation par default
		this.caracter_orientation = Orientation.DOWN;
		
		// animation
		this.animation_step = 0;
		
		// pathfinding
		this.target = null;
		
		// action
		this.busy = false;
		this.step_busy = 0;
		
		// debloquage
		this.time_bloqued = 0;
		
	}
	
	// affichage
	
	public void verifyCoords(int x, int y) throws Exception {
		if(x != this.posX/32 || y != this.posY/32) {
			System.err.println("dx = " + (posX-x*32) + " dy = " + (posY-y*32) );
			throw new Exception();
		}
	}
	
	public int getPosX(){
		return this.posX;
	}
	
	public int getPosY(){
		return this.posY;
	}
	
	public int dWidth() {
		return 0;
	}

	public int dHeight() {
		return -24;
	}
	
	public Rectangle selection_sprite(){
		
		return new Rectangle(32 * this.animation_step, 48 * this.caracter_orientation.ordinal(), 32, 48);
		
	}
	
	// regles du jeu :
	
	public void clock_tic(){
		
		// augmenter l'age de l'entite
		this.entity_age ++;
		// diminuer sa saciete
		this.saciety --;
		if(saciety < 20) {
			int amount = Math.min(100 - saciety, food_amount);
			food_amount -= amount;
			saciety += amount;
		}
		
	}
	
	public Orientation get_orientation(){
		return caracter_orientation;
	}
	
	// Actions :
	
	public void setBusy(boolean b) {
		this.busy = b;
	}
	
	public boolean isBusy() {
		return busy;
	}
	
	public void cut_corn(){
		
		int dx = this.target.getPosX()/32 - this.posX/32;
		int dy = this.target.getPosY()/32 - this.posY/32;
		
		if(dx == 1){
			this.caracter_orientation = Orientation.RIGHT;
		}else if(dx == -1){
			this.caracter_orientation = Orientation.LEFT;
		}else if(dy == 1){
			this.caracter_orientation = Orientation.DOWN;
		}else{
			this.caracter_orientation = Orientation.TOP;
		}
		
		if(!this.busy){
			this.busy = true;
		}
		else{
			if(step_busy == this.animation_time_cut_corn){
				
				this.busy = false;
				this.step_busy = 0;
				this.target.destroy();
				food_amount += 20;
				this.unTarget();
				this.path = null;
				
			}
			else {
				this.step_busy ++;
			}
		}
		
	}
	
	public void plant_corn(){
		
		if(this.target == null) {
			System.err.println("erreur : target indefinie");
			return;
		}
		
		int dx = this.target.getPosX()/32 - this.posX/32;
		int dy = this.target.getPosY()/32 - this.posY/32;
		
		if(dx == 1){
			this.caracter_orientation = Orientation.RIGHT;
		}else if(dx == -1){
			this.caracter_orientation = Orientation.LEFT;
		}else if(dy == 1){
			this.caracter_orientation = Orientation.DOWN;
		}else{
			this.caracter_orientation = Orientation.TOP;
		}
		
		if(!this.busy){
			this.busy = true;
		}
		else{
			if(step_busy == this.animation_time_plant_corn){
				
				this.busy = false;
				this.step_busy = 0;
				this.target.destroy();
				this.unTarget();
				path = null;
				this.caracter_action = Action.NONE;
				
			}
			else {
				this.step_busy ++;
			}
		}
		
	}
	
	public boolean corn_ready(){
		return this.step_busy == this.animation_time_plant_corn -1;
	}
	
	public void cut_weed(){
		
		int dx = this.target.getPosX()/32 - this.posX/32;
		int dy = this.target.getPosY()/32 - this.posY/32;
		
		if(dx == 1){
			this.caracter_orientation = Orientation.RIGHT;
		}else if(dx == -1){
			this.caracter_orientation = Orientation.LEFT;
		}else if(dy == 1){
			this.caracter_orientation = Orientation.DOWN;
		}else{
			this.caracter_orientation = Orientation.TOP;
		}
		
		if(!this.busy){
			this.busy = true;
		}
		else{
			if(step_busy == this.animation_time_cut_weed){
				
				this.busy = false;
				this.step_busy = 0;
				this.target.destroy();
				this.unTarget();
				path = null;
				this.caracter_action = Action.NONE;
				
			}
			else {
				this.step_busy ++;
			}
		}
		
	}
	
	public void enter_house(){
		this.caracter_orientation = Orientation.TOP;
		this.path = null;
	}
	
	public void exit_house() {
		this.target = null;
		this.caracter_orientation = Orientation.DOWN;
		this.caracter_action = Action.LEAVE_HOUSE;
	}
	
	public Action chose_action() {
		
		// choix de l'action en fonction du path
			
		if(path != null){
			if(path.size() == 1){
				
				if(this.target instanceof Corn)
					this.caracter_action = Action.CUT_CORN;
				if(this.target instanceof Weed)
					this.caracter_action = Action.CUT_WEED;
				if(this.target instanceof PlantCorn) 
					this.caracter_action = Action.PLANT_CORN;
				if(this.target instanceof HouseDoor)
					this.caracter_action = Action.ENTER_HOUSE;
				
			}
			else{
				
				Coord c = path.get(0);
				int dx = c.getX() - this.posX/32;
				int dy = c.getY() - this.posY/32;
				
				if(dx == 1){
					this.caracter_action = Action.GO_RIGHT;
				}else if(dx == -1){
					this.caracter_action = Action.GO_LEFT;
				}else if(dy == 1){
					this.caracter_action = Action.GO_DOWN;
				}else if(dy == -1){
					this.caracter_action = Action.GO_TOP;
				}else{
					this.caracter_action = Action.NONE;
					System.err.println("Erreur Path : dx = " + dx + ", dy = " + dy);
					System.err.println(path);
					System.err.println(this.target.getClass());
				}
				
			}
			
		}
		else
			this.caracter_action = Action.NONE;
		
		//
		
		switch(caracter_action) {
		
		case GO_LEFT:
			this.caracter_orientation = Orientation.LEFT;
			break;
		case GO_RIGHT:
			this.caracter_orientation = Orientation.RIGHT;
			break;
		case GO_TOP:
			this.caracter_orientation = Orientation.TOP;
			break;
		case GO_DOWN:
			this.caracter_orientation = Orientation.DOWN;
			break;
		case CUT_CORN:	
			this.cut_corn();
			break;
		case CUT_WEED:
			this.cut_weed();
			break;
		case PLANT_CORN:
			this.plant_corn();
			break;
		case ENTER_HOUSE:
			this.enter_house();
			break;
		default:
			break;
			
		}
		
		return this.caracter_action;
		
	}
	
	public boolean isFunctional(){
		
		return this.entity_age >= 5 && !this.hasPartner();
		
	}
	
	public boolean isDying(){
		
		return this.entity_age >= age_of_death || this.saciety <= 0;
		
	}
	
	// appelle pour verifier si le partenaire est toujours en vie
	public abstract boolean checkPartner();
	
	// appelle si le partenaire meurt, ou bloquage
	public abstract void rapture();
	
	public abstract boolean hasPartner();
	
	public abstract void setPartner(Human partner);
	
	// animation
	
	public boolean isEnteringHouse() {
		return this.caracter_action == Action.ENTER_HOUSE;
	}
	
	public void animer(int duration) {

		if(this.caracter_orientation != null && ((this.caracter_action.ordinal() >= 0 && this.caracter_action.ordinal() < 4) || this.caracter_action == Action.ENTER_HOUSE || this.caracter_action == Action.LEAVE_HOUSE)) {
			animation = new Thread(new AnimationDeplacement(duration));
			animation.start();
		}
		
	}
	
	public void wait_end_of_animation() {
		if(animation != null) {
			try {
				animation.join();
				animation = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.nextStepOnPath();	
		}
	}
	
	public class AnimationDeplacement implements Runnable{

		private int duration;
		
		public AnimationDeplacement( int duration) {
			this.duration = duration;
		}
		
		public void run() {
			
			for(int i = 0; i < 32; i++) {
				
				try {
					Thread.sleep(duration/32);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				animation_step = i*4/32;
				switch(caracter_orientation){
				
				case DOWN:
					posY ++;
					break;
				case LEFT:
					posX --;
					break;
				case RIGHT:
					posX ++;
					break;
				case TOP:
					posY --;
					break;
				
				}
				
			}
			animation_step = 0;
			
		}
		
	}
	
	// pathfinding
	
	public void setPath(ArrayList<Coord> path) {
		this.path = path;
		if(this.path != null)
			this.path.remove(0);
	}
	
	public void nextStepOnPath() {
		if(path != null)
			path.remove(0);
	}
	
	// deciblage, utilise en cas de bloquage
	public void unTarget() {
		if(this.target != null) {
			this.target.unmark();
			this.target = null;
			this.path = null;
			this.caracter_action = Action.NONE;
		}
	}
	
	public void setTarget(Non_human target) {
		this.target = target;
	}
	
	public boolean hasTarget() {
		return this.target != null;
	}
	
	// verifie si la cible est toujours vivante
	public boolean checkTarget(){
		if(this.target.isDying()){
			this.target = null;
			this.path = null;
			this.busy = false;
			this.caracter_action = Action.NONE;
			this.step_busy = 0;
			return false;
		}
		else
			return true;
	}
	
	public Class<? extends Non_human> getTargetClass(){
		if(this.target == null) {
			return null;
		}
		else {
			return this.target.getClass();
		}
	}
	
	//appelle en cas de bloquage
	
	public void increase_time_bloqued() {
		this.time_bloqued ++;
	}
	
	public void reset_time_bloqued() {
		this.time_bloqued = 0;
	}
	
	public boolean is_bloqued() {
		return this.time_bloqued > 5;
	}
	
}
