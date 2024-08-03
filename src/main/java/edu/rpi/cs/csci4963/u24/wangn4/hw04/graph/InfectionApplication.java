package edu.rpi.cs.csci4963.u24.wangn4.hw04.graph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The InfectionApplication class is the main entry point for the infection simulation application.
 */
public class InfectionApplication extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage for this application
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InfectionApplication.class.getResource("infection-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Infection Simulation");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}