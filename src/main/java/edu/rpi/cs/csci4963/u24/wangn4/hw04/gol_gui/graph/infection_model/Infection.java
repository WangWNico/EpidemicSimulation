package edu.rpi.cs.csci4963.u24.wangn4.hw04.gol_gui.graph.infection_model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Infection class for infecting a graph and progressing the simulation.
 */
public class Infection {
    private final Graph graph;
    // Todo: Infection rate could be implemented elsewhere to simulate random infection to susceptible nodes
    private final int infectionRate;
    private final int recoveryRate;
    private int maxInfectionTime = 3;

    /**
     * Constructs an instance of Infection with the provided data.
     *
     * @param graph The graph to infect
     * @param infectionRate The rate of infection
     * @param recoveryRate The rate of recovery
     * @param csvFile The csv file to construct the model off of
     */
    public Infection(Graph graph, int infectionRate, int recoveryRate, String csvFile) {
        this.graph = graph;
        this.infectionRate = infectionRate;
        this.recoveryRate = recoveryRate;
        // Create infection model from csv file
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                String parent = values[0];
                for (String value : values) {
                    // Randomly infected added nodes
                    if (Math.random() < infectionRate) {
                        Node node = new Node(value, NodeState.INFECTED);
                        if (!graph.containsNode(node)) {
                            graph.addNode(node);
                        }
                    }
                    else {
                        Node node = new Node(value, NodeState.SUSCEPTIBLE);
                        if (!graph.containsNode(node)) {
                            graph.addNode(node);
                        }
                    }
                    // Skip parent no need to add edges to itself
                    if (value.equals(parent)) {
                        continue;
                    }
                    // Add edge from parent to its children
                    graph.addEdge(graph.getNode(parent), graph.getNode(value), "edge");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Infects the graph based on the infection rate.
     */
    public void infect() {
        for (Node node : graph.getNodes()) {
            if (node.getState() == NodeState.SUSCEPTIBLE) {
                for (Edge edge : node.getChildren()) {
                    if (node.getTimeInfected() == 0 && edge.getChild().getState() == NodeState.INFECTED) {
                        node.setState(NodeState.INFECTED);
                        node.updateInfectedTime();
                        break;
                    }
                }
            }
        }
    }

    /**
     * Sets the maximum time a node can be infected.
     *
     * @param maxInfectionTime The maximum time a node can be infected
     */
    public void setMaxInfectionTime(int maxInfectionTime) {
        this.maxInfectionTime = maxInfectionTime;
    }


    /**
     * Recovers the graph based on the infection rate.
     */
    public void recover() {
        for (Node node : graph.getNodes()) {
            if (node.getState() == NodeState.INFECTED) {
                if (node.getTimeInfected() == maxInfectionTime && Math.random() < recoveryRate) {
                    node.setState(NodeState.RECOVERED);
                }
            }
        }
    }

    /**
     * Progresses the simulation by one tick.
     */
    public void tick() {
        infect();
        recover();
    }

}
