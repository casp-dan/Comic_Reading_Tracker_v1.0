package app;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * SQL Connection
 * @author: Daniel Casper
 **/

public class DBConnection {

    /***
     * This opens the connection to the database
     * @return connection
     */
    public static Connection connectDB(){
        String url = "urlGoesHere"; 
        String username = "username";
        String password = "password";
        //ArrayList<String> tasks = new ArrayList<String>();
        Connection connection;
        try{
            connection = DriverManager.getConnection(url, username, password);
        }catch (SQLException e) {
            throw new IllegalStateException("Cannot connect!", e);
        }
        return connection;
    }


    /***
     * Closes the connection to the database
     * @param connection
     */
    public static void closeDB(Connection connection){
        try{
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException("Cannot close database", e);
        }

    }
}