package org.matveyvs.dao;

import org.matveyvs.entity.SurfaceData;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SurfaceDataDao implements Dao<Long, SurfaceData> {
    private static final SurfaceDataDao INSTANCE = new SurfaceDataDao();

    private static final String SAVE_SQL = """
            INSERT INTO surface_data
            (measure_date, mdepth, hole_depth, tvdepth, hookload, wob, block_pos, standpipe_pr) 
            VALUES (?,?,?,?,?,?,?,?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
            id, measure_date, mdepth, hole_depth, tvdepth, hookload, wob, block_pos, standpipe_pr
            FROM surface_data;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id, measure_date, mdepth, hole_depth, tvdepth, hookload, wob, block_pos, standpipe_pr
            FROM  surface_data WHERE id = ?;
            """;
    private static final String UPDATE_FLIGHT_BY_ID = """
            UPDATE surface_data
            SET measure_date = ?, mdepth = ?, hole_depth = ?, tvdepth = ?, hookload = ?, 
                 wob = ?, block_pos = ?, standpipe_pr = ?
            WHERE id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM surface_data
            WHERE id = ?
            """;


    @Override
    public SurfaceData save(SurfaceData surfaceData) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setSurfaceDataIntoStatement(surfaceData, statement);
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            Long id = null;
            if (keys.next()) {
                id = keys.getLong("id");
            }
            return new SurfaceData(id, surfaceData.measuredDate(), surfaceData.measuredDepth(),
                    surfaceData.holeDepth(), surfaceData.tvDepth(), surfaceData.hookload(),
                    surfaceData.wob(), surfaceData.blockPos(), surfaceData.standpipePressure());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<SurfaceData> findAll() {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<SurfaceData> list = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                list.add(buildSurfaceData(result));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<SurfaceData> findById(Long id) {
        try (var connection = ConnectionManager.open()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<SurfaceData> findById(Long id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            SurfaceData directional = null;
            if (result.next()) {
                directional = buildSurfaceData(result);
            }
            return Optional.ofNullable(directional);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(SurfaceData surfaceData) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(UPDATE_FLIGHT_BY_ID)) {
            setSurfaceDataIntoStatement(surfaceData, statement);
            statement.setDouble(9, surfaceData.id());
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

    private SurfaceData buildSurfaceData(ResultSet result) throws SQLException {
        return new SurfaceData(result.getLong("id"),
                result.getTimestamp("measure_date"),
                result.getDouble("mdepth"),
                result.getDouble("hole_depth"),
                result.getDouble("tvdepth"),
                result.getDouble("hookload"),
                result.getDouble("wob"),
                result.getDouble("block_pos"),
                result.getDouble("standpipe_pr"));
    }

    private static void setSurfaceDataIntoStatement(SurfaceData surfaceData, PreparedStatement statement) throws SQLException {
        statement.setTimestamp(1, surfaceData.measuredDate());
        statement.setDouble(2, surfaceData.measuredDepth());
        statement.setDouble(3, surfaceData.holeDepth());
        statement.setDouble(4, surfaceData.tvDepth());
        statement.setDouble(5, surfaceData.hookload());
        statement.setDouble(6, surfaceData.wob());
        statement.setDouble(7, surfaceData.blockPos());
        statement.setDouble(8, surfaceData.standpipePressure());
    }

    private SurfaceDataDao() {

    }

    public static SurfaceDataDao getInstance() {
        return SurfaceDataDao.INSTANCE;
    }
}
