package Core.Model.Dao;

import Core.DBInfo;

import java.sql.*;

/**
 * Created by Annie on 12-Jun-15.
 */
public class DBWorker {
    private Connection conn;

    public DBWorker() {
        conn = getConnection();
    }

    private Connection getConnection(){
        try {
            Class.forName(DBInfo.JDBC_DRIVER).newInstance();
        } catch (InstantiationException e) {
            System.out.println("VendorError: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("VendorError: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("VendorError: " + e.getMessage());
        }

        Connection conn = null;
        try {
            String s = DBInfo.DB_URL + "mydb?" +
                    "user=" +DBInfo.USER+"&password=" +DBInfo.PASS;

            System.out.println(s);
            conn = DriverManager.getConnection(s);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return conn;
    }


    public ResultSet getResult(String query) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public Integer execute(String query){
        Statement stmt = null;
        Integer num=0;
        Integer result=-1;
        try {
            stmt = conn.createStatement();
            num = stmt.executeUpdate(query , Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            result=-1;
        }
        return result;
    }
}
