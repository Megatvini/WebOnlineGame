package Core.Dao;

import Core.Bean.Account;
import Interfaces.iAccount;
import Interfaces.iProfile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Nika on 02:20, 6/25/2015.
 */

public class AccountDao {
    private DataSource dataSource;

    public AccountDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean registerUser(iAccount account){
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO accounts " +
                    "(Nickname, LastName,  FirstName ,  Gender ,  Password ,  BirthDate ,  about ,  GameRating ,  Mail) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            stmt.setString(1, account.getNickname());
            stmt.setString(2, account.getLastName());
            stmt.setString(3, account.getFirstName());
            stmt.setString(4, account.getGender() == iProfile.Gender.FEMALE? "female":"male");
            stmt.setString(5, account.getPassword());
            stmt.setDate(6, account.getBirthDate());
            stmt.setString(7, account.getAbout());
            stmt.setInt(8, account.getRating());
            stmt.setString(9, account.getMail());
            System.out.println(stmt.toString());
            stmt.execute();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean changeUser(iAccount account){
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE accounts SET " +
                    "LastName = ?, FirstName = ?, Gender = ?, Password = ?, " +
                    "BirthDate = ?, about = ?, GameRating = ? WHERE ID = ?;");
            stmt.setString(1, account.getLastName());
            stmt.setString(2, account.getFirstName());
            stmt.setString(3, account.getGender() == iProfile.Gender.FEMALE ? "female" : "male");
            stmt.setString(4, account.getPassword());
            stmt.setDate(5, account.getBirthDate());
            stmt.setString(6, account.getAbout());
            stmt.setInt(7, account.getRating());
            stmt.setInt(8, account.getID());
            System.out.println(stmt.toString());
            stmt.execute();

            stmt.close();
            connection.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public iAccount getUser(String nickname) throws Exception {
        iAccount res = null;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM accounts WHERE NickName = ?;");
            stmt.setString(1, nickname);
            ResultSet result = stmt.executeQuery();
            if (!result.next()) throw new Exception("notRegistered");
            res = assembleAccount(result);
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return res;
    }

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
        return account;
    }

    public Set<String> getUsersLike(String search){
        Set<String> accounts = new HashSet<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT Nickname FROM accounts WHERE NickName LIKE ?;");
            stmt.setString(1, "%"+search+"%");
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                accounts.add(result.getString("Nickname"));
            }
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            return null;
        }
        return accounts;
    }
}
