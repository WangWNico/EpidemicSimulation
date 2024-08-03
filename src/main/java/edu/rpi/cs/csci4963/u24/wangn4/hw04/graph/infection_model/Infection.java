package edu.rpi.cs.csci4963.u24.wangn4.hw04.graph.infection_model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Infection class models the spread of an infection through a graph.
 */
public class Infection {
    private Graph graph;
    private final ExecutorService executor;
    private double recoveryRate;
    private double infectionRate;
    private int maxInfectionTime;

    private double force_of_infection;
    private double prevForceOfInfection;
    private long prevInfectionCount = 0;
    private long infectionCount = 0;
    private long totalSusceptibleNeighbors = 0;

    /**
     * Constructs an Infection model from a CSV file.
     *
     * @param graph the graph representing the network of nodes
     * @param infectionRate the rate at which nodes become infected
     * @param recoveryRate the rate at which nodes recover from infection
     * @param force_of_infection the initial force of infection
     * @param maxInfectionTime the maximum time a node can be infected
     * @param csvFile the path to the CSV file containing the graph data
     */
    public Infection(Graph graph, double infectionRate, double recoveryRate, double force_of_infection, int maxInfectionTime ,String csvFile) {
        this.graph = graph;
        this.infectionRate = infectionRate;
        this.recoveryRate = recoveryRate;
        this.prevForceOfInfection = force_of_infection;
        this.force_of_infection = force_of_infection;
        this.maxInfectionTime = maxInfectionTime;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // Create infection model from csv file
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] values = line.split(";");
                if (values.length < 2) {
                    System.err.println("Invalid line format: " + line);
                    continue; // Skip invalid lines
                }
                String parent = values[0];
                Node parentNode = graph.getNode(parent);
                // infect the node based on the infection rate
                if (parentNode == null) {
                    parentNode = new Node(parent, Math.random() < infectionRate ? NodeState.INFECTED : NodeState.SUSCEPTIBLE);
                    graph.addNode(parentNode);
                    infectionCount++;
                }
                for (int i = 1; i < values.length; i++) {
                    String child = values[i];
                    Node childNode = graph.getNode(child);
                    // infect the node based on the infection rate
                    if (childNode == null) {
                        childNode = new Node(child, Math.random() < infectionRate ? NodeState.INFECTED : NodeState.SUSCEPTIBLE);
                        graph.addNode(childNode);
                    }
                    graph.addEdge(parentNode, childNode, "edge");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Infects susceptible neighbors of infected nodes.
     */
    public synchronized void infect() {
        int totalInfected = 0;
        totalSusceptibleNeighbors = 0;
        for (Node node : graph.getNodes()) {
            // if infected node, grab neighbors and infect based on force of infection
            if (node.getState() == NodeState.INFECTED) {
                totalInfected++;
                for (Edge edge : node.getChildren()) {
                    Node child = edge.getChild();
                    if (child.getState() == NodeState.SUSCEPTIBLE && Math.random() < force_of_infection) {
                        totalSusceptibleNeighbors++;
                        executor.submit(() -> {
                            synchronized (child) {
                                infectionCount++;
                                child.setState(NodeState.INFECTED);
                                child.updateInfectedTime();
                            }
                        });
                    }
                }
            }
        }

        if (totalInfected > 0) {
            force_of_infection = (double) totalSusceptibleNeighbors / totalInfected;
        } else {
            force_of_infection = 0;
        }
    }

    /**
     * Recovers or kills infected nodes based on the recovery rate and maximum infection time.
     */
    public synchronized void recover() {
        for (Node node : graph.getNodes()) {
            executor.submit(() -> {
                // if infected node, recover or kill based on time infected and recovery rate
                if (node.getState() == NodeState.INFECTED) {
                    if (node.getTimeInfected() == maxInfectionTime && Math.random() < recoveryRate) {
                        synchronized (node) {
                            node.setState(NodeState.RECOVERED);
                            infectionCount--;
                        }
                    }
                    else if (node.getTimeInfected() == maxInfectionTime) {
                        synchronized (node) {
                            node.setState(NodeState.DEAD);
                            infectionCount--;
                        }
                    }
                    else synchronized (node) {
                            node.updateInfectedTime();
                        }
                }
            });
        }
    }

    /**
     * Advances the infection model by one tick, updating the infection and recovery states.
     */
    public synchronized void tick() {
        prevForceOfInfection = force_of_infection;
        prevInfectionCount = infectionCount;
        infect();
        recover();
        updateForceOfInfection();
    }

    /**
     * Updates the force of infection based on the current and previous infection states.
     */
    public synchronized void updateForceOfInfection() {
        force_of_infection = (double) (prevForceOfInfection * prevInfectionCount + totalSusceptibleNeighbors) / infectionCount;
    }

    /**
     * Sets the maximum infection time for nodes.
     *
     * @param maxInfectionTime the new maximum infection time
     */
    public void setMaxInfectionTime(int maxInfectionTime) {
        this.maxInfectionTime = maxInfectionTime;
    }

    /**
     * Sets the recovery rate for nodes.
     *
     * @param recoveryRate the new recovery rate
     */
    public void setRecoveryRate(double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    /**
     * Gets the graph representing the network of nodes.
     *
     * @return the graph
     */
    public Graph getGraph() {
        return graph;
    }
}