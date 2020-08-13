package com.github.moues0w0.jfxstyleviewer;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public final class FXUtils {

    public static <T> T loadFXML(Object root, Object controller, String location) {
        return loadFXML(root, controller, location, null);
    }

    public static <T> T loadFXML(Object root, Object controller, String location, ResourceBundle resources) {
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(root);
        loader.setController(controller);
        ClassLoader classLoader = getClassLoader(getCallerClass());
        loader.setClassLoader(classLoader);
        loader.setLocation(classLoader.getResource(location));
        loader.setResources(resources);
        loader.setCharset(StandardCharsets.UTF_8);
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load fxml", e);
        }
    }

    private static ClassLoader getClassLoader(Class<?> clazz) {
        return clazz != null ? clazz.getClassLoader() : Thread.currentThread().getContextClassLoader();
    }

    private static Class<?> getCallerClass() {
        try {
            return Class.forName(Thread.currentThread().getStackTrace()[3].getClassName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
