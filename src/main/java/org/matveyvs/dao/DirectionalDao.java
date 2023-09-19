package org.matveyvs.dao;

import org.matveyvs.entity.Directional;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectionalDao implements Dao<Long, Directional> {
    private static final DirectionalDao INSTANCE = new DirectionalDao();
    private static final DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();

    private static final String SAVE_SQL = """
            INSERT INTO directional
            (measure_date, mdepth, gx, gy, gz, bx, by, bz, 
            inc, az_true, az_mag, az_corr, toolface_corr, downhole_id) 
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
            id, measure_date, mdepth, gx, gy, gz, bx, by, bz, 
            inc, az_true, az_mag, az_corr, toolface_corr, downhole_id
            FROM directional;
            """;
    private static final String FIND_ALL_BY_DOWNHOLE_ID = """
            SELECT 
            id, measure_date, mdepth, gx, gy, gz, bx, by, bz, 
            inc, az_true, az_mag, az_corr, toolface_corr, downhole_id
            FROM directional WHERE downhole_id = ?;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id, measure_date, mdepth, gx, gy, gz, bx, by, bz,
             inc, az_true, az_mag, az_corr, toolface_corr, downhole_id
            FROM  directional WHERE id = ?;
            """;
    private static final String UPDATE_FLIGHT_BY_ID = """
            UPDATE directional
            SET  measure_date = ?, mdepth = ?,
                 gx = ?, gy = ?, gz = ?, bx = ?, by = ?, bz = ?, inc = ?,
                 az_true = ?, az_mag = ?, az_corr = ?, toolface_corr = ?, downhole_id = ?
            WHERE id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM directional
            WHERE id = ?
            """;

    @Override
    public Directional save(Directional directional) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setDirectionalIntoStatement(directional, statement);
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            Long id = null;
            if (keys.next()) {
                id = keys.getLong("id");
            }
            return new Directional(id, directional.measureDate(), directional.measuredDepth(), directional.gx(),
                    directional.gy(), directional.gz(), directional.bx(),
                    directional.by(), directional.bz(), directional.inc(),
                    directional.azTrue(), directional.azMag(), directional.azCorr(),
                    directional.toolfaceCorr(), directional.downholeData());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Directional> findAll() {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Directional> list = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                list.add(buildDirectional(result));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Directional> findAllByDownholeId(Long downholeId) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(FIND_ALL_BY_DOWNHOLE_ID)) {
            statement.setLong(1, downholeId);
            List<Directional> list = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                list.add(buildDirectional(result));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Directional> findById(Long id) {
        try (var connection = ConnectionManager.open()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }


    public Optional<Directional> findById(Long id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            Directional directional = null;
            if (result.next()) {
                directional = buildDirectional(result);
            }
            return Optional.ofNullable(directional);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Directional directional) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(UPDATE_FLIGHT_BY_ID)) {
            setDirectionalIntoStatement(directional, statement);
            statement.setDouble(15, directional.id());
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

    private Directional buildDirectional(ResultSet result) throws SQLException {
        return new Directional(result.getLong("id"),
                result.getTimestamp("measure_date"),
                result.getDouble("mdepth"),
                result.getDouble("gx"),
                result.getDouble("gy"),
                result.getDouble("gz"),
                result.getDouble("bx"),
                result.getDouble("by"),
                result.getDouble("bz"),
                result.getDouble("inc"),
                result.getDouble("az_true"),
                result.getDouble("az_mag"),
                result.getDouble("az_corr"),
                result.getDouble("toolface_corr"),
                downholeDataDao.findById(result.getLong("downhole_id"),
                                result.getStatement().getConnection())
                        .orElse(null));
    }

    private static void setDirectionalIntoStatement(Directional directional, PreparedStatement statement) throws SQLException {
        statement.setTimestamp(1, directional.measureDate());
        statement.setDouble(2, directional.measuredDepth());
        statement.setDouble(3, directional.gx());
        statement.setDouble(4, directional.gy());
        statement.setDouble(5, directional.gz());
        statement.setDouble(6, directional.bx());
        statement.setDouble(7, directional.by());
        statement.setDouble(8, directional.bz());
        statement.setDouble(9, directional.inc());
        statement.setDouble(10, directional.azTrue());
        statement.setDouble(11, directional.azMag());
        statement.setDouble(12, directional.azCorr());
        statement.setDouble(13, directional.toolfaceCorr());
        statement.setDouble(14, directional.downholeData().id());

    }

    private DirectionalDao() {

    }

    public static DirectionalDao getInstance() {
        return DirectionalDao.INSTANCE;
    }
}
