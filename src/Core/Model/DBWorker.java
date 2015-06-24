package Core.Model;

import java.sql.*;

/**
 * Created by Annie on 12-Jun-15.
 */
public class DBWorker {


    private Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            System.out.println("VendorError: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("VendorError: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("VendorError: " + e.getMessage());
        }

        Connection conn = null;
        try {
            conn =    DriverManager.getConnection("jdbc:mysql://localhost/mydb?" +
                    "user=root&password=gukaguk1");

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return conn;
    }

    public ResultSet getResult(String query) {
      Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public Integer execute(String query){
        Connection conn = getConnection();
        Statement stmt = null;
        Integer num=0;
        Integer result=-1;
        try {
            stmt = conn.createStatement();
            num = stmt.executeUpdate(query , Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                result=rs.getInt(1);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            result=-1;
        }

        return result;
    }
}
