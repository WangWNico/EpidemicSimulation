package edu.rpi.cs.csci4963.u24.wangn4.hw04.gol_gui.graph.infection_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Node class for storing a data point within a graph
 */
public class Node {
    private NodeState state;
    private final List<Edge> childrenList;
    private int hash = -1;
    private final String id;
    private int timeInfected = 0;

    /**
     * Constructs an instance of Node with the provided data.
     *
     * @param state The data being stored in this node.
     * @param id The id used to identify the node.
     */
    public Node(String id, NodeState state) {
        Objects.requireNonNull(state);
        this.id = id;
        this.state = state;
        this.childrenList = new ArrayList<>();
    }


    /**
     * Sets the state of the node to the provided state.
     *
     * @param state The new state of the node
     */
    public void setState(NodeState state) {
        this.state = state;
    }

    /**
     * Update timeInfected.
     */
    public void updateInfectedTime() {
        timeInfected++;
    }

    /**
     * Gets the id of this node.
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the time this node has been infected.
     *
     */
    public int getTimeInfected() {
        return timeInfected;
    }

    /**
     * Gets the data this node is storing.
     *
     * @return the data contained within this node
     */
    public NodeState getState() {
        return state;
    }

    /**
     * Gets the hash of this node.
     */
    public int getHash() {
        return hash;
    }

    /**
     * Gets the children of this node.
     */
    public List<Edge> getChildren() {
        return childrenList;
    }

    /**
     * Sets the state this node is storing
     *
     */
    public void setData(NodeState state) {
        Objects.requireNonNull(state);
        this.state = state;
        hash = -1;
    }

    /**
     * Does this node contain a child node given an edge connection?
     *
     * @param edge The Node instance to search for.
     * @return true if node child is a child of this node.
     */
    public boolean containsChild(Edge edge) {
        if (edge == null)
            return false;
        return containsChild(edge.getLabel());
    }

    /**
     * Does this node contain a child node given an edge label?
     *
     * @param edgeLabel The Node instance to search for.
     * @return true if node child is a child of this node.
     */
    public boolean containsChild(String edgeLabel) {
        for (Edge e : childrenList) {
            if (e.getLabel().equals(edgeLabel))
                return true;
        }
        return false;
    }

    /**
     * Adds child node to this node's list of children.
     *
     * @param child the node to add to the children
     */
    public void addChild(Edge child) {
        if (child == null)
            return;
        childrenList.add(child);
    }

    /**
     * Removes an Edge from this node's list of children.
     *
     * @param edge the edge to remove
     * @return true if the child was successfully removed, false if edge is not in the list of children
     */
    public boolean remove(Edge edge) {
        if (edge == null || !containsChild(edge))
            return false;
        return childrenList.remove(edge);
    }

    /**
     * Creates a copy of the children list and returns the copy to the caller.
     *
     * @return a copy of the children edges
     */
    public List<Edge> getChildrenList() {
        return new ArrayList<>(childrenList);
    }

    /**
     * Compares this with the supplied object to check equality via the data field.
     *
     * @param obj the other object to check for equality with.
     * @return true if the same object by reference or data field is the same
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Node other = (Node) obj;
        return this.getState().equals(other.getState());
    }

    /**
     * Uses various hash methods to create a hashcode for this object.
     *
     * @return a hash code for this object
     */
    @Override
    public int hashCode() {
        if (hash == -1)
            hash = 21 * this.state.hashCode();
        return hash;
    }

    /**
     * Returns a string representation of the node.
     *
     * @return a string representation of the node which is the data field's string representation
     */
    @Override
    public String toString() {
        return state.toString();
    }
}
