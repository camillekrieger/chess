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
    public int createGame(String gameName, String whiteUsername, String blackUsername) throws DataAccessException {
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        ChessGame newGame = new ChessGame();
        GameData game = new GameData(nextGameID, whiteUsername, blackUsername, gameName, newGame);
        var json = new Gson().toJson(game);
        executeUpdate(statement, game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName(), game.getGame(), json);
        int temp = nextGameID;
        nextGameID++;
        return temp;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String u) {
                        ps.setString(i + 1, u);
                    }
                    else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to update database: %s, %s");
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
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
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
              `whiteUsername` varchar(256) NOT NULL,
              'blackUsername' varchar(256) NOT NULL,
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
