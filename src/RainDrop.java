import java.util.Random;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Point2D;

/*
The RainDrop class is a child of the Ellipse JavaFX
class and has certain methods that allow it to work
nicely with the canvas.
*/
public class RainDrop extends Ellipse {
	
	private static Random random = new Random();
	private static final int LOWER_RADIUS_BOUND = 7;
	private static final int UPPER_RADIUS_BOUND = 13;
	
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
	
	private void orientEllipseCorrectly() {
		// The rain drop radius amounts can only be odd becuase
		// dealing with odd and even radii results in unnessecary complications.
		int xRadius = LOWER_RADIUS_BOUND + 2 * inclusiveRandInt(0, (UPPER_RADIUS_BOUND - LOWER_RADIUS_BOUND) / 2);
		int yRadius = LOWER_RADIUS_BOUND + 2 * inclusiveRandInt(0, (UPPER_RADIUS_BOUND - LOWER_RADIUS_BOUND) / 2);
		
		//flip it so that the raindrop is always taller
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
	
	private void placeEllipseWithinCanvasBounds() {
		setCenterX(inclusiveRandInt((int)halfRadiusX - 1, (int)WindowVisual.getCanvasLength() - halfRadiusX));
		setCenterY(inclusiveRandInt((int)halfRadiusY - 1, (int)WindowVisual.getCanvasLength() - halfRadiusY));
	}

	public void drawDropOnCanvas(GraphicsContext canvasContext) {
		canvasContext.setFill(getFill());
		canvasContext.fillOval(getCenterX() - halfRadiusX + 1, getCenterY() - halfRadiusY + 1, getRadiusX(), getRadiusY());
	}

	//possibly consider making this a static method with multiple raindrop parameter
	//the organization of this method most certainly will need to change after or during the creation of the merge method to work with it
	public boolean intersectsWith(RainDrop targetDrop) {
		int targetDropMaxHalfRadius = Math.max(targetDrop.halfRadiusX, targetDrop.halfRadiusY);
		int currentDropMaxHalfRadius = Math.max(this.halfRadiusX, this.halfRadiusY);
		double distanceBetweenCenters = Math.hypot(targetDrop.getCenterX() - this.getCenterX(), targetDrop.getCenterY() - this.getCenterY());
		
		//This is an initial tests that sees if there is a large seperation, and node checking is not even neccesary
		if ((int)distanceBetweenCenters > targetDropMaxHalfRadius + currentDropMaxHalfRadius + 2) {
				return false;
		} else {
			//if needing an optimization, look towards this, but as of now, using all bounding nodes appears to work the best
			/*if (this.getCenterY() > targetDrop.getCenterY()) {
				intersecting = isNodeInTargetDrop(targetDrop, getTopNodes());
				System.out.println("Using Top");
			} else if (this.getCenterY() < targetDrop.getCenterY()) {
				intersecting = isNodeInTargetDrop(targetDrop, getBottomNodes());
				System.out.println("Using Bottom");
			} else {
				intersecting = isNodeInTargetDrop(targetDrop, getHorizontalAxisNodes());
				System.out.println("Using Mid");
			}*/
			return isNodeInTargetDrop(targetDrop, getBoundingNodes());
		}
	}

	public Point2D[] getBoundingNodes() {
		Point2D[] eightNodes = new Point2D[9];
		eightNodes[0] = new Point2D(getCenterX() - halfRadiusX, getCenterY());
		eightNodes[1] = new Point2D(getCenterX() + halfRadiusX, getCenterY());
		eightNodes[2] = new Point2D(getCenterX(), getCenterY() + halfRadiusY);
		eightNodes[3] = new Point2D(getCenterX(), getCenterY() - halfRadiusY);
		eightNodes[4] = new Point2D(getCenterX() - calculateDiagonalEllipseDistance(), getCenterY() - calculateDiagonalEllipseDistance());
		eightNodes[5] = new Point2D(getCenterX() + calculateDiagonalEllipseDistance(), getCenterY() - calculateDiagonalEllipseDistance());
		eightNodes[6] = new Point2D(getCenterX() - calculateDiagonalEllipseDistance(), getCenterY() + calculateDiagonalEllipseDistance());
		eightNodes[7] = new Point2D(getCenterX() + calculateDiagonalEllipseDistance(), getCenterY() + calculateDiagonalEllipseDistance());
		eightNodes[8] = new Point2D(getCenterX(), getCenterY());
		return eightNodes;
	}
	public double calculateDiagonalEllipseDistance() {
		return Math.sqrt(
			(getRadiusX() * getRadiusX() * getRadiusY() * getRadiusY() ) /
			(double)(4.0 * ((getRadiusX() * getRadiusX()) + (getRadiusY() * getRadiusY())) )
		);
	}
	
	//ellipse equation with inequality that allows for a slight range of estimation
	public boolean isNodeInTargetDrop(RainDrop targetDrop, Point2D[] testNodes) {
		for (int i = 0; i < testNodes.length; i++ ) {
			testNodes[i] = testNodes[i].subtract(targetDrop.getCenterX(), targetDrop.getCenterY());
			double ellipseValue = 4 * (   ( Math.pow(testNodes[i].getX(), 2) / (Math.pow(targetDrop.getRadiusX(), 2)) ) + ( Math.pow(testNodes[i].getY(), 2) / (Math.pow(targetDrop.getRadiusY(), 2)) )   );
			//round the ellipseValue
			ellipseValue *= 100;
			ellipseValue = (int)ellipseValue - 1;
			ellipseValue /= 100;
			if (ellipseValue <= 1.15) {
				targetDrop.setFill(Color.RED);
				this.setFill(Color.RED);
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Centered: (" + getCenterX() + ", " + getCenterY() + ") Dimensions: " + "(" + getRadiusX() + ", " + getRadiusY() + ") " + "HalfRadii: " + "(" + halfRadiusX + ", " + halfRadiusY + ")";  
	}

}
