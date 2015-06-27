package Core.Dao;

import Core.Bean.Account;
import Interfaces.iAccount;
import Interfaces.iProfile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Nika on 02:20, 6/25/2015.
 */

public class AccountDao {
    private DataSource dataSource;

    public AccountDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * register new account to database
     * does not check if account is already registered
     * @param account info about new account
     * @return return true iif account was successfully created
     */
    public boolean registerUser(iAccount account){
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement("INSERT INTO accounts " +
                    "(Nickname, LastName,  FirstName,  Gender,  Password,  BirthDate,  about,  GameRating,  Mail, Picture) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            stmt.setString(1, account.getNickname());
            stmt.setString(2, account.getLastName());
            stmt.setString(3, account.getFirstName());
            stmt.setString(4, account.getGender() == iProfile.Gender.FEMALE? "female":"male");
            stmt.setString(5, account.getPassword());
            java.sql.Date date = new java.sql.Date(account.getBirthDate().getTime());
            stmt.setDate(6, date);
            stmt.setString(7, account.getAbout());
            stmt.setInt(8, account.getRating());
            stmt.setString(9, account.getMail());
            stmt.setString(10, account.getPicturePath());
            System.out.println(stmt.toString());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
        return true;
    }

    /**
     * updates user
     * @param account account which will be saved in database
     * @return true iff user was successfully updated
     */
    public boolean changeUser(iAccount account){
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement("UPDATE accounts SET " +
                    "LastName = ?, FirstName = ?, Gender = ?, Password = ?, " +
                    "BirthDate = ?, about = ?, GameRating = ?, Picture = ? WHERE ID = ?;");
            stmt.setString(1, account.getLastName());
            stmt.setString(2, account.getFirstName());
            stmt.setString(3, account.getGender() == iProfile.Gender.FEMALE ? "female" : "male");
            stmt.setString(4, account.getPassword());
            stmt.setDate(5, new java.sql.Date(account.getBirthDate().getTime()));
            stmt.setString(6, account.getAbout());
            stmt.setInt(7, account.getRating());
            stmt.setString(8, account.getPicturePath());
            stmt.setInt(9, account.getID());
            stmt.execute();

            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }  finally {
            try {
                if (stmt != null) stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
        return true;
    }

    /**
     * returns user from database if found
     * @param nickname name of a user
     * @return Account with nickname
     * @throws Exception if user with nickname was not found
     */
    public iAccount getUser(String nickname) throws Exception {
        iAccount res;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement("SELECT * FROM accounts WHERE NickName = ?;");
            stmt.setString(1, nickname);
            ResultSet result = stmt.executeQuery();
            if (!result.next()) throw new Exception("notRegistered");
            res = assembleAccount(result);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
        return res;
    }

    /**
     * assembles account from resultSet
     * @param result resultSet from database
     * @return assembled account
     * @throws SQLException if resultSet throws exception
     */
    private iAccount assembleAccount(ResultSet result) throws SQLException {
        iAccount account = new Account();
        account.setID(result.getInt("ID"));
        account.setNickname(result.getString("Nickname"));
        account.setLastName(result.getString("LastName"));
        account.setFirstName(result.getString("FirstName"));
        account.setGender(result.getString("Gender").equals("Male") ? Account.Gender.MALE : Account.Gender.FEMALE);
        account.setPassword(result.getString("password"));
        account.setBirthDate(result.getDate("birthDate"));
        account.setAbout(result.getString("about"));
        account.setRating(result.getInt("GameRating"));
        account.setMail(result.getString("Mail"));
        account.setPicturePath(result.getString("Picture"));
        return account;
    }


    /**
     * get all users whose nickname contains search
     * @param search substring for searching
     * @return set of all users with substring search in nickname
     */
    public Set<String> getUsersLike(String search){
        Set<String> accounts = new HashSet<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement("SELECT Nickname FROM accounts WHERE NickName LIKE ?;");
            stmt.setString(1, "%"+search+"%");
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                accounts.add(result.getString("Nickname"));
            }
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
        return accounts;
    }

    /**
     * get all users whose nickname contains search and who are on page pageNumber
     * @param search substring for searching
     * @param pageNumber index of page from which accounts will be returned
     * @param accountsPerPage how much accounts are on single page
     * @return set of all users with substring search in nickname
     */
    public Set<String> getUsersLike(String search, int pageNumber, int accountsPerPage){
        Set<String> accounts = new HashSet<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT Nickname FROM accounts " +
                    "WHERE NickName LIKE ? " +
                    "ORDER BY Nickname LIMIT ?, ?;");
            stmt.setString(1, "%"+search+"%");
            stmt.setInt(2, pageNumber*accountsPerPage);
            stmt.setInt(3, (pageNumber + 1)*accountsPerPage);

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                accounts.add(result.getString("Nickname"));
            }
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return accounts;
    }

    /**
     *
     * @return total number of users in database
     */
    public int getUsersCount() {
        int res = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement(
                    "SELECT Count(*) FROM accounts");
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) res = resultSet.getInt(1);
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            try {
                if (pst != null) pst.close();
                if(conn != null) conn.close();
            } catch (SQLException ignored) {}
        }
        return res;
    }

    /**
     *
     * @param pageNumber index of page from which user will be returned
     * @param accountsPerPage number of players on a single page
     * @return users sorted by rating on pages user with lowest
     * rating is on last index
     */
    public List<String> getUsersIntervalByRating(int pageNumber, int accountsPerPage) {
        List<String> accounts = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT Nickname, GameRating FROM accounts " +
                    "ORDER BY GameRating DESC " +
                    "LIMIT ?, ?");
            stmt.setInt(1, pageNumber*accountsPerPage);
            stmt.setInt(2, (pageNumber+1)*accountsPerPage);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                accounts.add(result.getString("Nickname"));
            }
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }  finally {
            try {
                if (stmt != null) stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
        return accounts;
    }
}
