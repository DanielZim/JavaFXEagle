package main;

//import com.sun.scenario.Settings;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.BoundingBox;
import model.sceneObjects.EagleBoard;
import model.sceneObjects.coordinateTransformables.Element;
import parser.EagleXmlParser;
import parser.EagleXmlReader;
import utils.ColorProvider;

import java.io.File;
import java.util.*;

/**
 * The type Eagle scene builder.
 */
public class EagleSceneBuilder {

    private static final double WINDOW_FILL_FACTOR = 0.8;

    private Stage stage;
    private VBox rootBox;
    private HBox contentBox;
    private TextArea informationPanel;
    private File original;
    private EagleBoard workingCopy;
    private Pane boardWrapper;
    private Node boardSceneNode;
    private Scene scene;
    private RandomBoardChanger boardChanger;
    private Timeline tl;

    private double windowHeight;
    private double windowWidth;

    // for drag event handling
    private double startMouseX;
    private double startMouseY;
    private double startTranslationX;
    private double startTranslationY;

    // stored transformation values
    private double storedScaleFactor;
    private double storedTranslationX;
    private double storedTranslationY;

    // modify element bboxes
    private Element clickedElement;
    Button changeBoundingBox;
    MenuItem changeBBox;

    // scene settings
    private SceneSettings sceneSettings;

    /**
     * Instantiates a new Eagle scene builder.
     *
     * @param stage the stage
     */
    public EagleSceneBuilder(Stage stage) {
        this.stage = stage;
        this.sceneSettings = new SceneSettings();
    }

    /**
     * Starts the scene building process.
     *
     * @throws Exception the exception
     */
    public void start() throws Exception{
        chooseFile();

        // set the bbox drawing
        workingCopy.setDrawBoundingBoxes(sceneSettings.isDrawBoundingBoxes());

        // initialise the random board changer
        boardChanger = new RandomBoardChanger(workingCopy);

        // show eagle board
        buildScene();
    }

