package org.matveyvs.service.repo.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.dao.repository.DirectionalRepository;
import org.matveyvs.dao.repository.DownholeDataRepository;
import org.matveyvs.dao.repository.WellDataRepository;
import org.matveyvs.dto.repo.*;
import org.matveyvs.mapper.repo.DirectionalMapperImpl;
import org.matveyvs.mapper.repo.DownholeDataMapperImpl;
import org.matveyvs.mapper.repo.WellDataMapperImpl;
import org.matveyvs.utils.HibernateUtil;

import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DirectionalServiceTest {

    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private DirectionalService directionalService;
    private DownholeDataService downholeDataService;
    private WellDataService wellDataService;
    private static WellDataReadDto wellDataReadDto;
    private static DownholeDataReadDto downholeDataReadDto;

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

        downholeDataService = new DownholeDataService(new DownholeDataRepository(session),
                new DownholeDataMapperImpl());
        Integer downholeId = downholeDataService.create(
                new DownholeDataCreateDto(wellDataReadDto));
        downholeDataReadDto = downholeDataService.findById(downholeId).get();

        directionalService = new DirectionalService(new DirectionalRepository(session),
                new DirectionalMapperImpl());
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().commit();

        //remove all tables
        TestDatabaseUtil.dropListOfTables();
    }

    private static DirectionalCreateDto getObject() {
        return new DirectionalCreateDto(
                Timestamp.valueOf(LocalDateTime.now()),
                2.2,
                3.3, 1.1, 2.2,
                3.3, 1.1, 2.2, 3.3,
                3.3, 1.1, 2.2, 3.3,
                downholeDataReadDto
        );
    }

    private static DirectionalReadDto getUpdatedObject() {
        return new DirectionalReadDto(1,
                Timestamp.valueOf(LocalDateTime.now()),
                2.2,
                3.3, 1.1, 2.2,
                3.3, 1.1, 2.2, 3.3,
                3.3, 1.1, 2.2, 3.3,
                downholeDataReadDto
        );
    }

    @Test
    void create() {
        DirectionalCreateDto object = getObject();
        Integer byId = directionalService.create(object);
        Optional<DirectionalReadDto> saved = directionalService.findById(byId);
        assertTrue(saved.isPresent());
        assertEquals(object.measureDate(), saved.get().measureDate());
    }

    @Test
    void update() {
        Optional<DirectionalReadDto> saved = directionalService.findById(1);
        DirectionalReadDto objectToUpdate = getUpdatedObject();
        boolean update = directionalService.update(objectToUpdate);
        assertTrue(update);
        Optional<DirectionalReadDto> updated = directionalService.findById(1);
        assertNotEquals(saved.get().measuredDepth(), updated.get().measuredDepth());

    }

    @Test
    void findById() {
        DirectionalCreateDto object = getObject();
        Integer id = directionalService.create(object);
        Optional<DirectionalReadDto> byId = directionalService.findById(id);
        assertEquals(object.azCorr(), byId.get().azCorr());
    }

    @Test
    void findAll() {
        DirectionalCreateDto object = getObject();
        directionalService.create(object);
        List<DirectionalReadDto> all = directionalService.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void delete() {
        DirectionalCreateDto object = getObject();
        Integer id = directionalService.create(object);
        boolean delete = directionalService.delete(id);
        assertTrue(delete);
        Optional<DirectionalReadDto> byId = directionalService.findById(id);
        assertTrue(byId.isEmpty());
    }

    @Test
    void findAllByDownholeId() {
        DirectionalCreateDto object = getObject();
        directionalService.create(object);
        List<DirectionalReadDto> allByWellId = directionalService
                .findAllByDownholeId(downholeDataReadDto.id());
        assertFalse(allByWellId.isEmpty());
    }
}