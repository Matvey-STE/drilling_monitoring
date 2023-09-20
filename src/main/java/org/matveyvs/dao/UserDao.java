package org.matveyvs.dao;

import org.matveyvs.entity.Status;
import org.matveyvs.entity.User;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements Dao<Long, User> {
    private static final UserDao INSTANCE = new UserDao();
    private static final String SAVE_SQL = """
            INSERT INTO users
            (username, email, password, status, created_at, last_login_at, first_name, last_name) 
            VALUES (?,?,?,?,?,?,?,?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
            user_id, username, email, password, status, created_at, last_login_at, first_name, last_name
            FROM users;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT user_id, username, email, password, status, created_at, last_login_at, first_name, last_name
            FROM  users WHERE user_id = ?;
            """;
    private static final String UPDATE_FLIGHT_BY_ID = """
            UPDATE users
            SET   username = ?, email = ?, password = ?,
            status = ?, created_at = ?, last_login_at = ?,
            first_name = ?, last_name = ?
            WHERE user_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE user_id = ?
            """;

    @Override
    public User save(User user) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setUserIntoStatement(user, statement);
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            Long id = null;
            if (keys.next()) {
                id = keys.getLong("user_id");
            }
            return new User(id, user.userName(), user.email(),
                    user.password(), user.status(), user.createdAt(),
                    user.lastLoginAt(), user.firstName(), user.lastName());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<User> list = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                list.add(buildUser(result));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            User user = null;
            if (result.next()) {
                user = buildUser(result);
            }
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(User user) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(UPDATE_FLIGHT_BY_ID)) {
            setUserIntoStatement(user, statement);
            statement.setDouble(9, user.id());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.open();
             var statement =
                     connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private User buildUser(ResultSet result) throws SQLException {
        return new User(result.getLong("user_id"),
                result.getString("username"),
                result.getString("email"),
                result.getString("password"),
                Status.valueOf(result.getString("status")),
                result.getTimestamp("created_at"),
                result.getTimestamp("last_login_at"),
                result.getString("first_name"),
                result.getString("last_name"));
    }

    private static void setUserIntoStatement(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.userName());
        statement.setString(2, user.email());
        statement.setString(3, user.password());
        statement.setString(4, String.valueOf(user.status()));
        statement.setTimestamp(5, user.createdAt());
        statement.setTimestamp(6, user.lastLoginAt());
        statement.setString(7, user.firstName());
        statement.setString(8, user.lastName());
    }

    private UserDao() {

    }

    public static UserDao getInstance() {
        return UserDao.INSTANCE;
    }
}
