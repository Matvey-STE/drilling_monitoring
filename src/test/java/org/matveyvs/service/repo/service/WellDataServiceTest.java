package org.matveyvs.service.repo.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
import org.matveyvs.dao.repository.WellDataRepository;
import org.matveyvs.dto.repo.WellDataCreateDto;
import org.matveyvs.dto.repo.WellDataReadDto;
import org.matveyvs.mapper.repo.WellDataMapperImpl;
import org.matveyvs.utils.HibernateUtil;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class WellDataServiceTest {
    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
    private WellDataService wellDataService;
    private Session session;

    @BeforeEach
    void setUp() {
        //create random data
        TestDatabaseUtil.createRandomData();

        session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, arg1) -> method.invoke(sessionFactory.getCurrentSession(), arg1));
        session = sessionFactory.openSession();
        session.beginTransaction();
        wellDataService = new WellDataService(new WellDataRepository(session), new WellDataMapperImpl());
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().commit();

        //remove all tables
        TestDatabaseUtil.dropListOfTables();
    }

    private static WellDataCreateDto getObject() {
        return new WellDataCreateDto(
                "Test",
                "Test",
                "Test",
                "Test"
        );
    }

    private static WellDataReadDto getUpdatedObject() {
        return new WellDataReadDto(1,
                "Test",
                "Test",
                "Test",
                "Test"
        );
    }


    @Test
    void create() {
        WellDataCreateDto object = getObject();
        Integer byId = wellDataService.create(object);
        Optional<WellDataReadDto> saved = wellDataService.findById(byId);
        assertTrue(saved.isPresent());
        assertEquals(object.companyName(), saved.get().companyName());
    }

    @Test
    void update() {
        Optional<WellDataReadDto> saved = wellDataService.findById(1);
        WellDataReadDto objectToUpdate = getUpdatedObject();
        boolean update = wellDataService.update(objectToUpdate);
        assertTrue(update);
        Optional<WellDataReadDto> updated = wellDataService.findById(1);
        assertNotEquals(saved.get().companyName(), updated.get().companyName());
        assertNotEquals(saved.get().fieldName(), updated.get().fieldName());
        assertEquals(objectToUpdate.fieldName(), updated.get().fieldName());
    }

    @Test
    void findById() {
        WellDataCreateDto object = getObject();
        Integer id = wellDataService.create(object);
        Optional<WellDataReadDto> byId = wellDataService.findById(id);
        assertEquals(object.fieldName(), byId.get().fieldName());
    }

    @Test
    void findAll() {
        WellDataCreateDto object = getObject();
        wellDataService.create(object);
        List<WellDataReadDto> all = wellDataService.findAll();
        assertFalse(all.isEmpty());
    }


    @Test
    void delete() {
        WellDataCreateDto object = getObject();
        Integer id = wellDataService.create(object);
        boolean delete = wellDataService.delete(id);
        assertTrue(delete);
        Optional<WellDataReadDto> byId = wellDataService.findById(id);
        assertTrue(byId.isEmpty());
    }

    private static WellDataCreateDto getConstraintObject() {
        return new WellDataCreateDto(
                null,
                "Test",
                "Test",
                "Test"
        );
    }

    @Test
    void constraintExpectation() {
        WellDataCreateDto object = getConstraintObject();
        assertThrows(ConstraintViolationException.class,
                () -> wellDataService.create(object));
    }
}