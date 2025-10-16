package br.com.todolist.repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseManager {

    private static final String PERSISTENCE_UNIT_NAME = "todolist";
    private static EntityManagerFactory factory;

    private DatabaseManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory;
    }

    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}