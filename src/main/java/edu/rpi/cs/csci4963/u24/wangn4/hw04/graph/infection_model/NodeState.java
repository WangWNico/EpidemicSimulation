package edu.rpi.cs.csci4963.u24.wangn4.hw04.graph.infection_model;

public enum NodeState {
    /**
     * Represents a node that is susceptible to infection.
     */
    SUSCEPTIBLE,

    /**
     * Represents a node that is currently infected.
     */
    INFECTED,

    /**
     * Represents a node that has recovered from infection.
     */
    RECOVERED,

    /**
     * Represents a node that has died from infection.
     */
    DEAD
}