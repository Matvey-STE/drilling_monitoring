package org.matveyvs.dao;

import org.matveyvs.entity.WellData;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WellDataDao implements Dao<Long, WellData> {
    private static final WellDataDao INSTANCE = new WellDataDao();

    private static final String SAVE_SQL = """
            INSERT INTO well_data
            (company_name, field_name, well_cluster, well) 
            VALUES (?,?,?,?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT 
            id, company_name, field_name, well_cluster, well
            FROM well_data;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id, company_name, field_name, well_cluster, well
            FROM  well_data WHERE id = ?;
            """;
    private static final String UPDATE_FLIGHT_BY_ID = """
            UPDATE well_data
            SET   company_name = ?, field_name = ?, well_cluster = ?,
            well = ?
            WHERE id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM well_data
            WHERE id = ?
            """;

    @Override
    public WellData save(WellData wellData) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setDirectionalIntoStatement(wellData, statement);
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            Long id = null;
            if (keys.next()) {
                id = keys.getLong("id");
            }
            return new WellData(id, wellData.companyName(), wellData.fieldName(),
                    wellData.wellCluster(), wellData.well());
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<WellData> findAll() {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<WellData> list = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next()) {
                list.add(buildWellData(result));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<WellData> findById(Long id) {
        try (var connection = ConnectionManager.open()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<WellData> findById(Long id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            var result = statement.executeQuery();
            WellData wellData = null;
            if (result.next()) {
                wellData = buildWellData(result);
            }
            return Optional.ofNullable(wellData);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(WellData wellData) {
        try (var connection = ConnectionManager.open();
             var statement = connection.prepareStatement(UPDATE_FLIGHT_BY_ID)) {
            setDirectionalIntoStatement(wellData, statement);
            statement.setDouble(5, wellData.id());
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

    private WellData buildWellData(ResultSet result) throws SQLException {
        return new WellData(result.getLong("id"),
                result.getString("company_name"),
                result.getString("field_name"),
                result.getString("well_cluster"),
                result.getString("well"));
    }

    private static void setDirectionalIntoStatement(WellData wellData, PreparedStatement statement) throws SQLException {
        statement.setString(1, wellData.companyName());
        statement.setString(2, wellData.fieldName());
        statement.setString(3, wellData.wellCluster());
        statement.setString(4, wellData.well());
    }

    private WellDataDao() {

    }

    public static WellDataDao getInstance() {
        return WellDataDao.INSTANCE;
    }
}
