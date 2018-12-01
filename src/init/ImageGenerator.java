package init;

import java.io.File;
import java.util.ArrayList;

public abstract class ImageGenerator {
	
	private static Picture corn;

	public static ArrayList<Picture> generate(){
		
		ArrayList<Picture> pP = new ArrayList<>();
		
		Picture man = new Picture("man.png");
		Picture woman = new Picture("woman.png");
		corn = new Picture("corn.png");
		Picture weed = new Picture("weed3state.png");
		Picture house = new Picture("house.png");
		man = new Picture(man,0,0,32,48,0,-16,Type.MAN);
		woman = new Picture(woman,0,0,32,48,0,-16,Type.WOMAN);
		corn = new Picture(corn,0,0,28,28,0,0,null);
		weed = new Picture(weed,0,0,28,28,0,0,Type.WEED);
		house = new Picture(house,0,0,216,218,0,0,Type.HOUSE);
		house.setGridWidth(5);
		house.setGridHeight(5);
		
		pP.add(man);
		pP.add(woman);
		pP.add(weed);
		pP.add(house);
		
		return pP;
		
	}
	
	public static Picture getDaCorn() {
		return corn;
	}
	
}
