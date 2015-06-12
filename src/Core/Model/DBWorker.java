package Core.Model;

import java.sql.*;

/**
 * Created by Annie on 12-Jun-15.
 */
public class DBWorker {
    public static ResultSet getResult(String query) {
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
                    "user=root&password=anniemargvela1");

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }


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
}
