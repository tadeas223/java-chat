package server;

import java.sql.*;

public class SQLConnection {
    private final String db = "messenger";
    private final String user = "server";
    private final String password = "secretpassword";

    private Connection connection;

    public SQLConnection() {
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://server2:3306/"+db,user,password);
    }

    public void close() throws SQLException {
        connection.close();
    }

    public int login(String username, String password) throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT id, username FROM users WHERE password='"+password+"' AND username='"+username+"'");

        if(resultSet.next()){
            int id = resultSet.getInt("id");

            resultSet.close();
            statement.close();

            return id;
        }

        resultSet.close();
        statement.close();

        return -1;
    }

    public int signup(String username, String password) throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("INSERT INTO users (`username`, `password`) VALUES ('"+username+"', '"+password+"');");

        statement.close();

        return login(username, password);
    }
}
