package elements;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import entity.Action;
import entity.Corn;
import entity.Entity;
import entity.House;
import entity.HouseDoor;
import entity.Human;
import entity.Man;
import entity.Non_human;
import entity.PlantCorn;
import entity.Weed;
import entity.Woman;

public class Map{

	private Ground[][] map_Grounds;
	private Rectangle2D[] crops;
	
	// a afficher
	private Non_human[][] map_non_human;
	private Human[][] map_human;
	
	// buffers
	private Non_human[][] map_buffer_non_human;
	private Human[][] map_buffer_human;
	
	private int width;
	private int height;
	
	// available for pathfinding
	private int nb_of_corn_available;
	private int nb_of_house_available;
	private int nb_of_herb_available;
	
	private int nb_of_herb;
	private int nb_of_man;
	private int nb_of_woman;

	private int time;

	public int getNb_of_herb() {
		return nb_of_herb;
	}

	public int getNb_of_man() {
		return nb_of_man;
	}

	public int getNb_of_woman() {
		return nb_of_woman;
	}
	
	public int getTime() {
		return time;
	}

	public Map(int width, int height){
		
		this.width = width;
		this.height = height;
		this.nb_of_corn_available = 0;
		this.nb_of_house_available = 0;
		this.nb_of_herb_available = 0;
		this.nb_of_herb = 0;
		this.nb_of_man = 0;
		this.nb_of_woman = 0;
		this.time = 0;
		this.crops = new Rectangle2D[3];
		this.map_human = new Human[width][height];
		this.map_non_human = new Non_human[width][height];
		this.map_buffer_human = new Human[width][height];
		this.map_buffer_non_human = new Non_human[width][height];
		this.map_Grounds = new Ground[width][height];
		for(int i= 0; i < width; i++)
			for(int j=0; j < height; j++)
				map_Grounds[i][j] = new Ground(i,j);
		
	}
	
	public void addWoman(int x, int y) {
		
		this.nb_of_woman ++;
		this.map_human[x][y] = new Woman(x,y,"InitGeneratedWoman");
		
	}
	
	public void addMan(int x, int y) {
		
		this.nb_of_man ++;
		this.map_human[x][y] = new Man(x,y,"InitGeneratedMan");
		
	}
	
	public void addWeed(int x, int y) {
		
		this.nb_of_herb ++;
		this.nb_of_herb_available ++;
		this.map_non_human[x][y] = new Weed(x,y);
		
	}
	
	public void generateCrops() {
		
		for(int i = 0; i < width; i ++) {
			for(int j = 0; j < height; j ++){
				for(Rectangle2D crop:crops){
					if(crop != null && crop.contains(i,j)){
						Corn c = new Corn(i,j);
						c.init();
						this.map_non_human[i][j] = c;
						this.map_Grounds[i][j].setEtat(1);
						this.nb_of_corn_available ++;
					}
				}
				
			}
		}
		
	}
	
	public void addRect(int i, Rectangle2D r) {
		this.crops[i] = r;
	}
	
	public Human get_human(int x,int y){
		if(0 <= x && x < this.width && 0 <= y && y < this.height)
			return this.map_human[x][y];
		else
			return null;
	}
	
	public Non_human get_non_human(int x, int y){
		if(0 <= x && x < this.width && 0 <= y && y < this.height)
			return this.map_non_human[x][y];
		else
			return null;
	}
	
	public Ground get_ground(int x, int y){
		
		if(0 <= x && x < this.width && 0 <= y && y < this.height)
			return this.map_Grounds[x][y];
		else 
			return null;
		
	}
	
	public void set_ground(int x, int y, Ground c){
		
		this.map_Grounds[x][y] = c;
		
	}
	
