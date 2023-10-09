//package org.matveyvs.service.repo.service;
//
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.matveyvs.dao.TestUtil.TestDatabaseUtil;
//import org.matveyvs.dao.repository.DownholeDataRepository;
//import org.matveyvs.dao.repository.GammaRepository;
//import org.matveyvs.dao.repository.WellDataRepository;
//import org.matveyvs.dto.repo.*;
//import org.matveyvs.mapper.repo.DownholeDataMapperImpl;
//import org.matveyvs.mapper.repo.GammaMapperImpl;
//import org.matveyvs.mapper.repo.WellDataMapperImpl;
//import org.matveyvs.utils.HibernateUtil;
//
//import javax.validation.ConstraintViolationException;
//import java.lang.reflect.Proxy;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GammaServiceTest {
//
//    SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
//    private GammaService gammaService;
//    private DownholeDataService downholeDataService;
//    private WellDataService wellDataService;
//    private static WellDataReadDto wellDataReadDto;
//    private static DownholeDataReadDto downholeDataReadDto;
//
//    private Session session;
//
//    @BeforeEach
//    void setUp() {
//        TestDatabaseUtil.createRandomData();
//
//        session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
//                new Class[]{Session.class},
//                (proxy, method, arg1) -> method.invoke(sessionFactory.getCurrentSession(), arg1));
//        session = sessionFactory.openSession();
//        session.beginTransaction();
//
//        wellDataService = new WellDataService(new WellDataRepository(session), new WellDataMapperImpl());
//        Integer integer = wellDataService.create(new WellDataCreateDto(
//                "Test",
//                "Test",
//                "Test",
//                "Test"));
//        wellDataReadDto = wellDataService.findById(integer).get();
//
//        downholeDataService = new DownholeDataService(new DownholeDataRepository(session),
//                new DownholeDataMapperImpl());
//        Integer downholeId = downholeDataService.create(
//                new DownholeDataCreateDto(wellDataReadDto));
//        downholeDataReadDto = downholeDataService.findById(downholeId).get();
//
//        gammaService = new GammaService(new GammaRepository(session),
//                new GammaMapperImpl());
//    }
//
//    @AfterEach
//    void tearDown() {
//        session.getTransaction().commit();
//
//        //remove all tables
//        TestDatabaseUtil.dropListOfTables();
//    }
//
//    private static GammaCreateDto getObject() {
//        return new GammaCreateDto(
//                Timestamp.valueOf(LocalDateTime.now()),
//                2.2,
//                3.3,
//                downholeDataReadDto
//        );
//    }
//
//    private static GammaReadDto getUpdatedObject() {
//        return new GammaReadDto(1,
//                Timestamp.valueOf(LocalDateTime.now()),
//                5.5,
//                4.4,
//                downholeDataReadDto
//        );
//    }
//
//    @Test
//    void create() {
//        GammaCreateDto object = getObject();
//        Integer byId = gammaService.create(object);
//        Optional<GammaReadDto> saved = gammaService.findById(byId);
//        assertTrue(saved.isPresent());
//        assertEquals(object.measureDate(), saved.get().measureDate());
//    }
//
//    @Test
//    void update() {
//        Optional<GammaReadDto> saved = gammaService.findById(1);
//        GammaReadDto objectToUpdate = getUpdatedObject();
//        boolean update = gammaService.update(objectToUpdate);
//        assertTrue(update);
//        Optional<GammaReadDto> updated = gammaService.findById(1);
//        assertNotEquals(saved.get().measuredDepth(), updated.get().measuredDepth());
//
//    }
//
//    @Test
//    void findById() {
//        GammaCreateDto object = getObject();
//        Integer id = gammaService.create(object);
//        Optional<GammaReadDto> byId = gammaService.findById(id);
//        assertEquals(object.grcx(), byId.get().grcx());
//    }
//
//    @Test
//    void findAll() {
//        GammaCreateDto object = getObject();
//        gammaService.create(object);
//        List<GammaReadDto> all = gammaService.findAll();
//        assertFalse(all.isEmpty());
//    }
//
//    @Test
//    void delete() {
//        GammaCreateDto object = getObject();
//        Integer id = gammaService.create(object);
//        boolean delete = gammaService.delete(id);
//        assertTrue(delete);
//        Optional<GammaReadDto> byId = gammaService.findById(id);
//        assertTrue(byId.isEmpty());
//    }
//
//    @Test
//    void findAllByDownholeId() {
//        GammaCreateDto object = getObject();
//        gammaService.create(object);
//        List<GammaReadDto> allByWellId = gammaService
//                .findAllByDownholeId(downholeDataReadDto.id());
//        assertFalse(allByWellId.isEmpty());
//    }
//    private static GammaCreateDto getConstraintObject() {
//        return new GammaCreateDto(
//                null,
//                2.2,
//                3.3,
//                downholeDataReadDto
//        );
//    }
//    @Test
//    void constraintExpectation(){
//        GammaCreateDto object = getConstraintObject();
//        assertThrows(ConstraintViolationException.class,
//                () -> gammaService.create(object));
//    }
//}