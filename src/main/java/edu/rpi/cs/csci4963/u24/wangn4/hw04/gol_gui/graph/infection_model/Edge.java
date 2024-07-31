package edu.rpi.cs.csci4963.u24.wangn4.hw04.gol_gui.graph.infection_model;

import java.util.Objects;

/**
 * Edge class for storing a connection from parent node to child node.
 */
public class Edge {
    private int hash = -1;
    private final Node parent;
    private final Node child;
    private String label;
    private String stringCache = null;

    /**
     * Construct an Edge class instance with the provided data.
     *
     * @param parent the parent node where the edge originates
     * @param child  the child node where the edge points to
     * @param label  the label to apply to the edge
     */
    public Edge(Node parent, Node child, String label) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(child);
        Objects.requireNonNull(label);
        this.parent = parent;
        this.child = child;
        this.label = label;
    }

    /**
     * The label assigned to this class instance.
     *
     * @return The label assigned to this class instance.
     */
    public String getLabel() {
        return label;
    }

    /**
     * The Edge's label will be updated
     */
    public void setLabel(String label) {
        if (label != null)
            this.label = label;
    }

    /**
     * The parent node where this edge originates from.
     *
     * @return The parent node where this edge originates from.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * The child node where this edge points to.
     *
     * @return The child node where this edge points to.
     */
    public Node getChild() {
        return child;
    }

    /**
     * Compares this with the supplied object to check equality via the label field.
     *
     * @param obj the other object to check for equality with.
     * @return true if the same object by reference or label field is the same
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Edge other = (Edge) obj;
        return this.getLabel().equals(other.getLabel());
    }

    /**
     * Uses various hash methods to create a hashcode for this object.
     *
     * @return a hash code for this object
     */
    @Override
    public int hashCode() {
        if (hash == -1)
            hash = this.parent.hashCode() + this.child.hashCode();
        return hash;
    }

    /**
     * Returns a string representation of the edge.
     *
     * @return a string representation of the edge in the format '[parent]\t(label)\t[child]'
     */
    @Override
    public String toString() {
        if (stringCache != null)
            return stringCache;

        stringCache = '[' + parent.getState().toString() + "]\t(" + label + ")\t[" + child.getState().toString() + ']';
        stringCache = stringCache.intern();

        return stringCache;
    }
}