	// retourne si la maison a bien ete placee
	public boolean put_an_house(int x, int y){
		
		// verifier les bords
		if(x < 0 || x >= this.width || y < 0 || y >= this.height)
			return false;
		
		boolean something_found = false;
		
		// chercher si il y a deja un element
		for(int i = 0; i < 5; i ++){
			for(int j = 0; j < 5; j++){
				
				if(this.map_human[x+i][y+j] != null 
						|| !this.map_Grounds[x+i][y+j].isTraversable())
					something_found = true;
				
			}
		}
		
		// si rien ne bloque
		if(!something_found){
			
			House h = new House(x,y);
			
			for(int i = 0; i < 5; i ++){
				for(int j = 0; j < 5; j++){
					
					if(this.map_non_human[x+i][y+j] != null)
						decreaseAmount(this.map_non_human[x+i][y+j].getClass());	
						
					this.map_non_human[x+i][y+j] = h;
					
				}
			}
			
			this.map_non_human[x+2][y+4] = new HouseDoor(h);
			
			this.nb_of_house_available ++;
			
		}
		
		return !something_found;
		
	}
	
	public boolean isInBound(int x, int y) {
		return 0 <= x && x < this.width && 0 <= y && y < this.height;
	}
	
	public boolean isAvailable(int x, int y, Class<? extends Entity> type) {
		
		boolean result = (this.map_non_human[x][y] != null 
				&& !map_non_human[x][y].isMarked() 
				&& this.map_non_human[x][y].getClass() == type 
				&& map_non_human[x][y].isFunctional())
				||
				(this.map_human[x][y] != null 
				&& !map_human[x][y].hasPartner() 
				&& this.map_human[x][y].getClass() == type 
				&& map_human[x][y].isFunctional()
				&& !map_human[x][y].isBusy());
		
		return result;
		
	}
	
	public boolean isNotSolid(int x, int y) {
		return this.map_non_human[x][y] == null || !this.map_non_human[x][y].isSolid();
	}
	
	public void increaseAmount(Class<?> type) {
		if(type == Corn.class)
			this.nb_of_corn_available ++;
		if(type == Weed.class)
			this.nb_of_herb_available ++;
		if(type == HouseDoor.class)
			this.nb_of_house_available ++;
	}
	
	public void decreaseAmount(Class<?> type) {
		if(type == Corn.class)
			this.nb_of_corn_available --;
		if(type == Weed.class)
			this.nb_of_herb_available --;
		if(type == HouseDoor.class)
			this.nb_of_house_available --;
	}
	
	public boolean inCrops(int x, int y){
		
		boolean found = false;
		for(Rectangle2D r: crops){
			if(r != null && r.contains(x, y))
				found = true;
		}
		return found;
		
	}
	
