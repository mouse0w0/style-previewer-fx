package com.github.moues0w0.jfxstyleviewer;

import java.io.File;
import java.net.MalformedURLException;

public class FileUtils {

    public static String toExternalForm(File file) {
        try {
            return file.toURI().toURL().toExternalForm();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
