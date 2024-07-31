module edu.rpi.cs.csci4963.u24.rcsid.hw02.gol_gui.graph {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.rpi.cs.csci4963.u24.wangn4.hw04.gol_gui.graph to javafx.fxml;
    exports edu.rpi.cs.csci4963.u24.wangn4.hw04.gol_gui.graph;
}