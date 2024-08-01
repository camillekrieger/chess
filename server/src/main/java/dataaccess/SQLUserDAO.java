package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()){
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Error creating user");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT username, password, email FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: %s");
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException, DataAccessException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    public void clear() throws DataAccessException, SQLException {
        var statement = "TRUNCATE user";
        var conn = DatabaseManager.getConnection();
        var ps = conn.prepareStatement(statement);
        ps.executeUpdate();
    }

    @Override
    public HashMap<String, UserData> getUsers() throws DataAccessException {
        HashMap<String, UserData> results = new HashMap<>();
        String query = "SELECT username, password, email FROM user";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                UserData user = readUser(rs);
                results.put(rs.getString("username"), user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX idx_password (`password`),
              INDEX idx_email (`email`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    public HashMap<String, List> getTable() throws DataAccessException {
        HashMap<String, List> results = new HashMap<>();
        String query = "SELECT username, password, email FROM user";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                List<String> info = new ArrayList<>();
                info.add(rs.getString("username"));
                info.add(rs.getString("password"));
                info.add(rs.getString("email"));
                results.put(rs.getString("username"), info);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    private void configureDatabase() throws DataAccessException {
        ConfigureDatabase conData = new ConfigureDatabase();
        conData.configureDatabase(createStatements);
    }
}
