package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO{

    private int nextGameID = 1;

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public int createGame(String gameName, String whiteUsername, String blackUsername) throws DataAccessException, SQLException {
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        ChessGame newGame = new ChessGame();
        //change chess game to json object
        var gameJson = new Gson().toJson(newGame);
        GameData game = new GameData(nextGameID, whiteUsername, blackUsername, gameName, newGame);
        var json = new Gson().toJson(game);
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
                int temp = nextGameID;
                nextGameID++;
                return temp;
            }
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: %s");
        }
        return null;
    }

    private GameData readGame(ResultSet rs) throws SQLException, DataAccessException {
        var gameID = rs.getInt("gameID");
        return getGame(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> result = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: %s");
        }
        return result;
    }

    @Override
    public String updateGame(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            int id = gameData.getGameID();
            String wUser = gameData.getWhiteUsername();
            String bUser = gameData.getBlackUsername();
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)){
                try (var rs = ps.executeQuery()) {
                    if (color == ChessGame.TeamColor.WHITE){
                        if (wUser == null){
                            var updateStatement = "UPDATE game SET whiteUsername = ? WHERE gameID=?";
                            var ts = conn.prepareStatement(updateStatement);
                            ts.executeQuery();
                            return "{}";
                        }
                        else if (wUser.equals(username)){
                            return "{}";
                        }
                        else {
                            return "taken";
                        }
                    }
                    if (color == ChessGame.TeamColor.BLACK){
                        if (bUser == null){
                            var updateStatement = "UPDATE game SET blackUsername = ? WHERE gameID=?";
                            var ts = conn.prepareStatement(updateStatement);
                            ts.executeQuery();
                            return "{}";
                        }
                        else if (bUser.equals(username)){
                            return "{}";
                        }
                        else {
                            return "taken";
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data, %s");
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException, SQLException {
        var statement = "TRUNCATE game";
        var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(statement);
        ps.executeUpdate();
    }

    @Override
    public HashMap<Integer, GameData> getGames() throws DataAccessException {
        var result = new HashMap<Integer, GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.put(getPreviousID(), readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: %s");
        }
        return result;
    }

    @Override
    public GameData getGameByName(String name) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameName=?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: %s");
        }
        return null;
    }

    @Override
    public int getPreviousID() {
        return nextGameID - 1;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              'blackUsername' varchar(256),
              'gameName' varchar(256) NOT NULL,
              'game' ChessGame NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName),
              INDEX(game)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: %s");
        }
    }
}
