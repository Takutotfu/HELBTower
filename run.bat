@echo off

javac --module-path "%PATH_TO_FX%" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web SnakeFXGame.java

java --module-path "%PATH_TO_FX%" --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web SnakeFXGame

erase *.class
