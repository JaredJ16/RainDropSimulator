import java.util.Random;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;


/*
The RainDrop class is a child of the Ellipse JavaFX
class and has certain methods that allow it to work
nicely with the canvas.
*/
//this random comment should be deleted later
public class RainDrop extends Ellipse {
	
	private static Random random = new Random();
	private static final int LOWER_RADIUS_BOUND = 7;
	private static final int UPPER_RADIUS_BOUND = 11;
	
	//These help with placing the ellipses and note that they might be rounded up at times.
	private int halfRadiusX;
	private int halfRadiusY;
//	private double velocity;
//	private double accelaration;

	public RainDrop() {
		super();
		orientEllipseCorrectly();
		placeEllipseWithinCanvasBounds();
		setFill(Color.BLUE);
	}
	
	private static int inclusiveRandInt(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}
	
	public void orientEllipseCorrectly() {
		// The rain drop radius amounts can only be odd becuase I dont want to deal with centers of .5
		int xRadius = LOWER_RADIUS_BOUND + 2 * inclusiveRandInt(0, (UPPER_RADIUS_BOUND - LOWER_RADIUS_BOUND) / 2);
		int yRadius = LOWER_RADIUS_BOUND + 2 * inclusiveRandInt(0, (UPPER_RADIUS_BOUND - LOWER_RADIUS_BOUND) / 2);
		
		//flip it so that the raindrop is always taller or a circle
		if (xRadius > yRadius) {
			int temp = xRadius;
			xRadius = yRadius;
			yRadius = temp;
		}

		setRadiusX(xRadius);
		setRadiusY(yRadius);
		halfRadiusX = (int)Math.ceil(xRadius / 2.0);
		halfRadiusY = (int)Math.ceil(yRadius / 2.0);
	}
	
	public void placeEllipseWithinCanvasBounds() {
		setCenterX(inclusiveRandInt((int)halfRadiusX - 1, (int)WindowVisual.getCanvasLength() - halfRadiusX));
		setCenterY(inclusiveRandInt((int)halfRadiusY - 1, (int)WindowVisual.getCanvasLength() - halfRadiusY));
	}

	public void drawDropOnCanvas(GraphicsContext canvasContext) {
		canvasContext.setFill(getFill());
		canvasContext.fillOval(getCenterX() - halfRadiusX + 1, getCenterY() - halfRadiusY + 1, getRadiusX(), getRadiusY());
	}

	public boolean intersectsWith(RainDrop targetDrop) {
		boolean intersecting = false;
		
		int targetDropMaxRadius = Math.max((int)targetDrop.getRadiusX(), (int)targetDrop.getRadiusY());
		double distanceBetweenCenters = Math.hypot(targetDrop.getCenterX() - this.getCenterX(), targetDrop.getCenterY() - this.getCenterY());
		
		if (distanceBetweenCenters > targetDropMaxRadius) {//note that depending how I want to implement this, >= could be used, just think of the implications
			intersecting = false;
		} else {
			if (this.getCenterY() > targetDrop.getCenterY()) {
				//continue with bottom five nodes

			} else if (this.getCenterY() < targetDrop.getCenterY()) {
				//continue with top five nodes
			} else {
				//continue with 2 horizontal axis nodes
			}
			intersecting = true;//delete this later;
		}
		
		return intersecting;
	}	
}
