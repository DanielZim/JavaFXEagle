package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    // for drag event handling
    private double startMouseX;
    private double startMouseY;
    private double startTranslationX;
    private double startTranslationY;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        addMouseEventHandler(root);

        Pane pane1 = new Pane();
        Text text1 = new Text(400, 400, "Text 1");
        text1.setFont(new Font(5));
        pane1.getChildren().addAll(text1);
        root.getChildren().add(pane1);

        HBox hbox = new HBox(
                new Text("T"),
                new Text("e"),
                new Text("x"),
                new Text("t"),
                new Text(" "),
                new Text("1"));

        root.getChildren().add(hbox);

        primaryStage.setTitle("Text scaling problem");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();
    }

    private void addMouseEventHandler(Pane root) {
        // add scroll handling to zoom in and out
        root.setOnScroll((event) ->
        {
            double factor = event.getDeltaY() > 0 ? 1.1 : 0.9;
            root.setScaleX(root.getScaleX() * factor);
            root.setScaleY(root.getScaleY() * factor);
        });

        // add drag handling
        root.setOnMousePressed((mouseEvent) ->
        {
            startMouseX = mouseEvent.getSceneX();
            startMouseY = mouseEvent.getSceneY();

            startTranslationX = root.getTranslateX();
            startTranslationY = root.getTranslateY();
        });

        root.setOnMouseDragged((mouseEvent) ->
        {
            double movedX = startMouseX - mouseEvent.getSceneX();
            double movedY = startMouseY - mouseEvent.getSceneY();

            double transX = startTranslationX - movedX;
            double transY = startTranslationY - movedY;

            root.setTranslateX(transX);
            root.setTranslateY(transY);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
