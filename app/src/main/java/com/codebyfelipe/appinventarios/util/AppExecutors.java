package com.codebyfelipe.appinventarios.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors instance;
    private final ExecutorService diskIO;

    private AppExecutors() {
        diskIO = Executors.newSingleThreadExecutor(); //Se crea un nuevo hilo dedicado para las operaciones de Room, para no tocar la base de datos desde el hilo principal
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