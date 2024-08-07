package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public int createGame(String gameName, String whiteUsername, String blackUsername) throws DataAccessException, SQLException {
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        ChessGame newGame = new ChessGame();
        //change chess game to json object
        var gameJson = new Gson().toJson(newGame);
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, whiteUsername);
                ps.setString(2, blackUsername);
                ps.setString(3, gameName);
                ps.setString(4, gameJson);
                ps.executeUpdate();
                //return game id
                var resultSet = ps.getGeneratedKeys();
                var ID = 0;
                if (resultSet.next()) {
                    ID = resultSet.getInt(1);
                }
                return ID;
            }
        } catch (Exception e){
            throw new DataAccessException("Unable to read data");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
        return null;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String game = rs.getString("game");
        ChessGame gotGame = new Gson().fromJson(game, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, gotGame);
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
            throw new DataAccessException("Unable to read data");
        }
        return result;
    }

    private String checkUpdate(ChessGame.TeamColor color, Connection conn, ResultSet rs, String username, int id) throws SQLException {
        if (color == ChessGame.TeamColor.WHITE) {
            String user = rs.getString("whiteUsername");
            if (user == null) {
                var updateStatement = "UPDATE game SET whiteUsername = ? WHERE gameID=?";
                var ts = conn.prepareStatement(updateStatement);
                ts.setString(1, username);
                ts.setInt(2, id);
                ts.executeUpdate();
                return "{}";
            } else if (rs.getString("whiteUsername").equals(username)) {
                return "{}";
            } else {
                return "taken";
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {
            if (rs.getString("blackUsername") == null) {
                var updateStatement = "UPDATE game SET blackUsername = ? WHERE gameID=?";
                var ts = conn.prepareStatement(updateStatement);
                ts.setString(1, username);
                ts.setInt(2, id);
                ts.executeUpdate();
                return "{}";
            } else if (rs.getString("blackUsername").equals(username)) {
                return "{}";
            } else {
                return "taken";
            }
        }
        return null;
    }

    @Override
    public String updateGame(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            int id = gameData.getGameID();
            try (var ps = conn.prepareStatement(statement)){
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return checkUpdate(color, conn, rs, username, id);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
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
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int ID = rs.getInt("gameID");
                        result.put(ID, readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
        return result;
    }

    @Override
    public GameData getGameByName(String name) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameName=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, name);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` JSON NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX idx_whiteUsername (whiteUsername),
              INDEX idx_blackUsername (blackUsername),
              INDEX idx_gameName (gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        ConfigureDatabase conData = new ConfigureDatabase();
        conData.configureDatabase(createStatements);
    }
}
