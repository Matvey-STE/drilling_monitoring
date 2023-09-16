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

    private static final String SAVE_SQL = """
            INSERT INTO directional
            (mdepth,gx,gy,gz,bx,by,bz,inc,az_true,az_mag,az_corr,toolface_corr) 
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
            id, mdepth, gx, gy, gz, bx, by, bz, inc, az_true, az_mag, az_corr, toolface_corr
            FROM directional;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id, mdepth, gx, gy, gz, bx, by, bz, inc, az_true, az_mag, az_corr, toolface_corr
            FROM  directional WHERE id = ?;
            """;
    private static final String UPDATE_FLIGHT_BY_ID = """
            UPDATE directional
            SET  mdepth = ?,
                 gx = ?, gy = ?, gz = ?, bx = ?, by = ?, bz = ?, inc = ?,
                 az_true = ?, az_mag = ?, az_corr = ?, toolface_corr = ?
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
            return new Directional(id, directional.measuredDepth(), directional.gx(),
                    directional.gy(), directional.gz(), directional.bx(),
                    directional.by(), directional.bz(), directional.inc(),
                    directional.azTrue(), directional.azMag(), directional.azCorr(),
                    directional.toolfaceCorr());
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
            statement.setDouble(13, directional.id());
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
                result.getDouble("toolface_corr"));
    }

    private static void setDirectionalIntoStatement(Directional directional, PreparedStatement statement) throws SQLException {
        statement.setDouble(1, directional.measuredDepth());
        statement.setDouble(2, directional.gx());
        statement.setDouble(3, directional.gy());
        statement.setDouble(4, directional.gz());
        statement.setDouble(5, directional.bx());
        statement.setDouble(6, directional.by());
        statement.setDouble(7, directional.bz());
        statement.setDouble(8, directional.inc());
        statement.setDouble(9, directional.azTrue());
        statement.setDouble(10, directional.azMag());
        statement.setDouble(11, directional.azCorr());
        statement.setDouble(12, directional.toolfaceCorr());
    }

    private DirectionalDao() {

    }

    public static DirectionalDao getInstance() {
        return DirectionalDao.INSTANCE;
    }
}
