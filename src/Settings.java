/*
The settings class is a helper class that contains many state
variables of the simulation that the user can modify in the
settings scene. This class cannot be instantiated.
*/
public final class Settings {
    
   	private static final int RAINDROP_LIMIT = 1000;
	
	private static final double MAX_RAIN_RATE = 1;
	private static final double MIN_RAIN_RATE = 5;
	private static final double DEFAULT_RAIN_RATE = 3;
	private static double rainRate = DEFAULT_RAIN_RATE;
	
	private static boolean rainStopped = true;
	//private static boolean defaultSettings = true;

	private Settings() {
        
    }
	
	public static void setRainStopped(boolean rainStoppedIn) {
		rainStopped = rainStoppedIn;
	}
	public static boolean getRainStopped() {
		return rainStopped;
	}
	
	public static double getRainRate() {
		return rainRate;
	}
	
	public static int getRainDropLimit() {
		return RAINDROP_LIMIT;
	}
	
}
