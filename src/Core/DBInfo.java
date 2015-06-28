package Core;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by gukam on 5/28/2015.
 */
public class DBInfo {
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost/";

    //  Database credentials
    public static final String USER = "root";
    public static final String PASS = "anniemargvela1";
}
