import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

//import java.util.ArrayList;
import javafx.geometry.Point2D;
/*
The WindowVisual class represents an object that updates the
state of the rain and displays the rain.
*/
public class WindowVisual {
	
	private Canvas canvas;
	private GraphicsContext canvasContext;
	private AnimationTimer animationTimer;
	private Rain rain;
	private static final double CANVAS_LENGTH = 720;
    
	public WindowVisual() {
		canvas = new Canvas(CANVAS_LENGTH, CANVAS_LENGTH);
		canvasContext = canvas.getGraphicsContext2D();
		rain = new Rain();
		animationTimer = new AnimationTimer() {
			public void handle(long currentTime) {
				clearCanvas();
				updateRain();
				drawRain();
			}
		};
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void startAnimation() {
		animationTimer.start();
		Settings.setRainStopped(false);
	}
	public void stopAnimation() {
		animationTimer.stop();
		Settings.setRainStopped(true);
	}
	
	private void clearCanvas() {
		canvasContext.setFill(Color.GHOSTWHITE);
		canvasContext.fillRect(0, 0, CANVAS_LENGTH, CANVAS_LENGTH);
	}
	
	private void drawRain() {
		for (int i = 0; i < rain.getRainDrops().size(); i++) {
			rain.getRainDrops().get(i).drawDropOnCanvas(canvasContext);
		}
	}
///////////////////////
//This method is very useful to see why certain drops are colliding, keep for now, and delete it when not needed later
/*	public void drawNodes(Point2D[] nodes) {
		for (int i = 0; i < 9; i++) {
			canvasContext.setFill(Color.RED);
			canvasContext.fillRect(nodes[i].getX(), nodes[i].getY(), 1.0, 1.0);
		}
	}*/
//////////////////////	
	private void updateRain() {
		addRainDrops();
		//this method will change once the data structure of the raindrops is changed, so that
		//checking for collisions is not so intensive and much more locally done.
		for (int i = 0; i < rain.getRainDrops().size(); i++) {
			for (int j = 0; j < rain.getRainDrops().size(); j++) {
				if (i != j) {
					rain.getRainDrops().get(i).intersectsWith(rain.getRainDrops().get(j));
				}
			}
		}
		//rain.mergeIntersectingRainDrops();
		//change velocity/position of raindrops
	}
    
	private void addRainDrops() {
		if((rain.getRainDrops().size() < Settings.getRainDropLimit()) && ((int)(Math.random() * Settings.getRainRate()) == 0)) {
			rain.addRainDrop();
		}
	}
	
	public static double getCanvasLength() {
		return CANVAS_LENGTH;
	}

}
