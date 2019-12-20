/* ToDo
current:
	-Intersecting method then qud tree then merging
	-merge detection/method
	-structuring of raindrops
	-basic physics
	-basic settings/current focus issue in settings
finalizing:
	-fix event handling so that it is all uniform
	-check all modfiers/names
	-check style
	-reorganize method location
	-color switches for canvas (make everything the same)
	-verify ellipse functions and organization (RainDrop Methods)
*/


import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

/*
The RainDropSimulator class is the main class and entry point for the JavaFX application.
There are two main scenes that the class can display: the rain scene and the settings
scene. The rain scene is the screen where the simulation occurs and the settings scene
is the screen where the user can modify certain aspects of the simulation. The
RainDropSimulator class displays all the scenes and nodes in the scene graph, and also
has the event functions that allow movement between the rain scene and the settings scene.
The RainDropSimulator class contains a WindowVisual object which updates the state of the
rain and draws the rain onto a canvas.
*/
public class RainDropSimulator extends Application {
	private static final String WINDOW_TITLE = "Rain Drop Simulator";
	private static final String ICON_IMAGE_PATH = "/images/rainDropIcon.png";
	private static final String RAIN_IMAGE_PATH = "/images/toRainScene.png";
	private static final String SETTINGS_IMAGE_PATH = "/images/toSettingsScene.png";
	private static final String START_IMAGE_PATH = "/images/startButton.png";
	private static final String STOP_IMAGE_PATH = "/images/stopButton.png";
	private static final double WINDOW_WIDTH = 720;
	private static final double WINDOW_HEIGHT = 770;
	private static final double SETTINGS_BAR_HEIGHT = 50;

	private Scene rainScene;
	private StackPane rainRoot;
	private	ImageView toSettingsSceneImage;
	private Image stopImage;
	private Image startImage;
	private ImageView startStopButton;
	private WindowVisual windowVisual;
	
	private Scene settingsScene;
	private StackPane settingsRoot;
	private VBox rainRateBox;

	private	ImageView toRainSceneImage;
	private Slider rainRateSlider;
	private Text rainRateText;

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		initializeRoots();

		addSceneSwitchingEventHandlers(primaryStage);
		addKeyEvents(primaryStage);
		addStartAndStopButtonEventHandlers();
		
		setStageProperties(primaryStage);
	}
//initialization///////////////////////////////////////////////////////////////////////////////////	
	private void initializeRoots() {
		//Rain Scene nodes
		windowVisual = new WindowVisual();
		stopImage = new Image(STOP_IMAGE_PATH);
		startImage = new Image(START_IMAGE_PATH);
		toSettingsSceneImage = new ImageView(new Image(SETTINGS_IMAGE_PATH));
		startStopButton = new ImageView(startImage);

		//Settings Scene nodes
		toRainSceneImage = new ImageView(new Image(RAIN_IMAGE_PATH));
		rainRateSlider = new Slider(1,5,3);//possibly change to final vars
		rainRateText = new Text("Rate of Rain");

		rainRateSlider.setMajorTickUnit(1);
		rainRateSlider.setBlockIncrement(1);
		rainRateSlider.setMinorTickCount(0);
		rainRateSlider.setSnapToTicks(true);
		rainRateSlider.setShowTickLabels(true);
		rainRateSlider.setShowTickMarks(true);
		rainRateSlider.setMaxWidth(100);
		
		//initialize the roots and scenes
		rainRoot = new StackPane();
		settingsRoot = new StackPane();
		rainScene = new Scene(rainRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
		settingsScene = new Scene(settingsRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

		//organize nodes into panes
		rainRateBox = new VBox(rainRateText, rainRateSlider);

		//style the nodes and add them to the roots
		rainRoot.getChildren().addAll(windowVisual.getCanvas(), toSettingsSceneImage, startStopButton);
		settingsRoot.getChildren().addAll(toRainSceneImage, rainRateBox);
		styleNodesOfRainRoot();
		styleNodesOfSettingsRoot();
	}
	
	private void styleNodesOfRainRoot() {
		rainRoot.setAlignment(windowVisual.getCanvas(), Pos.TOP_CENTER);
		rainRoot.setAlignment(toSettingsSceneImage, Pos.BOTTOM_CENTER);
		rainRoot.setAlignment(startStopButton, Pos.BOTTOM_RIGHT);
	}
	
	private void styleNodesOfSettingsRoot() {
		settingsRoot.setAlignment(toRainSceneImage, Pos.BOTTOM_CENTER);
	}
//event handling///////////////////////////////////////////////////////////////////////////////////	
	private void addKeyEvents(Stage primaryStage) {

		//switch scenes when the s key is pressed or
		//toggle the rain starting/stopping when enter is pressed.
		rainScene.setOnKeyReleased( e -> {
			if (e.getCode().equals(KeyCode.S)) {
				windowVisual.stopAnimation();
				startStopButton.setImage(startImage);
				primaryStage.setScene(settingsScene);
			} else if (e.getCode().equals(KeyCode.ENTER)) {
				switchButtonsAndRainState();
			}
		});
		
		//switch scenes when the s key is pressed	
		settingsScene.setOnKeyReleased( e -> {
			if (e.getCode().equals(KeyCode.S)) {
				primaryStage.setScene(rainScene);
			}
		});

		//terminate the progam when the q key is pressed
		primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode().equals(KeyCode.Q)) {
				Platform.exit();
				System.exit(0);	
			}
		});
	}


	private void addSceneSwitchingEventHandlers(Stage primaryStage) {
        toSettingsSceneImage.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			windowVisual.stopAnimation();
			startStopButton.setImage(startImage);
			primaryStage.setScene(settingsScene);
        });
        
        toRainSceneImage.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			primaryStage.setScene(rainScene);
        });
	}
	
	private void addStartAndStopButtonEventHandlers() {
        startStopButton.setOnMouseClicked( e -> {
			switchButtonsAndRainState();
        });
	}

	private void switchButtonsAndRainState() {
		if (Settings.getRainStopped()) {
			windowVisual.startAnimation();
			startStopButton.setImage(stopImage);
		} else {
			windowVisual.stopAnimation();
			startStopButton.setImage(startImage);
		}
	}
//display//////////////////////////////////////////////////////////////////////////////////////////
	private void setStageProperties(Stage primaryStage) {
		primaryStage.setMinWidth(WINDOW_WIDTH);
		primaryStage.setMinHeight(WINDOW_HEIGHT);
		primaryStage.setResizable(false);
		primaryStage.setTitle(WINDOW_TITLE);
		primaryStage.getIcons().add(new Image(ICON_IMAGE_PATH));
		primaryStage.setScene(rainScene);
		primaryStage.show();
	}
	
}
