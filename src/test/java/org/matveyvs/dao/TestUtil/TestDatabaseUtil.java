package org.matveyvs.dao.TestUtil;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.RandomWellDataBaseCreator;
import org.hibernate.cfg.Configuration;

@UtilityClass
@Slf4j
public class TestDatabaseUtil {
    private final RandomWellDataBaseCreator randomCreator = RandomWellDataBaseCreator.getInstance();
    private static final String DROP_TABLES = "DROP TABLE ";

    public void createRandomData() {
        randomCreator.createRandomDataForTests();
    }

    public void dropListOfTables() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml"); // Load Hibernate configuration from XML file
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (TableNames tableToDelete : TableNames.values()) {
                var nativeQuery = session
                        .createNativeQuery(DROP_TABLES + tableToDelete.toString().toLowerCase());
                nativeQuery.executeUpdate();
            }
            session.getTransaction().commit();
        } catch (DaoException e) {
            log.error("Error drop list of tables: " + e);
        }
    }
}
