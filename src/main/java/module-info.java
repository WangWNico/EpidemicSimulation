module edu.rpi.cs.csci4963.u24.wangn4.hw04.graph {
    requires javafx.controls;
    requires javafx.fxml;
    opens edu.rpi.cs.csci4963.u24.wangn4.hw04.graph to javafx.fxml;
    exports edu.rpi.cs.csci4963.u24.wangn4.hw04.graph;
    exports edu.rpi.cs.csci4963.u24.wangn4.hw04.graph.infection_model;
    opens edu.rpi.cs.csci4963.u24.wangn4.hw04.graph.infection_model to javafx.fxml;
}