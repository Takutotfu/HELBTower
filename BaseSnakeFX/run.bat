@echo off

javac --module-path "%PATH_TO_FX%" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web Main.java

java --module-path "%PATH_TO_FX%" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web Main

erase *.class
