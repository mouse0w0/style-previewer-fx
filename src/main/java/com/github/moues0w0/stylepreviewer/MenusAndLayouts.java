package com.github.moues0w0.stylepreviewer;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

public class MenusAndLayouts extends BorderPane {

    @FXML
    private ListView<String> listView;
    @FXML
    private TreeView<String> treeView;

    public MenusAndLayouts() {
        FXUtils.loadFXML(this, this, "Menus&Layouts.fxml");

        listView.getItems().addAll("Item 1", "Item 2", "Item 3");

        TreeItem<String> root = new TreeItem<>("Root");
        treeView.setRoot(root);
        treeView.setShowRoot(false);
        TreeItem<String> parent1 = new TreeItem<>("Parent 1");
        parent1.getChildren().addAll(new TreeItem<>("Child 1"), new TreeItem<>("Child 2"), new TreeItem<>("Child 3"));
        parent1.setExpanded(true);
        root.getChildren().addAll(parent1, new TreeItem<>("Parent 2"));
    }
}
