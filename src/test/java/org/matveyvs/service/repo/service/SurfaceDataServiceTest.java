package org.matveyvs.service.repo.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.dao.repository.SurfaceDataRepository;
import org.matveyvs.dao.repository.WellDataRepository;
import org.matveyvs.dto.repo.SurfaceDataCreateDto;
import org.matveyvs.dto.repo.SurfaceDataReadDto;
import org.matveyvs.dto.repo.WellDataCreateDto;
import org.matveyvs.dto.repo.WellDataReadDto;
import org.matveyvs.mapper.repo.SurfaceDataMapperImpl;
import org.matveyvs.mapper.repo.WellDataMapperImpl;
import org.matveyvs.utils.HibernateUtil;

import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SurfaceDataServiceTest {
    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private SurfaceDataService surfaceDataService;
    private WellDataService wellDataService;
    private static WellDataReadDto wellDataReadDto;
    private Session session;

    @BeforeEach
    void setUp() {
        TestDatabaseUtil.createRandomData();

        session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, arg1) -> method.invoke(sessionFactory.getCurrentSession(), arg1));
        session = sessionFactory.openSession();
        session.beginTransaction();
        wellDataService = new WellDataService(new WellDataRepository(session), new WellDataMapperImpl());
        Integer integer = wellDataService.create(new WellDataCreateDto(
                "Test",
                "Test",
                "Test",
                "Test"));
        wellDataReadDto = wellDataService.findById(integer).get();

        surfaceDataService = new SurfaceDataService(new SurfaceDataRepository(session),
                new SurfaceDataMapperImpl());
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().commit();

        //remove all tables
        TestDatabaseUtil.dropListOfTables();
    }

    private static SurfaceDataCreateDto getObject() {
        return new SurfaceDataCreateDto(
                Timestamp.valueOf(LocalDateTime.now()),
                1.1,
                2.2,
                3.3,
                4.4,
                5.5,
                6.6,
                7.7,
                wellDataReadDto
        );
    }
    private static SurfaceDataReadDto getUpdatedObject() {
        return new SurfaceDataReadDto(1,
                Timestamp.valueOf(LocalDateTime.now()),
                2.2,
                3.3,
                3.3,
                4.4,
                5.5,
                6.6,
                7.7,
                wellDataReadDto
        );
    }
    @Test
    void create() {
        SurfaceDataCreateDto object = getObject();
        Integer byId = surfaceDataService.create(object);
        Optional<SurfaceDataReadDto> saved = surfaceDataService.findById(byId);
        assertTrue(saved.isPresent());
        assertEquals(object.measuredDepth(), saved.get().measuredDepth());
    }

    @Test
    void update() {
        Optional<SurfaceDataReadDto> saved = surfaceDataService.findById(1);
        SurfaceDataReadDto objectToUpdate = getUpdatedObject();
        boolean update = surfaceDataService.update(objectToUpdate);
        assertTrue(update);
        Optional<SurfaceDataReadDto> updated = surfaceDataService.findById(1);
        assertNotEquals(saved.get().measuredDate(), updated.get().measuredDate());
        assertNotEquals(saved.get().holeDepth(), updated.get().holeDepth());
    }

    @Test
    void findById() {
        SurfaceDataCreateDto object = getObject();
        Integer id = surfaceDataService.create(object);
        Optional<SurfaceDataReadDto> byId = surfaceDataService.findById(id);
        assertEquals(object.measuredDepth(), byId.get().measuredDepth());
    }

    @Test
    void findAll() {
        SurfaceDataCreateDto object = getObject();
        Integer id = surfaceDataService.create(object);
        List<SurfaceDataReadDto> all = surfaceDataService.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void delete() {
        SurfaceDataCreateDto object = getObject();
        Integer id = surfaceDataService.create(object);
        boolean delete = surfaceDataService.delete(id);
        assertTrue(delete);
        Optional<SurfaceDataReadDto> byId = surfaceDataService.findById(id);
        assertTrue(byId.isEmpty());
    }

    @Test
    void findAllByWellId() {
        SurfaceDataCreateDto object = getObject();
        Integer id = surfaceDataService.create(object);
        List<SurfaceDataReadDto> allByWellId = surfaceDataService.findAllByWellId(wellDataReadDto.id());
        assertFalse(allByWellId.isEmpty());
    }
}