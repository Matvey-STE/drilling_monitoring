package org.matveyvs.dao;

import org.matveyvs.entity.DownholeData;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DownholeDataDao implements Dao<Long, DownholeData> {
    private static final DownholeDataDao INSTANCE = new DownholeDataDao();
    private static final DirectionalDao directionalDao = DirectionalDao.getInstance();
    private static final GammaDao gammaDao = GammaDao.getInstance();

    private static final String SAVE_SQL = """
            INSERT INTO downhole_data
            (measure_date, directional_id, gamma_id) 
            VALUES (?,?,?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
            id, measure_date, directional_id, gamma_id
            FROM downhole_data;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id, measure_date, directional_id, gamma_id
            FROM  downhole_data WHERE id = ?;
            """;
    private static final String UPDATE_FLIGHT_BY_ID = """
            UPDATE downhole_data
            SET  measure_date = ?, directional_id = ?, gamma_id = ?
            WHERE id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM downhole_data
            WHERE id = ?
            """;

    @Override
    public DownholeData save(DownholeData downholeData) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setDirectionalIntoStatement(downholeData, statement);
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            Long id = null;
            if (keys.next()) {
                id = keys.getLong("id");
            }
            return new DownholeData(id, downholeData.measuredDate(), downholeData.directional(),
                    downholeData.gamma());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<DownholeData> findAll() {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<DownholeData> list = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                list.add(buildDownholeData(result));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<DownholeData> findById(Long id) {
        try (var connection = ConnectionManager.open()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<DownholeData> findById(Long id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            DownholeData downholeData = null;
            if (result.next()) {
                downholeData = buildDownholeData(result);
            }
            return Optional.ofNullable(downholeData);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(DownholeData downholeData) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(UPDATE_FLIGHT_BY_ID)) {
            setDirectionalIntoStatement(downholeData, statement);
            statement.setDouble(4, downholeData.id());
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

    private DownholeData buildDownholeData(ResultSet result) throws SQLException {
        return new DownholeData(result.getLong("id"),
                result.getTimestamp("measure_date"),
                directionalDao.findById(result.getLong("directional_id"),
                                result.getStatement().getConnection())
                        .orElse(null),
                gammaDao.findById(result.getLong("gamma_id"),
                                result.getStatement().getConnection())
                        .orElse(null));
    }

    private static void setDirectionalIntoStatement(DownholeData downholeData, PreparedStatement statement) throws SQLException {
        statement.setTimestamp(1, downholeData.measuredDate());
        statement.setLong(2, downholeData.directional().id());
        statement.setLong(3, downholeData.gamma().id());
    }

    private DownholeDataDao() {

    }

    public static DownholeDataDao getInstance() {
        return DownholeDataDao.INSTANCE;
    }
}
