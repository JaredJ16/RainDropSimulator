import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;


import javafx.scene.shape.Shape;
import javafx.scene.shape.Ellipse;

import javafx.scene.shape.Rectangle;
//get rid of expirement and drawExpirement and all not needed imports

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
		canvasContext.setFill(Color.WHITE);
		canvasContext.fillRect(0, 0, CANVAS_LENGTH, CANVAS_LENGTH);
	}
	
	private void drawRain() {
		for (int i = 0; i < rain.getRainDrops().size(); i++) {
			rain.getRainDrops().get(i).drawDropOnCanvas(canvasContext);
		}
	}
	
	private void updateRain() {
		addRainDrops();
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
