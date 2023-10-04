package org.matveyvs.service.repo.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.dao.repository.DownholeDataRepository;
import org.matveyvs.dao.repository.WellDataRepository;
import org.matveyvs.dto.repo.*;
import org.matveyvs.mapper.repo.DownholeDataMapperImpl;
import org.matveyvs.mapper.repo.WellDataMapperImpl;
import org.matveyvs.utils.HibernateUtil;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DownholeDataServiceTest {
    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private DownholeDataService downholeDataService;
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

        downholeDataService = new DownholeDataService(new DownholeDataRepository(session),
                new DownholeDataMapperImpl());
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().commit();

        //remove all tables
        TestDatabaseUtil.dropListOfTables();
    }

    private static DownholeDataCreateDto getObject() {
        return new DownholeDataCreateDto(
                wellDataReadDto
        );
    }

    private static DownholeDataReadDto getUpdatedObject() {
        return new DownholeDataReadDto(1,
                wellDataReadDto
        );
    }

    @Test
    void create() {
        DownholeDataCreateDto object = getObject();
        Integer byId = downholeDataService.create(object);
        Optional<DownholeDataReadDto> saved = downholeDataService.findById(byId);
        assertTrue(saved.isPresent());
        assertEquals(object.wellDataReadDto(), saved.get().wellDataReadDto());
    }

    @Test
    void update() {
        Optional<DownholeDataReadDto> saved = downholeDataService.findById(1);
        DownholeDataReadDto objectToUpdate = getUpdatedObject();
        boolean update = downholeDataService.update(objectToUpdate);
        assertTrue(update);
        Optional<DownholeDataReadDto> updated = downholeDataService.findById(1);
        assertNotEquals(saved.get().wellDataReadDto(), updated.get().wellDataReadDto());

    }

    @Test
    void findById() {
        DownholeDataCreateDto object = getObject();
        Integer id = downholeDataService.create(object);
        Optional<DownholeDataReadDto> byId = downholeDataService.findById(id);
        assertEquals(object.wellDataReadDto(), byId.get().wellDataReadDto());
    }

    @Test
    void findAll() {
        DownholeDataCreateDto object = getObject();
        downholeDataService.create(object);
        List<DownholeDataReadDto> all = downholeDataService.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void delete() {
        DownholeDataCreateDto object = getObject();
        Integer id = downholeDataService.create(object);
        boolean delete = downholeDataService.delete(id);
        assertTrue(delete);
        Optional<DownholeDataReadDto> byId = downholeDataService.findById(id);
        assertTrue(byId.isEmpty());
    }

    @Test
    void findAllByWellId() {
        DownholeDataCreateDto object = getObject();
        downholeDataService.create(object);
        List<DownholeDataReadDto> allByWellId = downholeDataService.findAllByWellId(wellDataReadDto.id());
        assertFalse(allByWellId.isEmpty());
    }
    private static DownholeDataCreateDto getConstraintObject() {
        return new DownholeDataCreateDto(
                null
        );
    }


    @Test
    void constraintExpectation(){
        DownholeDataCreateDto object = getConstraintObject();
        assertThrows(ConstraintViolationException.class,
                () -> downholeDataService.create(object));
    }
}