package org.matveyvs.dao.TestUtil;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.matveyvs.exception.DaoException;
import org.matveyvs.utils.HibernateUtil;
import org.matveyvs.utils.RandomWellDataBaseCreator;

@UtilityClass
@Slf4j
public class TestDatabaseUtil {
    private final SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private final RandomWellDataBaseCreator randomCreator = RandomWellDataBaseCreator.getInstance();
    private static final String DROP_TABLES = "DROP TABLE ";

    public void createRandomData() {
        randomCreator.createRandomDataForTests();
    }

    public void dropListOfTables() {
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
