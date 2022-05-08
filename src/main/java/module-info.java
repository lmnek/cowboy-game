module cvut.gartnkry {
    requires javafx.controls;
    requires gson;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.logging;
    exports cvut.gartnkry;
    exports cvut.gartnkry.model;
    exports cvut.gartnkry.model.map;
    exports cvut.gartnkry.model.entities;
    exports cvut.gartnkry.model.items;
}