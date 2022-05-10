package ru.nsu.pharmacydatabase;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

public class Connection {
    private java.sql.Connection connection;

    private static final String defaultIP = "84.237.50.81";
    private static final String localIP = "127.0.0.1";
    private static final String defaultPort = "1521";
    private static final String login = "19203_PETROVA";
    private static final String password = "1234";
    private static final String driver = "oracle.jdbc.driver.OracleDriver";
    public String userId;

    public Connection() {
    }

    public java.sql.Connection getConnection() {
        return connection;
    }

    public void registerDefaultConnection() throws SQLException, ClassNotFoundException {
        registerConnection(defaultIP, login, password);
    }

    private void registerConnection(String ip, String login, String password) throws SQLException, ClassNotFoundException {
        Class.forName(driver);

        Properties props = new Properties();
        props.setProperty("user", login);
        props.setProperty("password", password);
        createConnection();

        System.out.println("register connection to " + ip + "...");
        connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip + ":" + Connection.defaultPort + ":", props);

        if (connection.isValid(1)) {
            System.out.println("success connection to " + ip);
        } else {
            System.out.println("bad connection to " + ip);
        }
    }

    private void createConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        TimeZone timeZone = TimeZone.getTimeZone("GMT+7");
        TimeZone.setDefault(timeZone);
        Locale.setDefault(Locale.ENGLISH);
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            System.out.println("connection was closed");
        } else {
            System.out.println("connection is not registered");
        }
    }
}