    /**
     * User chooses a *.brd file via FileChooser dialog. The parser reads the file and the scene gets updated.
     *
     * @throws Exception the exception
     */
    private void chooseFile() throws Exception {
        // parse board from file
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose an Eagle Board");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Eagle Board (*.brd)", "*.brd"));
        original = fc.showOpenDialog(stage);
        createBoard();
        buildScene();
    }

    private void createBoard() throws Exception {
        EagleXmlReader parser = new EagleXmlReader();
        parser.readFile(original);
        workingCopy = parser.getBoard();
        boardChanger = new RandomBoardChanger(workingCopy);
    }

    /**
     * Builds the scene.
     */
    private void buildScene() {
        // setup window height to fill the screen mostly
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        windowHeight = bounds.getHeight() - 30;
        windowWidth = bounds.getWidth() - 30;

        // setup single views
        rootBox = new VBox();
        contentBox = new HBox();
        setBackgroundColor(sceneSettings.getBackgroundColor());
        setupMenuBar();
        rootBox.getChildren().addAll(contentBox);
        setupInformationPanel();
        setupBoardView();
        setupTransformation(false);

        // show stage with scene
        stage.setTitle("Eagle Board - " + workingCopy.getName());
        scene = new Scene(rootBox, windowWidth, windowHeight);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the background color of the board view.
     *
     * @param color the background color to be used
     */
    private void setBackgroundColor(Color color) {
        contentBox.setBackground(new Background(new BackgroundFill(color,
                                                                   CornerRadii.EMPTY,
                                                                   Insets.EMPTY)));
    }

    /**
     * Updates the scene after the board got changed
     */
    private void updateScene(boolean reset) {
        if (!reset) {
            storeTransformation();
        }
        clearScene();
        setupBoardView();
        setupTransformation(!reset);
    }

    /**
     * Sets up the interaction bar on top of the scene.
     */
    private void setupMenuBar() {
        // create menu bar
        MenuBar menuBar = new MenuBar();

        // create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        // create menuitems
        MenuItem openBoard = new MenuItem("Open Board");
        openBoard.setOnAction(event -> {
            try {
                chooseFile();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("File Chooser Exception");
            }
        });
        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem settings = new MenuItem("Settings");
        settings.setOnAction(event -> {
            showAndApplySettings();
        });
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> {
            System.exit(0);
        });

        changeBBox = new MenuItem("Change Bbox");
        changeBBox.setDisable(true);
        changeBBox.setOnAction(event -> {
            setBoundingBoxWithDialog();
        });
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                boardChanger.shuffleBoard();
                return null;
            }
        };
        task.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    updateScene(false);
                }
            }
        });
        MenuItem randomize = new MenuItem("Shuffle");
        randomize.setOnAction(event -> {
            workingCopy = boardChanger.shuffleBoard();
            updateScene(false);
        });

        SeparatorMenuItem separator2 = new SeparatorMenuItem();
        MenuItem reset = new MenuItem("Reset");
        reset.setOnAction(event -> {
            try {
                createBoard();
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateScene(true);
        });

        MenuItem howto = new MenuItem("How to use..");
        howto.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("How to use..");
            alert.setHeaderText(null);
            alert.setContentText("Hint: You can also use your right mouse button to shuffle the board!");
            alert.showAndWait();
        });
        MenuItem about = new MenuItem("About");
        about.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText(null);
            alert.setContentText("This \"Eagle Simulator\" project was created by Dennis Weigelt and Florian Haase as part of \"Ingenieursmäßige Softwareentwicklung\" at KIT.");
            alert.showAndWait();
        });


        // add menuitems to corresponding menus
        fileMenu.getItems().addAll(openBoard, settings, separator, exit);
        editMenu.getItems().addAll(changeBBox, randomize, separator2, reset);
        helpMenu.getItems().addAll(howto, about);

        // add menus to menu bar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        Button random = new Button("Shuffle");
        random.setOnMouseClicked(event -> {
            workingCopy = boardChanger.shuffleBoard();
            updateScene(false);
        });

        changeBoundingBox = new Button("Change Bbox");
        changeBoundingBox.setOnMouseClicked(event -> setBoundingBoxWithDialog());
        changeBoundingBox.setDisable(true);

        HBox box = new HBox();
        rootBox.getChildren().addAll(menuBar, box);
        box.getChildren().addAll(random, changeBoundingBox);
    }

    /**
     * Shows the settings dialog and updates the scene according to the changes.
     */
    private void showAndApplySettings() {
        Dialog<SceneSettings> dialog = sceneSettings.getDialog();
        Optional<SceneSettings> result = dialog.showAndWait();

        // apply the settings and update the scene
        result.ifPresent(settings -> {
            sceneSettings.setBackgroundColor(settings.getBackgroundColor());
            sceneSettings.setDrawBoundingBoxes(settings.isDrawBoundingBoxes());

            workingCopy.setDrawBoundingBoxes(settings.isDrawBoundingBoxes());
            setBackgroundColor(settings.getBackgroundColor());
            updateScene(false);
        });
    }

    /**
     * Sets up the board view (including mouse event handler registration).
     */
    private void setupBoardView() {
        boardWrapper = createClippedWrapper();
        boardSceneNode = workingCopy.getSceneNode();

        // draw the grid if it is set
        if(workingCopy.getIsSetGrid() && workingCopy.getGrid().getDisplay()) {
            workingCopy.getGrid().drawOnNode(boardSceneNode, (int) windowWidth, (int) windowHeight - 50);
        }

        addMouseEventHandler();

        boardWrapper.getChildren().add(boardSceneNode);
        contentBox.getChildren().add(boardWrapper);
    }

    /**
     * Sets up the transformation (scaling and translation)
     *
     * @param useOldValues determines if the last transformation should be used again
     */
    private void setupTransformation(boolean useOldValues) {
        setScaling(useOldValues);
        setTranslation(useOldValues);
    }

    /**
     * Sets up the scaling of the board.
     *
     * @param useOldValues determines if the last scale factor should be used again
     */
    private void setScaling(boolean useOldValues) {
        if(useOldValues) {
            boardSceneNode.setScaleX(storedScaleFactor);
            boardSceneNode.setScaleY(storedScaleFactor);
        } else {
            // set scaling to fill the board wrapper
            double scaleFactorY = Math.floor((windowHeight - 50) / workingCopy.getHeight());
            double scaleFactorX = Math.floor(windowWidth / workingCopy.getWidth());
            double scaleFactor = Math.min(scaleFactorX, scaleFactorY) * WINDOW_FILL_FACTOR;
            boardSceneNode.setScaleX(scaleFactor);
            boardSceneNode.setScaleY(scaleFactor);
        }
    }

    /**
     * Sets up the translation.
     *
     * @param useOldValues determines if the last translation should be used again.
     */
    private void setTranslation(boolean useOldValues) {
        if(useOldValues) {
            boardSceneNode.setTranslateX(storedTranslationX);
            boardSceneNode.setTranslateY(storedTranslationY);
        } else {
            // translate board to fit in the view
            // by default the center of the board node is set to the origin of the wrapper pane
            double translationX = workingCopy.getWidth() / 2 * boardSceneNode.getScaleX();
            double translationY = workingCopy.getHeight() / 2 * boardSceneNode.getScaleX();
            boardSceneNode.setTranslateX(translationX);
            boardSceneNode.setTranslateY(translationY);
            System.out.println("Scaling Factor: " + boardSceneNode.getScaleX() + " TranslationX: " + translationX +
                    " TranslationY: " + translationY);

        }
    }

    /**
     * Stores the current board transformation to use it again after the board was changed.
     */
    private void storeTransformation() {
        storedScaleFactor = boardSceneNode.getScaleX();
        storedTranslationX = boardSceneNode.getTranslateX();
        storedTranslationY = boardSceneNode.getTranslateY();
    }

    /**
     * Sets up the information panel on the left of the gui.
     */
    private void setupInformationPanel() {
        // set the information view on the right side and the board on the left
        informationPanel = new TextArea();
        informationPanel.setMinWidth(300);
        informationPanel.setMaxWidth(300);
        informationPanel.setMinHeight(400);
        informationPanel.setEditable(false);
        informationPanel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        informationPanel.setText("Hey information panel is ready!");
        contentBox.getChildren().add(informationPanel);
        workingCopy.setInformationPanel(informationPanel);
    }

    /**
     * Adds a MouseEventHandler to handle mouse inputs.
     */
    private void addMouseEventHandler() {
        // add drag handling
        boardSceneNode.setOnMousePressed((mouseEvent) ->
        {
            startMouseX = mouseEvent.getSceneX();
            startMouseY = mouseEvent.getSceneY();

            startTranslationX = boardSceneNode.getTranslateX();
            startTranslationY = boardSceneNode.getTranslateY();
        });

        boardSceneNode.setOnMouseDragged((mouseEvent) ->
        {
            double movedX = startMouseX - mouseEvent.getSceneX();
            double movedY = startMouseY - mouseEvent.getSceneY();

            double transX = startTranslationX - movedX;
            double transY = startTranslationY - movedY;

            boardSceneNode.setTranslateX(transX);
            boardSceneNode.setTranslateY(transY);
        });

        // add scroll handling to zoom in and out
        boardWrapper.setOnScroll((event) ->
        {
            double factor = event.getDeltaY() > 0 ? 1.1 : 0.9;
            boardSceneNode.setScaleX(boardSceneNode.getScaleX() * factor);
            boardSceneNode.setScaleY(boardSceneNode.getScaleY() * factor);
        });

        // add handler to reset the view on right click
        boardWrapper.setOnMouseClicked( (event -> {
            if(event.getButton() == MouseButton.SECONDARY) {
                // TODO: only for testing, needs to be removed when the toggle button gets functionality
                workingCopy = boardChanger.shuffleBoard();
                updateScene(false);
//                setScaling();
//                setTranslation();
            }
        }));

        boardSceneNode.setOnMouseClicked((mouseEvent -> {
            double x = mouseEvent.getX();
            double y = (mouseEvent.getY());
            System.out.println("X: " + x + ", Y: " + y);

            clickedElement = workingCopy.findElementAt(new Point2D(x, y));

            informationPanel.clear();
            if(clickedElement != null) {
                informationPanel.setText(clickedElement.getElementInformation());
                changeBoundingBox.setDisable(false);
                changeBBox.setDisable(false);
            } else {
                changeBoundingBox.setDisable(true);
                changeBBox.setDisable(true);

            }
        }));
    }

    private void setBoundingBoxWithDialog() {
        // get the bounding box to be changed
        BoundingBox bbox = clickedElement.getBoundingBox();

        // build the dialog with four inpput fields: lower left x, y and upper right x, y
        Dialog<ArrayList<Double>> dialog = new Dialog<>();
        dialog.setTitle("Change bounding box");
        dialog.setHeaderText("Bounding box values");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

        // setup the input fields with labels
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField lowerLeftX = new TextField();
        lowerLeftX.setText("" + bbox.getLowerLeftEdge().getX());
        TextField lowerLeftY = new TextField();
        lowerLeftY.setText("" + bbox.getLowerLeftEdge().getY());
        TextField upperRightX = new TextField();
        upperRightX.setText("" + bbox.getUpperRightEdge().getX());
        TextField upperRightY = new TextField();
        upperRightY.setText("" + bbox.getUpperRightEdge().getY());

        grid.add(new Label("Lower left X:"), 0, 0);
        grid.add(lowerLeftX, 1, 0);
        grid.add(new Label("Lower left Y:"), 0, 1);
        grid.add(lowerLeftY, 1, 1);
        grid.add(new Label("Upper right X:"), 0, 2);
        grid.add(upperRightX, 1, 2);
        grid.add(new Label("Upper right Y:"), 0, 3);
        grid.add(upperRightY, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(lowerLeftX::requestFocus);

        // convert the result to a list
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.APPLY) {
                ArrayList<Double> result = new ArrayList<>();
                result.add(Double.parseDouble(lowerLeftX.getText()));
                result.add(Double.parseDouble(lowerLeftY.getText()));
                result.add(Double.parseDouble(upperRightX.getText()));
                result.add(Double.parseDouble(upperRightY.getText()));

                return result;
            }

            return null;
        });

        Optional<ArrayList<Double>> result = dialog.showAndWait();

        // set the new bbox and update the scene
        result.ifPresent(coordinates -> {
            if(coordinates.size() != 4) return;
            clickedElement.setBoundingBox(
                    new BoundingBox(
                            coordinates.get(0),
                            coordinates.get(1),
                            coordinates.get(2),
                            coordinates.get(3)
                    )
            );
            updateScene(false);
        });
    }

    // just for testing purpose
    private void addBorders() {
        Border wrapperBorder = new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null,
                BorderStroke.THICK));
        Border boardBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null,
                BorderStroke.THIN));

        boardWrapper.setBorder(wrapperBorder);
        ((Pane) boardSceneNode).setBorder(boardBorder);
    }

    /** Pane does not clip its content by default, so it is possible that children’s bounds may extend
     * outside its own bounds, either if children are positioned at negative coordinates or the pane
     * is re-sized smaller than its preferred size.
     */
    private Pane createClippedWrapper() {
        final Pane pane = new Pane();
        pane.setPrefSize(windowWidth, windowHeight-50);

        // clipped children still overwrite border!
        clipChildren(pane);

        return pane;
    }

    /**
     * Clips the children of a pane.
     *
     * @param pane the pane to clip the children from
     */
    private void clipChildren(Pane pane) {
        final Rectangle outputClip = new Rectangle();
        pane.setClip(outputClip);

        pane.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

    /**
     * Clears the scene.
     */
    private void clearScene() {
        contentBox.getChildren().remove(boardWrapper);
        changeBoundingBox.setDisable(true);
    }
}
