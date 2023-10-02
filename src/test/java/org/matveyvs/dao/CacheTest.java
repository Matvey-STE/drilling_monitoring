package org.matveyvs.dao;

import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.entity.*;
import org.matveyvs.utils.HibernateUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheTest {

    @BeforeEach
    void setUp() {
        //create random data
        TestDatabaseUtil.createRandomData();
    }

    @AfterEach
    void tearDown() {
        //remove all tables
        TestDatabaseUtil.dropListOfTables();
    }

    @Test
    void secondLevelUserCacheTest() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        User entity;
        Class<User> aClass = User.class;
        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            session1.find(aClass, 1);
            entity = session1.find(aClass, 1);
            session1.getTransaction().commit();
        }
        try (Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();
            var entity2 = session2.find(aClass, 1);
            session2.getTransaction().commit();
            assertEquals(entity, entity2);
        }
        long secondLevelCacheHitCount = sessionFactory.getStatistics()
                .getSecondLevelCacheHitCount();
        assertTrue(secondLevelCacheHitCount > 0);
        System.out.println(sessionFactory.getMetamodel().entity(aClass));

    }

    @Test
    void secondLevelWellDataCacheTest() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        WellData entity;
        Class<WellData> aClass = WellData.class;
        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            session1.find(aClass, 1);
            entity = session1.find(aClass, 1);
            session1.getTransaction().commit();
        }
        try (Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();
            var entity2 = session2.find(aClass, 1);
            session2.getTransaction().commit();
            assertEquals(entity, entity2);
        }
        long secondLevelCacheHitCount = sessionFactory.getStatistics()
                .getSecondLevelCacheHitCount();
        assertTrue(secondLevelCacheHitCount > 0);
    }

    @Test
    void secondLevelSurfaceDataCacheTest() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        SurfaceData entity;
        Class<SurfaceData> aClass = SurfaceData.class;
        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            session1.find(aClass, 1);
            entity = session1.find(aClass, 1);
            session1.getTransaction().commit();
        }
        try (Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();
            var entity2 = session2.find(aClass, 1);
            session2.getTransaction().commit();
            assertEquals(entity, entity2);
        }
        long secondLevelCacheHitCount = sessionFactory.getStatistics()
                .getSecondLevelCacheHitCount();
        assertTrue(secondLevelCacheHitCount > 0);
    }

    @Test
    void secondLevelDownholeCacheTest() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        DownholeData entity;
        Class<DownholeData> aClass = DownholeData.class;
        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            session1.find(aClass, 1);
            entity = session1.find(aClass, 1);
            session1.getTransaction().commit();
        }
        try (Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();
            var entity2 = session2.find(aClass, 1);
            session2.getTransaction().commit();
            assertEquals(entity, entity2);
        }
        long secondLevelCacheHitCount = sessionFactory.getStatistics()
                .getSecondLevelCacheHitCount();
        assertTrue(secondLevelCacheHitCount > 0);
    }

    @Test
    void secondLevelDirectionalCacheTest() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        Directional entity;
        Class<Directional> aClass = Directional.class;
        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            session1.find(aClass, 1);
            entity = session1.find(aClass, 1);
            session1.getTransaction().commit();
        }
        try (Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();
            var entity2 = session2.find(aClass, 1);
            session2.getTransaction().commit();
            assertEquals(entity, entity2);
        }
        long secondLevelCacheHitCount = sessionFactory.getStatistics()
                .getSecondLevelCacheHitCount();
        assertTrue(secondLevelCacheHitCount > 0);
    }

    @Test
    void secondLevelGammaCacheTest() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        Gamma entity;
        Class<Gamma> aClass = Gamma.class;
        try (Session session1 = sessionFactory.openSession()) {
            session1.beginTransaction();
            session1.find(aClass, 1);
            entity = session1.find(aClass, 1);
            session1.getTransaction().commit();
        }
        try (Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();
            var entity2 = session2.find(aClass, 1);
            session2.getTransaction().commit();
            assertEquals(entity, entity2);
        }
        long secondLevelCacheHitCount = sessionFactory.getStatistics()
                .getSecondLevelCacheHitCount();
        assertTrue(secondLevelCacheHitCount > 0);
    }

}
