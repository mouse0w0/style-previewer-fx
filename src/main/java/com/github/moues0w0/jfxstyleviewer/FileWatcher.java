package com.github.moues0w0.jfxstyleviewer;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FileWatcher implements Runnable {
    private final Map<File, Long> watchingFiles = new HashMap<>();

    private Thread thread;

    private Consumer<File> modifyListener;
    private Consumer<File> deleteListener;

    private long interval = 2000L;

    public FileWatcher() {
        thread = new Thread(this);
        thread.setDaemon(true);
    }

    public Consumer<File> getModifyListener() {
        return modifyListener;
    }

    public void setModifyListener(Consumer<File> modifyListener) {
        this.modifyListener = modifyListener;
    }

    public Consumer<File> getDeleteListener() {
        return deleteListener;
    }

    public void setDeleteListener(Consumer<File> deleteListener) {
        this.deleteListener = deleteListener;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval, TimeUnit timeUnit) {
        this.interval = timeUnit.toMillis(interval);
    }

    public void watch(File file) {
        if (file.exists()) {
            watchingFiles.put(file, file.lastModified());
        } else {
            if (deleteListener != null) deleteListener.accept(file);
        }
    }

    public void run() {
        while (true) {
            Iterator<Map.Entry<File, Long>> iterator = watchingFiles.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<File, Long> entry = iterator.next();
                File file = entry.getKey();
                if (file.exists()) {
                    long lastModified = file.lastModified();
                    if (lastModified != entry.getValue()) {
                        watchingFiles.put(file, lastModified);
                        if (modifyListener != null) modifyListener.accept(file);
                    }
                } else {
                    iterator.remove();
                    if (deleteListener != null) deleteListener.accept(file);
                }
            }

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }
}
