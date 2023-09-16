package org.matveyvs.dao;

import org.matveyvs.entity.Gamma;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GammaDao implements Dao<Long, Gamma>{
    private static final GammaDao INSTANCE = new GammaDao();

    private static final String SAVE_SQL = """
            INSERT INTO gamma
            (mdepth,grcx) 
            VALUES (?,?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
            id, mdepth, grcx
            FROM gamma;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id, mdepth, grcx
            FROM  gamma WHERE id = ?;
            """;
    private static final String UPDATE_FLIGHT_BY_ID = """
            UPDATE gamma
            SET  mdepth = ?,
                 grcx = ?
            WHERE id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM gamma
            WHERE id = ?
            """;


    @Override
    public Gamma save(Gamma gamma) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setGammaIntoStatement(gamma, statement);
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            Long id = null;
            if (keys.next()) {
                id = keys.getLong("id");
            }
            return new Gamma(id, gamma.measuredDepth(), gamma.grcx());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Gamma> findAll() {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Gamma> list = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                list.add(buildGamma(result));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Gamma> findById(Long id) {
        try (var connection = ConnectionManager.open()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<Gamma> findById(Long id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            Gamma gamma = null;
            if (result.next()) {
                gamma = buildGamma(result);
            }
            return Optional.ofNullable(gamma);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Gamma gamma) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(UPDATE_FLIGHT_BY_ID)) {
            setGammaIntoStatement(gamma, statement);
            statement.setDouble(3, gamma.id());
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
    private Gamma buildGamma(ResultSet result) throws SQLException {
        return new Gamma(result.getLong("id"),
                result.getDouble("mdepth"),
                result.getDouble("grcx"));
    }

    private static void setGammaIntoStatement(Gamma gamma, PreparedStatement statement) throws SQLException {
        statement.setDouble(1, gamma.measuredDepth());
        statement.setDouble(2, gamma.grcx());
    }
    private GammaDao() {

    }
    public static GammaDao getInstance() {
        return GammaDao.INSTANCE;
    }
}
