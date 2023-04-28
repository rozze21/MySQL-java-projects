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
  //string variable   
    String uri = 
        String.format("jdbc:mysql://%s:%d/%s",HOST, PORT, SCHEMA);
    try {
      
      Connection conn =  DriverManager.getConnection(uri, USER, PASSWORD);
      //printed message got connection
      System.out.printf("Got connection at %s%n", uri);
      return conn;
    } catch(SQLException e) {   //catch
      //message if there is no connection
      System.out.printf("Unable to get connection at %s%n", uri);
      throw new DbException(e);  //throw
    
   }
  }
}