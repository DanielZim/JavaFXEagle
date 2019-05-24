package main;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import model.enums.PadShape;
import model.enums.ViaShape;
import model.enums.WireStyle;
import model.sceneObjects.*;
import model.sceneObjects.coordinateTransformables.*;
import parser.EagleXmlReader;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        EagleSceneBuilder sceneBuilder = new EagleSceneBuilder(primaryStage);
        sceneBuilder.start();
    }

    /**
     * Main method. Launched the Application.
     *
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
