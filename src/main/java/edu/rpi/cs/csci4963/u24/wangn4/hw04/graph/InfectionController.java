package edu.rpi.cs.csci4963.u24.wangn4.hw04.graph;

import edu.rpi.cs.csci4963.u24.wangn4.hw04.graph.infection_model.Graph;
import edu.rpi.cs.csci4963.u24.wangn4.hw04.graph.infection_model.Infection;
import edu.rpi.cs.csci4963.u24.wangn4.hw04.graph.infection_model.NodeState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller class for managing the infection simulation.
 */
public class InfectionController {

    private Infection infection;
    private ScheduledExecutorService executorService;
    private int tickCount = 0;
    private boolean isGraphLoaded = false;

    private double infectionRate = 0.1;
    private double recoveryRate = 0.01;
    private double forceOfInfection = 0.1;
    private int maxInfectionTime = 10;
    private String csvFile;

    @FXML
    private Label welcomeText;

    @FXML
    private MenuBar menuBar;

    @FXML
    private ToolBar toolBar;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button loadGraphButton;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button resetButton;

    @FXML
    private GridPane configGrid;

    @FXML
    private Button configButton;

    @FXML
    private TextField infectionRateField;

    @FXML
    private TextField recoveryRateField;

    @FXML
    private TextField forceOfInfectionField;

    @FXML
    private TextField maxInfectionTimeField;

    private XYChart.Series<Number, Number> infectedSeries;
    private XYChart.Series<Number, Number> recoveredSeries;
    private XYChart.Series<Number, Number> deadSeries;

    /**
     * Initializes the controller and sets up the chart and button actions.
     */
    @FXML
    public void initialize() {
        xAxis.setLabel("Time (ticks)");
        yAxis.setLabel("Number of Nodes");

        infectedSeries = new XYChart.Series<>();
        infectedSeries.setName("Infected");

        recoveredSeries = new XYChart.Series<>();
        recoveredSeries.setName("Recovered");

        deadSeries = new XYChart.Series<>();
        deadSeries.setName("Dead");

        lineChart.getData().clear(); // Clear existing series
        lineChart.getData().addAll(infectedSeries, recoveredSeries, deadSeries);

        loadGraphButton.setOnAction(event -> loadGraph());
        startButton.setOnAction(event -> startSimulation());
        pauseButton.setOnAction(event -> pauseSimulation());
        resetButton.setOnAction(event -> resetSimulation());
    }

    /**
     * Updates the infection parameters.
     *
     * @param newInfectionRate   the new infection rate
     * @param newRecoveryRate    the new recovery rate
     * @param newForceOfInfection the new force of infection
     * @param newMaxInfectionTime the new maximum infection time
     */
    public void updateInfectionParameters(double newInfectionRate, double newRecoveryRate, double newForceOfInfection, int newMaxInfectionTime) {
        if (infection != null) {
            infectionRate = newInfectionRate;
            recoveryRate = newRecoveryRate;
            forceOfInfection = newForceOfInfection;
            maxInfectionTime = newMaxInfectionTime;
        }
    }

    /**
     * Toggles the visibility of the configuration panel.
     */
    @FXML
    private void toggleConfigPanel() {
        configGrid.setVisible(!configGrid.isVisible());
    }

    /**
     * Saves the configuration parameters from the input fields.
     */
    @FXML
    private void saveConfig() {
        double infectionRate = Double.parseDouble(infectionRateField.getText());
        double recoveryRate = Double.parseDouble(recoveryRateField.getText());
        double forceOfInfection = Double.parseDouble(forceOfInfectionField.getText());
        int maxInfectionTime = Integer.parseInt(maxInfectionTimeField.getText());

        updateInfectionParameters(infectionRate, recoveryRate, forceOfInfection, maxInfectionTime);

        configGrid.setVisible(false);
        showDialogue("Configuration Saved", null, "The configuration has been saved successfully. To see the changes load a new graph or reset the simulation.");
    }

    /**
     * Loads a graph from a CSV file and initializes the infection model.
     */
    private void loadGraph() {
        Graph graph = new Graph();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            String fileName = file.getName();
            if (!fileName.endsWith(".csv")) {
                showAlert(AlertType.ERROR, "Invalid File", null, "Please select a valid CSV file.");
                return;
            }

            csvFile = file.toString();

            infection = new Infection(graph, infectionRate, recoveryRate, forceOfInfection, maxInfectionTime, csvFile);

            initialize();
            updateChart();
            showDialogue("Graph Loaded", null, "The graph has been successfully loaded.");
            isGraphLoaded = true;
            resetSimulation();
        }
    }

    /**
     * Starts the infection simulation.
     */
    private void startSimulation() {
        if (!isGraphLoaded) {
            showAlert(AlertType.WARNING, "Warning", null, "Please load the graph before starting or resetting the simulation.");
            return;
        }
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            infection.tick();
            updateChart();
            if (infection.getGraph().getNodes().stream().allMatch(node -> node.getState() == NodeState.RECOVERED || node.getState() == NodeState.DEAD)) {
                executorService.shutdown();
                showDialogue("Simulation Complete", null, "The simulation has completed. All nodes have recovered or died.");
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Pauses the infection simulation.
     */
    private void pauseSimulation() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    /**
     * Resets the infection simulation.
     */
    private void resetSimulation() {
        if (!isGraphLoaded) {
            showAlert(AlertType.WARNING, "Warning", null, "Please load the graph before starting or resetting the simulation.");
            return;
        }
        if (executorService != null) {
            executorService.shutdown();
        }
        tickCount = 0;
        infectedSeries.getData().clear();
        recoveredSeries.getData().clear();
        deadSeries.getData().clear();

        Graph graph = new Graph();
        infection = new Infection(graph, infectionRate, recoveryRate, forceOfInfection, maxInfectionTime, csvFile);
        updateChart();
    }

    /**
     * Updates the chart with the current infection data.
     */
    private void updateChart() {
        Platform.runLater(() -> {
            int infectedCount = (int) infection.getGraph().getNodes().stream().filter(node -> node.getState() == NodeState.INFECTED).count();
            int recoveredCount = (int) infection.getGraph().getNodes().stream().filter(node -> node.getState() == NodeState.RECOVERED).count();
            int deadCount = (int) infection.getGraph().getNodes().stream().filter(node -> node.getState() == NodeState.DEAD).count();

            // Todo Testing Purposes Remove
            System.out.println(infectedCount);
            System.out.println(recoveredCount);
            System.out.println(deadCount);

            infectedSeries.getData().add(new XYChart.Data<>(tickCount, infectedCount));
            recoveredSeries.getData().add(new XYChart.Data<>(tickCount, recoveredCount));
            deadSeries.getData().add(new XYChart.Data<>(tickCount, deadCount));

            tickCount++;
        });
    }

    /**
     * Shows an information dialogue.
     *
     * @param title   the title of the dialogue
     * @param header  the header text of the dialogue
     * @param content the content text of the dialogue
     */
    private void showDialogue(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Shows an alert dialogue.
     *
     * @param type    the type of the alert
     * @param title   the title of the alert
     * @param header  the header text of the alert
     * @param content the content text of the alert
     */
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}