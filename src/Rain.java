import java.util.ArrayList;

/*
The Rain class contains all of the RainDrops in
an ArrayList.
*/
public class Rain {
	
	//to be changed to a better data structure(quaternary tree?)
	private ArrayList<RainDrop> rainDrops;
	
	public Rain() {
		rainDrops = new ArrayList<RainDrop>();
	}
	
	public ArrayList<RainDrop> getRainDrops() {
		return rainDrops;
	}
	
	public void addRainDrop() {
		rainDrops.add(new RainDrop());
	}
	
	public void mergeIntersectingRainDrops() {

	}
    
}
