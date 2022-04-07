package ru.nsu.databaseproject.database.user;

import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserRequests {
    @Setter
    private Connection connection;

    public Integer addUser(User user) {
        int userId = getNextUserId();
        user.setUserId(userId);

        String sqlAddUser = "INSERT INTO pharmacyAdmin.user_table VALUES (" +
                userId + ", " +
                "'" + user.getUserName() + "' ," +
                "'" + user.getPassword() + "' ," +
                 user.getUserRole() +
                ")";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlAddUser);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't add new user info");
            e.printStackTrace();
        }
        return userId;
    }

    public User getUser(Integer userId) {
        String sqlGetUser = "SELECT  * FROM pharmacyAdmin.user_table " +
                "WHERE user_id=" + userId;
        User user = new User();
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlGetUser);
            resultSet = preparedStatement.executeQuery(sqlGetUser);
        } catch (SQLException e) {
            System.err.println("Can't get user");
            e.printStackTrace();
        }
        try {
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt("user_id"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setUserRole(Role.valueOf(resultSet.getString("user_role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getUsers() {
        String sqlGetUsers = "SELECT  * FROM pharmacyAdmin.user_table ";
        List<User> users = new LinkedList<>();
        ResultSet resultSet = null;
        try {
            PreparedStatement preStatementReader = connection.prepareStatement(sqlGetUsers);
            resultSet = preStatementReader.executeQuery(sqlGetUsers);
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setUserRole(Role.valueOf(resultSet.getString("user_role")));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("can't get list of users");
            e.printStackTrace();
        }
        return users;
    }

    public User getUserByName(String userName) {
        String sqlGetUserByName = "SELECT  * FROM pharmacyAdmin.user_table " +
                "WHERE username='" + userName + "'";
        User user = new User();
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlGetUserByName);
            resultSet = preparedStatement.executeQuery(sqlGetUserByName);
        } catch (SQLException e) {
            System.err.println("Can't get user by name");
            e.printStackTrace();
        }
        try {
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt("user_id"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setUserRole(Role.valueOf(resultSet.getString("user_role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUser(Integer userId) {
        String sqlDeleteUser = "DELETE FROM pharmacyAdmin.user_table WHERE user_id=" + userId;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUser);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't delete user");
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        String sqlUpdateUser = "UPDATE pharmacyAdmin.user_table SET ";
        sqlUpdateUser += "username= '" + user.getUserName() + "' ";
        sqlUpdateUser += ",password= '" + user.getPassword()+ "' ";
        sqlUpdateUser += ",user_role= '" + user.getUserRole()+ "' ";
        sqlUpdateUser += "WHERE user_id=" + user.getUserId();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateUser);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't update user");
            e.printStackTrace();
        }
    }

    public Integer getNextUserId() {
        String sqlGetNextUserId = "SELECT sq_user.nextval FROM DUAL";
        Integer nextID = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlGetNextUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
                nextID = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextID;
    }

    public void createNewUser(String login, String password) throws SQLException {
        connection.setAutoCommit(false);
        String sqlCreateUser = "create user " + login + " identified by " + password;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCreateUser);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't create new user");
            e.printStackTrace();
        }
        String sqlGrant = "grant pharmacy_user to  " + login;
        try {
            PreparedStatement preStatementReader = connection.prepareStatement(sqlGrant);
            preStatementReader.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't grant role");
            e.printStackTrace();
        }
        connection.commit();
        connection.setAutoCommit(true);
    }

    public Integer createNewUser(User user) throws SQLException {
        connection.setAutoCommit(false);
        String sqlCreateUser = "create user " + user.getUserName() + " identified by " + user.getPassword();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlCreateUser);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't create new user");
            connection.rollback();
            connection.setAutoCommit(true);
            e.printStackTrace();
        }
        String sqlGrant = "grant pharmacy_user to  " + user.getUserName();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlGrant);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't grant role");
            e.printStackTrace();
            connection.rollback();
            connection.setAutoCommit(true);
        }
        Integer userId=addUser(user);
        connection.commit();
        connection.setAutoCommit(true);
        return userId;
    }
}
