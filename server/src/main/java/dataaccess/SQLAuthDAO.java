package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String newAuth = java.util.UUID.randomUUID().toString();
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        AuthData createdAuth = new AuthData(username, newAuth);
        var json = new Gson().toJson(createdAuth);
        executeUpdate(statement, createdAuth.getAuthToken(), createdAuth.getUsername(), json);
        return newAuth;
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
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: %s");
        }
        return null;
    }

    private AuthData readAuth(ResultSet rs) throws SQLException, DataAccessException {
        var authToken = rs.getString("authToken");
        return getAuth(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    @Override
    public HashMap<String, AuthData> getAuths() throws DataAccessException {
        var result = new HashMap<String, AuthData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String authToken = "SELECT authToken FROM rs";
                        result.put(authToken, readAuth(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: %s");
        }
        return result;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              'username' varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
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
