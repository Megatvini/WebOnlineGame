package Core.Dao;

import Core.Bean.Game;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Nika on 16:38, 6/26/2015.
 */

public class GameDao {
    public DataSource dataSource;
    public GameDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * saves new game to database
     * @param date when game was finished
     * @return ID of new game in table
     */
    public int addNewGame(Date date) {
        int gameID = 0;
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement("INSERT INTO games (Date) VALUES (?)");
            pst.setDate(1, new java.sql.Date(date.getTime()));
            pst.close();

            pst = conn.prepareStatement("SELECT LAST_INSERT_ID()");
            ResultSet result = pst.executeQuery();
            if (result.next()) gameID = result.getInt(1);
            result.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameID;
    }

    /**
     *
     * @param userName nickname of a user
     * @param limit maximum number of games to be returned
     * @return all games played by user ordered by date
     * last game is at index 0
     */
    public List<Game> getUserGames(String userName, int limit) {
        List<Game> games = new ArrayList<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT PA.GameID, AccID, participations.RatingChange, Date From" +
                        "(SELECT GameID, NickName, RatingChange, Date FROM participations " +
                        "LEFT JOIN accounts " +
                        "ON participations.AccID = accounts.ID " +
                        "LEFT JOIN Games " +
                        "ON GameID = Games.ID " +
                        "WHERE Nickname = ?) AS PA " +
                    "LEFT JOIN participations " +
                    "ON PA.GameID = participations.GameID " +
                    "ORDER BY Date DESC " +
                    "LIMIT ?;");
            pst.setString(1, userName);
            pst.setInt(2, limit);

            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                int gameID = resultSet.getInt("GameID");
                int accID = resultSet.getInt("AccID");
                int ratingChange = resultSet.getInt("ratingChange");
                Date date = resultSet.getDate("Date");

                if (games.size() != 0 && games.get(games.size()-1).getGameID() == gameID) {
                    games.get(gameID).addParticipant(accID);
                } else {
                    Game game = new Game(date, gameID, ratingChange);
                    game.addParticipant(accID);
                    games.add(game);
                }
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    /**
     *
     * @param userID ID of the user whose games will be returned
     * @param limit maximum number of games that will e returned
     * @return all games played by user ordered by date
     * last game is at index 0
     */
    public List<Game> getUserGamesByID(int userID, int limit) {
        List<Game> games = new ArrayList<>();
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "select PA.GameID, AccID, participations.RatingChange, Date FROM " +
                            "(SELECT GameID, RatingChange, Date FROM participations " +
                            "LEFT JOIN Games " +
                            "ON GameID = Games.ID " +
                            "WHERE participations.AccID = ?) AS PA " +
                            "LEFT JOIN participations " +
                            "ON PA.GameID = participations.GameID " +
                            "ORDER BY Date DESC " +
                            "LIMIT ?;");
            pst.setInt(1, userID);
            pst.setInt(2, limit);


            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                int gameID = resultSet.getInt("GameID");
                int accID = resultSet.getInt("AccID");
                int ratingChange = resultSet.getInt("ratingChange");
                Date date = resultSet.getDate("Date");

                if (games.size() != 0 && games.get(games.size()-1).getGameID() == gameID) {
                    games.get(gameID).addParticipant(accID);
                } else {
                    Game game = new Game(date, gameID, ratingChange);
                    game.addParticipant(accID);
                    games.add(game);
                }
            }
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }


    public void addParticipation(int gameID, int accountID, int ratingChange) {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO participations (GameID, AccID, RatingChange) VALUES (?, ?, ?)");
            pst.setInt(1, gameID);
            pst.setInt(2, accountID);
            pst.setInt(3, ratingChange);
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getGamesCount() {
        int res = 0;
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT Count(*) FROM games");
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) res = resultSet.getInt(1);
            pst.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}