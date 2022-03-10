package ru.nsu.databaseproject.database;

public class DBRequests {
    private static DBRequests instance;

    public static synchronized DBRequests getInstance() {
        if (instance == null) {
            instance = new DBRequests();
        }
        return instance;
    }
}
