package org.matveyvs.utils;

import org.matveyvs.dao.*;
import org.matveyvs.entity.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class RandomWellDataBaseCreator {
    private static final RandomWellDataBaseCreator INSTANCE = new RandomWellDataBaseCreator();
    UserDao userDao = UserDao.getInstance();
    GammaDao gammaDao = GammaDao.getInstance();
    DirectionalDao directionalDao = DirectionalDao.getInstance();
    SurfaceDataDao surfaceDataDao = SurfaceDataDao.getInstance();
    DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    WellDataDao wellDataDao = WellDataDao.getInstance();
    private static final Random random = new Random();

    public void createRandomDataForTests(){
        createRandomWellInformation("Company test",
                "FieldName test", "WellCluster test",
                "Well test" , 0.01, 3.00);
        userDao.save(getUserObject());
    }
    private static User getUserObject() {
        return User.builder()
                .userName("Random Test")
                .email("Random Test")
                .password("Test")
                .role(Role.USER)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .firstName("Test")
                .lastName("Test")
                .build();
    }

    public void createRandomWellInformation(String companyName, String fieldName,
                                            String wellCluster, String well, Double startDepth, Double finishDepth) {
        var wellData = wellDataDao.save(createRandomWellData(companyName, fieldName, wellCluster, well));

        var downholeData = downholeDataDao.save(createRandomDownholeData(wellData));

        for (double depth = startDepth; depth < finishDepth; depth += 0.5) {
            surfaceDataDao.save(createRandomSurfaceData(depth, wellData));

            directionalDao.save(createRandomDirectional(depth, downholeData));
            gammaDao.save(createRandomGamma(depth, downholeData));
        }
    }


    private Gamma createRandomGamma(Double measureDepth, DownholeData downholeData) {
        double grcx = creteRandomDouble(30, 400, 2);

        return Gamma.builder()
                .measureDate(Timestamp.valueOf(LocalDateTime.now()))
                .measuredDepth(measureDepth)
                .grcx(grcx)
                .downholeData(downholeData)
                .build();
    }

    private Directional createRandomDirectional(Double measureDepth, DownholeData downholeData) {
        double gx = creteRandomDouble(-1, 1, 4);
        double gy = creteRandomDouble(-1, 1, 4);
        double gz = creteRandomDouble(-1, 1, 4);
        double bx = creteRandomDouble(-1, 1, 4);
        double by = creteRandomDouble(-1, 1, 4);
        double bz = creteRandomDouble(-1, 1, 4);
        double inc = creteRandomDouble(0, 120, 2);
        double azTrue = creteRandomDouble(0, 359, 2);
        double azMag = creteRandomDouble(0, 359, 2);
        double azCorr = creteRandomDouble(0, 359, 2);
        double toolfaceCorr = creteRandomDouble(0, 359, 2);

        return Directional.builder()
                .measureDate(Timestamp.valueOf(LocalDateTime.now()))
                .measuredDepth(measureDepth)
                .gx(gx)
                .gy(gy)
                .gz(gz)
                .bx(bx)
                .by(by)
                .bz(bz)
                .inc(inc)
                .azTrue(azTrue)
                .azMag(azMag)
                .azCorr(azCorr)
                .toolfaceCorr(toolfaceCorr)
                .downholeData(downholeData)
                .build();
    }

    private SurfaceData createRandomSurfaceData(Double measureDepth, WellData wellData) {
        Double holeDepth = creteRandomDouble(0, 3000, 2);
        Double tvDepth = creteRandomDouble(0, 3000, 2);
        Double hookload = creteRandomDouble(0, 300, 2);
        Double wob = creteRandomDouble(0, 40, 2);
        Double blockPos = creteRandomDouble(0, 50, 2);
        Double standpipePr = creteRandomDouble(0, 400, 2);

        return SurfaceData.builder()
                .measuredDate(Timestamp.valueOf(LocalDateTime.now()))
                .measuredDepth(measureDepth)
                .holeDepth(holeDepth)
                .tvDepth(tvDepth)
                .hookload(hookload)
                .wob(wob)
                .blockPos(blockPos)
                .standpipePressure(standpipePr)
                .wellData(wellData)
                .build();
    }

    private DownholeData createRandomDownholeData(WellData wellData) {
        return DownholeData.builder()
                .wellData(wellData)
                .build();
    }

    private WellData createRandomWellData(String companyName, String fieldName,
                                          String wellCluster, String well) {
        return WellData.builder()
                .companyName(companyName)
                .fieldName(fieldName)
                .wellCluster(wellCluster)
                .well(well)
                .build();
    }


    private static double creteRandomDouble(int origin, int bound, int round) {
        double randomValue = random.nextDouble(origin, bound);
        return roundDouble(randomValue, round);
    }

    private static double roundDouble(double value, int round) {
        double multiplier = Math.pow(10, round);
        return Math.round(value * multiplier) / multiplier;
    }

    private RandomWellDataBaseCreator() {

    }

    public static RandomWellDataBaseCreator getInstance() {
        return INSTANCE;
    }
}
