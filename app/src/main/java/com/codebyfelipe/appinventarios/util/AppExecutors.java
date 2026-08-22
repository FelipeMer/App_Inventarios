package com.codebyfelipe.appinventarios.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors instance;
    private final ExecutorService diskIO;

    private AppExecutors() {
        diskIO = Executors.newSingleThreadExecutor();
    }

    public static synchronized AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    public ExecutorService diskIO() {
        return diskIO;
    }
}