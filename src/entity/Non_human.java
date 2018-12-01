package entity;

public abstract class Non_human extends Entity{

	// regles
	
	private boolean marked;
	
	public Non_human(int posX, int posY) {
		super(posX, posY);
		marked = false;
	}
	
	// regles
	
	public abstract boolean spread();
	
	public void mark() {
		this.marked = true;
	}
	
	public void unmark() {
		this.marked = false;
	}
	
	public boolean isMarked() {
		return this.marked;
	}
	
	// pathfinding
	
	public abstract boolean isSolid();
	
	public abstract void destroy();

}
