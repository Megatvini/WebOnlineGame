package Interfaces.Model;

import java.sql.*;

/**
 * Created by gukam on 5/28/2015.
 */
public interface DBManager {
    public void excecute(Statement stmt);

    public Connection  getConnection();
}