	/* *
	 * @method next_trun
	 * @brief passe d'un tour a l autre
	 * @arg duration : duree d un tour au minimun, la longueur varie en fonction du nombre de donnees a traiter 
	 * */
	public void next_turn(int duration) throws Exception {
		
		System.out.println("tour " + time + ": " + "reserve nouriture : " + Human.getFoodAmount() );
		System.out.println("maisons libres : " + nb_of_house_available);
		System.out.println("nombre de ble libre : " + nb_of_corn_available);
		System.out.println("nombre d'herbe libre : " + nb_of_herb);
		
		// grille pour la gestion de collision
		boolean grid_d[][] = new boolean[this.width][this.height];
		
		// initialisation de la grille et des buffers.
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				
				grid_d[i][j] = false; // quelles entites se deplacent
				this.map_buffer_human[i][j] = null; // netoyer les buffer
				this.map_buffer_non_human[i][j] = null;
				
			}
		}
		
		//mort des humains
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				
				if( this.map_human[i][j] != null) {
					
					//choisir une direction
					this.map_human[i][j].clock_tic();
					if(this.map_human[i][j].isDying()) {
						
						if(this.map_human[i][j] instanceof Man)
							this.nb_of_man --;
						else
							this.nb_of_woman --;
						
						this.map_human[i][j].unTarget(); // decibler la ressource cible
						this.map_human[i][j] = null; // TUER l'humain
						
						
					}
					
				}
				
			}
		}
		
		// bloquages, verification cible, partenaire
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				
				// les humains
				if( this.map_human[i][j] != null) {
						
					// verifie si le partenaire est encore en vie
					if(this.map_human[i][j].hasTarget()) {
						this.map_human[i][j].checkTarget();
						this.map_human[i][j].checkPartner();
					}
					
					// gestion du bloquage
					if(this.map_human[i][j].is_bloqued()) {
						
						// un non humain disponible en plus
						this.increaseAmount(this.map_human[i][j].getTargetClass());
						
						this.map_human[i][j].unTarget();
						this.map_human[i][j].rapture();
					}
				}
			}
		}
		
		// trouver une cible pour chaque humain
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
							
				// les humains
				if( this.map_human[i][j] != null) {
					
					// l'humain doit trouver un chemin a suivre s'il n'en a pas
					if(!this.map_human[i][j].hasTarget()){
						
						//choix d'une cible
						this.map_human[i][j].setPath(null);
						ArrayList<Coord> path = null;
						
						if(this.map_human[i][j].isFunctional())
							path = this.path_to_the_nearest_non_human(i, j, HouseDoor.class);
						
						// si on trouve un path pour une maison
						if(path != null) {
							
							// coords de la maison
							int xHouse = path.get(path.size()-1).x;
							int yHouse = path.get(path.size()-1).y;
							HouseDoor targetDoor = (HouseDoor) this.map_non_human[xHouse][yHouse];
							
							// recherche partenaire
							Class<?extends Human> oppositeSexe = null;
							if(this.map_human[i][j].getClass() == Man.class) 
								oppositeSexe = Woman.class;
							else if(this.map_human[i][j].getClass() == Woman.class)
								oppositeSexe = Man.class;
							else {
								System.err.println("Erreur l'humain n'a pas de sexe ?");
							}
							
							ArrayList<Coord> pathToTheHouse = null;
							if(oppositeSexe != null)
								pathToTheHouse = this.path_to_the_nearest_non_human(xHouse, yHouse, oppositeSexe);
							
							if(pathToTheHouse != null) {
								
								ArrayList<Coord> pathReversed = new ArrayList<>();
								for(Coord c:pathToTheHouse) {
									pathReversed.add(0,c);
								}
								
								int xHuman = pathReversed.get(0).x;
								int yHuman = pathReversed.get(0).y;
								
								Human partner = this.map_human[xHuman][yHuman];
								
								if(partner.getPosX()/32 != xHuman || partner.getPosY()/32 != yHuman)
									System.err.println("Erreur coord partenaire");
									
								partner.setPartner(this.map_human[i][j]);
								this.map_human[i][j].setPartner(partner);
								partner.unTarget();
								partner.setPath(pathReversed);
								partner.setTarget(targetDoor);
								
							}
							else {
								path = null;
							}
						} // fin recherche maison
						
						// si il n a pas trouve de maison : herbe ou ble ou planter du ble
						if(path == null) {
							
							ArrayList<Coord> pathWeed = this.path_to_the_nearest_non_human(i, j, Weed.class);
							ArrayList<Coord> pathCorn = this.path_to_the_nearest_non_human(i, j, Corn.class);	
							ArrayList<Coord> pathPlantCorn = this.path_to_the_nearest_non_human(i, j, PlantCorn.class);
							
							// un path ne peut pas avoir une longueur superieure a longueur * largeur de la map
							int sizeMax = this.height * this.width;
							int sizeWeed = sizeMax;
							int sizeCorn = sizeMax;
							int sizePlantCorn = sizeMax;
							
							if(pathWeed != null)
								sizeWeed = pathWeed.size();
							if(pathCorn != null)
								sizeCorn = pathCorn.size();
							if(pathPlantCorn != null)
								sizePlantCorn = pathPlantCorn.size();
							
							if(sizePlantCorn < sizeWeed && sizePlantCorn <= sizeCorn) {
								path = pathPlantCorn;
							} else if ( sizeWeed <= sizePlantCorn && sizeWeed <= sizeCorn){
								path = pathWeed;
							} else {
								path = pathCorn;
							}
							
						}
						
						// marquer la cible, faire en sorte que l humain la cible
						if(path != null){
							int xTarget = path.get(path.size()-1).x;
							int yTarget = path.get(path.size()-1).y;
							Non_human target = this.map_non_human[xTarget][yTarget];
							
							// debug
							Coord firstCase = path.get(0);
							if(this.map_human[i][j].getPosX()/32 != firstCase.x || this.map_human[i][j].getPosY()/32 != firstCase.y) {
								System.err.println("Erreur Path : la premiere case ne correspond pas");
								System.err.println(target.getClass());
								System.err.println(path + ", humain : " + this.map_human[i][j].getPosX()/32 + ", " + this.map_human[i][j].getPosY()/32);
								System.err.println("Coord map : " + i + ", " + j);
							}
							
							this.map_human[i][j].setPath(path);
							if(target == null)
								System.err.println("erreur path : pas de cible en bout de path");
							else {
								// marquage cible
								target.mark();
							}
							this.map_human[i][j].setTarget(target);
							
							// une disponibilite en moins
							decreaseAmount(target.getClass());
						}
						
						//fin choix du path
						
					}
				}
				
			}
		}
		
		// choix du deplacement
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				
				if( this.map_human[i][j] != null) {
					int x = i,y = j;
					
					// debug
					if(this.map_human[i][j].getPosX()/32 != i || this.map_human[i][j].getPosY()/32 != j) {
						System.err.println("Erreur dans le posisitionnement d'un humain");
						throw new Exception();
					}
					
					Action action = this.map_human[i][j].chose_action();
					
					// en fonction de l'action
					switch(action) {
					
					case GO_DOWN:
						
						y++;
						break;
						
					case GO_LEFT:
						
						x--;
						break;
						
					case GO_RIGHT:
						
						x++;
						break;
						
					case GO_TOP:
						
						y--;
						break;
						
					case PLANT_CORN:
						
						if(this.map_human[i][j].corn_ready()){
							
							int dx = i, dy = j;
							switch(this.map_human[i][j].get_orientation()) {
							
							case DOWN:
								dy++;
								break;
							case LEFT:
								dx--;
								break;
							case RIGHT:
								dx++;
								break;
							case TOP:
								dy--;
								break;
								
							}
							
							this.map_non_human[dx][dy] = new Corn(dx,dy);
							this.nb_of_corn_available ++;
							
						}
						break;
						
					case ENTER_HOUSE:
						
						HouseDoor hd = (HouseDoor)this.map_non_human[i][j-1];
						if(hd.enterHouse(this.map_human[i][j])) {
							this.map_human[i][j].setBusy(true);
							this.map_human[i][j].setTarget(null);
							this.map_human[i][j].setPath(null);
							y--;
						}
						break;
						
					default: 
						// Pas de deplacement
						break;
					
					}
					
					// generer le tour suivant dans le buffer
					if(0<=y && y<this.height && 0<=x && x<this.width && this.map_buffer_human[x][y] == null) { 
						
						// deplacement
						
						if(!this.map_human[i][j].isEnteringHouse())
							this.map_buffer_human[x][y] = this.map_human[i][j];
						grid_d[i][j] = true;
						
					}
					else { 
						
						// colision : annuler un ou plusieurs deplacements
						
						Human h = this.map_human[i][j];
						Human tmp_h;
						x = i; y = j;
						boolean colision_nullified = false;
						while(!colision_nullified){ // on informe les entites qui se suivent qu'un element bloque
							
							if(this.map_buffer_human[x][y] == null){ // on a fini de rembobiner
								this.map_buffer_human[x][y] = h;
								grid_d[x][y] = false;
								colision_nullified = true;
							}
							else { // on rembobine
								tmp_h = this.map_buffer_human[x][y];
								this.map_buffer_human[x][y] = h;
								h = tmp_h;
								grid_d[x][y] = false;
								x = tmp_h.getPosX()/32;
								y = tmp_h.getPosY()/32;
							}
							
						}
						
						// deplacements annules
						
					}
					
				}
			}
		}
		
		// mise a jour de la vie des non humains
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				
				if(this.map_non_human[i][j] != null) {
					
					this.map_non_human[i][j].clock_tic();
					
				}
				
			}
		}
		
		this.nb_of_house_available = 0;
		this.nb_of_corn_available = 0;
		// mise a jour de la vie des non humains
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				
				Non_human current = this.map_non_human[i][j];
				
				// maison
				if(current instanceof House)
					this.map_buffer_non_human[i][j] = current;
				
				// porte de maison
				if(current instanceof HouseDoor){
					this.map_buffer_non_human[i][j] = current;
					
					if(isInBound(i, j+1) && this.map_buffer_human[i][j+1] == null){
						
						HouseDoor door = (HouseDoor)current;
						Human human = door.exitHouse();
						
						if(human != null){
							human.exit_house();
							this.map_buffer_human[i][j+1] = human;
							human.animer(duration);
						}
						
					}
					
					if(!current.isMarked())
						this.nb_of_house_available ++;
					
				}
				
				// herbe
				if(current instanceof Weed){
					if(!current.isDying())
						this.map_buffer_non_human[i][j] = current;
					else{
						if(inCrops(i, j))
							this.map_buffer_non_human[i][j] = new PlantCorn(i,j);
						this.nb_of_herb --;
					}
				}
				
				// ble, case a planter, vide
				if(current instanceof Corn || current instanceof PlantCorn || current == null){
					
					boolean killed = false;
					
					if( isInBound(i -1, j) && map_non_human[i-1][j] != null && map_non_human[i-1][j].spread()){
						
						this.map_buffer_non_human[i][j] = new Weed(i,j);
						this.nb_of_herb ++;
						killed = true;
						
					} else if( isInBound(i +1, j) && map_non_human[i+1][j] != null && map_non_human[i+1][j].spread()){
						
						this.map_buffer_non_human[i][j] = new Weed(i,j);
						this.nb_of_herb ++;
						killed = true;
						
					} else if( isInBound(i, j -1) && map_non_human[i][j-1] != null && map_non_human[i][j-1].spread()){
						
						this.map_buffer_non_human[i][j] = new Weed(i,j);
						this.nb_of_herb ++;
						killed = true;
						
					} else if( isInBound(i, j +1) && map_non_human[i][j+1] != null && map_non_human[i][j+1].spread()){
						
						this.map_buffer_non_human[i][j] = new Weed(i,j);
						this.nb_of_herb ++;
						killed = true;
						
					} else if( current instanceof Corn || current instanceof PlantCorn) {
						
						if( !current.isDying())
							
							map_buffer_non_human[i][j] = current;
						
						else{
							
							if(inCrops(i,j))
								map_buffer_non_human[i][j] = new PlantCorn(i,j);
							
						}
						
					}
					
					if(killed && current != null) {
						current.destroy();
					}
					else if(current instanceof Corn && !current.isMarked()) {
						nb_of_corn_available ++;
					}
					
				}
			}
		}
		
		// lancer les animations de humains
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if(this.map_human[i][j] != null) {
					
					// l'animation peut �tre lancee
					if(grid_d[i][j]) {
						this.map_human[i][j].animer(duration); // lancer les animations
						this.map_human[i][j].reset_time_bloqued();
					}
					
					else {
						this.map_human[i][j].increase_time_bloqued();
					}
					
				}
				
				// maj non humains
				this.map_non_human[i][j] = this.map_buffer_non_human[i][j];
				
			}
		}
		
		try {
			Thread.sleep(duration/2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// maj humains dans la grille au millieu de leur animation (chevauchement des sprites)
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				
				this.map_human[i][j] = this.map_buffer_human[i][j];
				
			}
		}
		
		try {
			Thread.sleep(duration/2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// fin animation humains
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if(this.map_human[i][j] != null) {
					this.map_human[i][j].wait_end_of_animation(); // attendre la fin des animations
					this.map_human[i][j].verifyCoords(i, j); 
					// envoie une erreur si les coordonnees ne sont pas coherentes
				}
			}
		}
		
		nb_of_man = 0;
		nb_of_woman = 0;
		nb_of_herb = 0;
		
		nb_of_herb_available = 0;
		nb_of_corn_available = 0;
		
		for(int i = 0; i < this.width; i++) {
			for(int j = 0; j < this.height; j++) {
				
				Human hu = this.map_human[i][j];
				if(hu != null) {
					
					if(hu instanceof Man)
						nb_of_man ++;
					else
						nb_of_woman ++;
					
				}
				
				Non_human nh = this.map_non_human[i][j];
				if(nh instanceof HouseDoor) {
					
					HouseDoor hd = (HouseDoor)nh;
					nb_of_man += hd.getNb_of_men();
					nb_of_woman += hd.getNb_of_women();
					
				}
				
				if(nh instanceof Weed ) {
					
					nb_of_herb ++;
					
					if(!nh.isMarked())
						nb_of_herb_available ++;
					
				}
				
				if(nh instanceof Corn && !nh.isMarked() && nh.isFunctional()) {
					nb_of_corn_available ++;
				}
				
			}
		}
		
		this.time ++;
		
	} // fin de next turn
	
	public class Coord{ // coordonnees d'une case
		
		private int x;
		private int y;
		public Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public String toString() {
			return "(" + x + ",  " + y + ")";
		}
		
	}
	
	//contient : coordonnees d'une case et son poid, la case precedemment traversee. 
	private class Link{ 
		
		// lien vers la case precedente du chemin
		private Link previous;
		
		// coordonnées de la case
		private Coord coord;
		
		public Link(Link previous, int x, int y) {
			this.previous = previous;
			this.coord = new Coord(x,y);
		}
		
		public Link getPrevious() {
			return previous;
		}
		
		public Coord getCoord() {
			return coord;
		}
		
	}

	// methode iterative : donne le chemin a parcourir pour atteindre la plus proche entite souhaitee
	public ArrayList<Coord> path_to_the_nearest_non_human(int x,int y, Class<? extends Entity> entity_type) {
		
		if(entity_type == Corn.class && this.nb_of_corn_available == 0) {
			return null; 
		}
		
		if(entity_type == HouseDoor.class && this.nb_of_house_available == 0) {
			return null; 
		}
		
		if(entity_type == Weed.class && this.nb_of_herb_available == 0) {
			return null;
		}
		
		// creation d'un tableau de boolean pour les cases mouillees
		boolean tabWet[][] = new boolean[this.width][this.height];
		for(int i = 0 ; i< this.width; i ++) {
			for(int j = 0; j < this.height; j ++)
				tabWet[i][j] = false;
		}
		
		// Liste stoquant les possibilites pour le parcours en largeur
		ArrayList<Link> eventualities = new ArrayList<>();
		
		// mettre la case de coordonnees x,y dans la liste
		Link init = new Link(null, x, y);
		Link previous = null;
		eventualities.add(init);
		
		/*Recuperation des coordonees de la case initiale*/
		int xaux, yaux;
		Coord aux;
		
		// tant qu'on a pas trouvé l'entité souhaité
		int i = 0;
		boolean found = false;
		while(i < eventualities.size() && !found) {
			
			/*Recuperation de la prochaine case qui devient "previous" et de ses coordonees
			 *Au tour 1 c'est la case initiale qui est récupérée
			 */
			previous = eventualities.get(i);
			aux = previous.getCoord();
			xaux = aux.getX();
			yaux = aux.getY();
			
			// chercher dans les cases à coté			
			if(isInBound(xaux+1,yaux) && isAvailable(xaux+1,yaux,entity_type)){
				found = true;
				eventualities.add(new Link(previous, xaux+1, yaux));
			}
			else if(isInBound(xaux,yaux+1) && isAvailable(xaux,yaux+1,entity_type)){
				found = true;
				eventualities.add(new Link(previous, xaux, yaux+1));
			}
			else if(isInBound(xaux-1,yaux) && isAvailable(xaux-1,yaux,entity_type)){
				found = true;
				eventualities.add(new Link(previous, xaux-1, yaux));
			}
			else if(isInBound(xaux,yaux-1) && isAvailable(xaux,yaux-1,entity_type)){
				found = true;
				eventualities.add(new Link(previous, xaux, yaux-1));
			}
			else{// si on a rien trouvé: rajouter les cases adjacentes dans le tableau 
				
				//randomiser les paths
				int ra = (int) (Math.random()*8);
				int dx1, dx2, dx3, dx4;
				int dy1, dy2, dy3, dy4;
				
				dx1 = 1; dx2 = -1; dx3 = 0; dx4 = 0;
				dy1 = 0; dy2 = 0; dy3 = 1; dy4 = -1;
				
				switch(ra) {
				case 0: 
					dx1 = 1; dx2 = -1; dx3 = 0; dx4 = 0;
					dy1 = 0; dy2 = 0; dy3 = 1; dy4 = -1;
					break;
				case 1:
					dx1 = 0; dx2 = 1; dx3 = -1; dx4 = 0;
					dy1 = -1; dy2 = 0; dy3 = 0; dy4 = 1;
					break;
				case 2: 
					dx1 = 0; dx2 = 0; dx3 = 1; dx4 = -1;
					dy1 = 1; dy2 = -1; dy3 = 0; dy4 = 0;
					break;
				case 3: 
					dx1 = -1; dx2 = 0; dx3 = 0; dx4 = 1;
					dy1 = 0; dy2 = 1; dy3 = -1; dy4 = 0;
					break;
				case 4: 
					dx1 = -1; dx2 = 1; dx3 = 0; dx4 = 0;
					dy1 = 0; dy2 = 0; dy3 = -1; dy4 = 1;
					break;
				case 5: 
					dx1 = 0; dx2 = -1; dx3 = 1; dx4 = 0;
					dy1 = 1; dy2 = 0; dy3 = 0; dy4 = -1;
					break;
				case 6: 
					dx1 = 0; dx2 = 0; dx3 = -1; dx4 = 1;
					dy1 = -1; dy2 = 1; dy3 = 0; dy4 = 0;
					break;
				case 7: 
					dx1 = 1; dx2 = 0; dx3 = 0; dx4 = -1;
					dy1 = 0; dy2 = -1; dy3 = 1; dy4 = 0;
					break;
				}
				
				/*On ajoute les cases adjacentes dans la liste et on mouille les cases*/
				if(isInBound(xaux+ dx1,yaux + dy1) && !tabWet[xaux + dx1][yaux + dy1] && isNotSolid(xaux + dx1 ,yaux + dy1)) {
					eventualities.add(new Link(previous, xaux+dx1, yaux+ dy1));
					tabWet[xaux+dx1][yaux+dy1] = true;
				}
				if(isInBound(xaux+ dx2,yaux + dy2) && !tabWet[xaux + dx2][yaux + dy2] && isNotSolid(xaux + dx2 ,yaux + dy2)) {
					eventualities.add(new Link(previous, xaux+dx2, yaux+ dy2));
					tabWet[xaux+dx2][yaux+dy2] = true;
				}
				if(isInBound(xaux+ dx3,yaux + dy3) && !tabWet[xaux + dx3][yaux + dy3] && isNotSolid(xaux + dx3 ,yaux + dy3)) {
					eventualities.add(new Link(previous, xaux+dx3, yaux+ dy3));
					tabWet[xaux+dx3][yaux+dy3] = true;
				}
				if(isInBound(xaux+ dx4,yaux + dy4) && !tabWet[xaux + dx4][yaux + dy4] && isNotSolid(xaux + dx4 ,yaux + dy4)) {
					eventualities.add(new Link(previous, xaux+dx4, yaux+ dy4));
					tabWet[xaux+dx4][yaux+dy4] = true;
				}
				
			}
			
			i++;
		} 
		
		if(!found) {
			
			return null;
			
		}
		else {
		
			// on obtient la case d'arrivee et une liste a trier
			
			/*Recuperation de la case cible*/
			Link target = eventualities.get(eventualities.size()-1);
			
			// obtenir le path jusqu'a la case souhaitee
			ArrayList<Coord> path = new ArrayList<>();
			while(target != null){
				path.add(0,target.getCoord());
				target = target.getPrevious();
			}
			
			return path;
			
		}
		
	}
	
}
