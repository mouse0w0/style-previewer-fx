package com.github.moues0w0.stylepreviewer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StylePreviewerApp extends Application {

    private FileWatcher fileWatcher;

    private TabPane root;
    @FXML
    private ListView<String> list;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXUtils.loadFXML(null, this, "Main.fxml");

        fileWatcher = new FileWatcher();
        fileWatcher.setModifyListener(file ->
                Platform.runLater(() -> {
                    ObservableList<String> stylesheets = root.getStylesheets();
                    stylesheets.set(0, stylesheets.get(0));
                }));
        fileWatcher.setDeleteListener(file -> Platform.runLater(() -> removeStylesheet(FileUtils.toExternalForm(file))));
        fileWatcher.setInterval(1000, TimeUnit.MILLISECONDS);
        fileWatcher.start();

        list.setCellFactory(listView -> new ListCell<String>() {

            ContextMenu menu = new ContextMenu();

            {
                MenuItem delete = new MenuItem("Delete");
                delete.setOnAction(event -> {
                    String item = getItem();
                    removeStylesheet(item);
                });
                menu.getItems().add(delete);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setContextMenu(null);
                } else {
                    setText(item);
                    setContextMenu(menu);
                }
            }
        });

        root.getTabs().addAll(
                new Tab("Texts&TextInputs", FXUtils.loadFXML(null, null, "Texts&TextInputs.fxml")),
                new Tab("Buttons&ChoiceBoxes", FXUtils.loadFXML(null, null, "Buttons&ChoiceBoxes.fxml")),
                new Tab("ProgressBars&Slider", FXUtils.loadFXML(null, null, "ProgressBars&Slider.fxml")),
                new Tab("Menus&Layouts", FXUtils.loadFXML(null, null, "Menus&Layouts.fxml")));

        Scene scene = new Scene(root);
        scene.setOnDragOver(event -> {
            event.consume();
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.LINK);
            }
        });
        scene.setOnDragDropped(event -> {
            event.consume();

            List<File> files = event.getDragboard().getFiles();
            for (File file : files) {
                String url = FileUtils.toExternalForm(file);
                root.getStylesheets().add(url);
                list.getItems().add(url);
                fileWatcher.watch(file);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void removeStylesheet(String url) {
        root.getStylesheets().remove(url);
        list.getItems().remove(url);
    }
}
