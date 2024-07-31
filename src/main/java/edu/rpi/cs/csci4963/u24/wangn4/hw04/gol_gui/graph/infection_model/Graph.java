package edu.rpi.cs.csci4963.u24.wangn4.hw04.gol_gui.graph.infection_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Graph class for representing a directed graph of Nodes via directed Edges.
 * Nodes are connected to each other via Edges.
 * Edges are labeled with a string.
 */
public class Graph {
    public static final boolean DISABLE_CHECK_REP = true;

    private final Map<String, Node> nodesById;
    /*
        Abstraction function:
            The nodes represent all the nodes in this graph.
            All nodes must have a unique id and unique data.
        Rep Invariant:
            The nodes are mapped using their data field and id to the node object.
     */

    /**
     * Constructs an instance of Graph with empty maps.
     */
    public Graph() {
        this.nodesById = new HashMap<>();

        checkRep();
    }

    /**
     * Checks ensuring the fields are storing valid data.
     *
     * @throws RuntimeException if a field or any key or value are null
     */
    private void checkRep() {
        if (Graph.DISABLE_CHECK_REP)
            return;

        if (nodesById.containsValue(null))
            throw new RuntimeException("Graph.nodes contains a value of null");
    }

    /**
     * Adds a node into this graph
     *
     * @param node the node to insert into this graph
     * @return true if successfully added, false if not added
     */
    public boolean addNode(Node node) {
        if (node == null ||  nodesById.containsKey(node.getId()))
            return false;

        nodesById.put(node.getId(), node);
        checkRep();
        return true;
    }

    /**
     * Removes the Node specified.
     *
     * @param node the node to remove from the graph
     * @return true if successfully removed otherwise false
     */
    public boolean removeNode(Node node) {
        if (node == null || !containsNode(node))
            return false;

        nodesById.remove(node.getId());
        checkRep();
        return true;
    }

    /**
     * Adds an edge into this graph.
     *
     * @param parent the originating node
     * @param child  the destination node
     * @param label  the label to apply to the edge for identification
     * @return true if added to the graph, false if any of the params are null
     */
    public boolean addEdge(Node parent, Node child, String label) {
        if (parent == null || child == null || label == null)
            return false;

        // Reflexive edge because we don't have a directed graph
        parent.addChild(new Edge(parent, child, label));
        child.addChild(new Edge(parent, child, label));
        checkRep();
        return true;
    }

    /**
     * Searches the graph to determine if a Node is in the graph using a Node
     *
     * @param node the Node to search for
     * @return true if found, false if not found
     */
    public boolean containsNode(Node node) {
        return nodesById.containsValue(node);
    }

    /**
     * Using the id of a Node get the Node object.
     *
     * @param id the id of the Node
     * @return the Node if found or null if not found
     */
    public Node getNode(String id) {
        return nodesById.get(id);
    }

    /**
     * A list of all Nodes in the graph.
     *
     * @return a list of all nodes within this graph
     */
    public List<Node> getNodes() {
        return new ArrayList<>(nodesById.values());
    }

    /**
     * Returns the number of nodes in the graph
     *
     * @return the number of nodes in the graph
     */
    public int size() {
        return nodesById.size();
    }

    /**
     * Returns the number of edges in the graph
     *
     * @return the number of edges in the graph
     */
    public int edgeSize() {
        int edgeCount = 0;
        for (Node node : getNodes()) {
            edgeCount += node.getChildren().size();
        }
        return edgeCount;
    }

}