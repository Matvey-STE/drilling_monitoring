package org.matveyvs.utils;

import org.matveyvs.dao.*;
import org.matveyvs.entity.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

public class RandomWellDataBaseCreator {
    private static final RandomWellDataBaseCreator INSTANCE = new RandomWellDataBaseCreator();

    GammaDao gammaDao = GammaDao.getInstance();
    DirectionalDao directionalDao = DirectionalDao.getInstance();
    SurfaceDataDao surfaceDataDao = SurfaceDataDao.getInstance();
    DownholeDataDao downholeDataDao = DownholeDataDao.getInstance();
    WellDataDao wellDataDao = WellDataDao.getInstance();

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

        return new Gamma(Timestamp.valueOf(LocalDateTime.now()), measureDepth, grcx, downholeData);
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

        return new Directional
                (Timestamp.valueOf(LocalDateTime.now()), measureDepth,
                        gx, gy, gz, bx, by, bz, inc, azTrue, azMag,
                        azCorr, toolfaceCorr, downholeData);
    }

    private SurfaceData createRandomSurfaceData(Double measureDepth, WellData wellData) {
        Double holeDepth = creteRandomDouble(0, 3000, 2);
        Double tvDepth = creteRandomDouble(0, 3000, 2);
        Double hookload = creteRandomDouble(0, 300, 2);
        Double wob = creteRandomDouble(0, 40, 2);
        Double blockPos = creteRandomDouble(0, 50, 2);
        Double standpipePr = creteRandomDouble(0, 400, 2);

        return new SurfaceData(Timestamp.valueOf(LocalDateTime.now()),
                measureDepth, holeDepth, tvDepth, hookload, wob, blockPos, standpipePr, wellData);
    }

    private DownholeData createRandomDownholeData(WellData wellData) {
        return new DownholeData(wellData);
    }

    private WellData createRandomWellData(String companyName, String fieldName,
                                          String wellCluster, String well) {
        return new WellData(companyName, fieldName, wellCluster, well);
    }


    private static double creteRandomDouble(int origin, int bound, int round) {
        Random random = new Random();
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
