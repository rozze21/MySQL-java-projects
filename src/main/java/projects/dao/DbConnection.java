package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import projects.exception.DbException;

public class DbConnection {

  private static String HOST = "localhost";
  private static String PASSWORD = "projects";
  private static int PORT = 3306;
  private static String SCHEMA = "projects";
  private static String USER = "projects";
  
  public static Connection getConnection() {
    // string variable
    String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false", 
        HOST, PORT, SCHEMA, USER, PASSWORD);

    try {

      Connection conn = DriverManager.getConnection(url);
      // printed message got connection
      System.out.println("Successfully obtained connection!");
      return conn;
    } catch (SQLException e) { // catch
      // message if there is no connection
      System.out.printf("Unable to get connection at %s%n", url);
      throw new DbException(e); // throw

    }
  }
}
