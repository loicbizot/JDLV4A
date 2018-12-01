package display;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.regex.MatchResult;

import elements.Map;
import entity.Human;
import init.Panel;
import init.PositionnedPicture;
import init.Type;

public class Init extends Panel{

	public Map loadMap() {
		
		Map res = new Map(this.getDimX(),this.getDimY());
		
		ArrayList<PositionnedPicture> p = getPictures();
		ArrayList<Rectangle2D> r = getRects();
		
		int i = 0;
		for(Rectangle2D ri : r) {
			
			Rectangle2D rec = new Rectangle2D.Double(ri.getX()/32,ri.getY()/32,ri.getWidth()/32,ri.getHeight()/32);
			res.addRect(i, rec);
			i++;
			
		}
		
		res.generateCrops();
		
		for(PositionnedPicture pi : p) {
			
			if(pi.getPicture().getType() == Type.MAN)
				res.addMan(pi.getX()/32, pi.getY()/32);
			
			if(pi.getPicture().getType() == Type.WOMAN)
				res.addWoman(pi.getX()/32, pi.getY()/32);
			
			if(pi.getPicture().getType() == Type.WEED)
				res.addWeed(pi.getX()/32, pi.getY()/32);
			
			if(pi.getPicture().getType() == Type.HOUSE)
				res.put_an_house(pi.getX()/32, pi.getY()/32);
			
		}
		
		Human.initFoodAmount();
		
		return res;

	}
	
}
