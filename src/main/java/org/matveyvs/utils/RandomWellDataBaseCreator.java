package org.matveyvs.utils;

import lombok.AllArgsConstructor;
import org.matveyvs.dto.*;
import org.matveyvs.entity.*;
import org.matveyvs.service.*;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;

@Component
@AllArgsConstructor
public class RandomWellDataBaseCreator {
    private final UserService userService;
    private final WellDataService wellDataService;
    private final SurfaceDataService surfaceDataService;
    private final DownholeDataService downholeDataService;
    private final DirectionalService directionalService;
    private final GammaService gammaService;
    private static final Random random = new Random();

    public void createRandomDataForTests() {
        createRandomWellInformation("Sibur",
                "Samotlorskoe", "2022",
                "10485", 0.01, 1.00);
        createRandomWellInformation("Rosneft",
                "Tallinskow", "3456",
                "18451", 0.01, 1.00);
        createRandomWellInformation("Gazprom",
                "Yamalskoe", "1500",
                "28324", 0.01, 1.00);
        userService.create(getUserObject());
    }

    private static UserCreateDto getUserObject() {
        return new UserCreateDto(
                "username",
                "email@test.ru",
                "password",
                Role.USER,
                Timestamp.valueOf(LocalDateTime.now()),
                "First Name",
                "Last Name");
    }

    private void createRandomWellInformation(String companyName, String fieldName,
                                             String wellCluster, String well, Double startDepth, Double finishDepth) {
        var wellDataId = wellDataService.create(createRandomWellData(companyName, fieldName, wellCluster, well));
        var byIdWell = wellDataService.findById(wellDataId);

        var downholeDataId = downholeDataService.create(createRandomDownholeData(byIdWell.get()));
        var byIdDownhole = downholeDataService.findById(downholeDataId);
        for (double depth = startDepth; depth < finishDepth; depth += 0.5) {
            surfaceDataService.create(createRandomSurfaceData(depth, byIdWell.get()));

            directionalService.create(createRandomDirectional(depth, byIdDownhole.get()));
            gammaService.create(createRandomGamma(depth, byIdDownhole.get()));
        }
    }


    private DownholeDataCreateDto createRandomDownholeData(WellDataReadDto wellDataReadDto) {
        return new DownholeDataCreateDto(
                wellDataReadDto);
    }

    private GammaCreateDto createRandomGamma(Double measureDepth, DownholeDataReadDto downholeDataReadDto) {
        double grcx = creteRandomDouble(30, 400, 2);

        return new GammaCreateDto(
                Timestamp.valueOf(LocalDateTime.now()),
                measureDepth,
                grcx,
                downholeDataReadDto
        );
    }

    private DirectionalCreateDto createRandomDirectional(Double measureDepth, DownholeDataReadDto downholeDataReadDto) {
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

        return new DirectionalCreateDto(
                Timestamp.valueOf(LocalDateTime.now()),
                measureDepth,
                gx,
                gy,
                gz,
                bx,
                by,
                bz,
                inc,
                azTrue,
                azMag,
                azCorr,
                toolfaceCorr,
                downholeDataReadDto
        );
    }

    private SurfaceDataCreateDto createRandomSurfaceData(Double measureDepth, WellDataReadDto wellDataReadDto) {
        Double holeDepth = creteRandomDouble(0, 3000, 2);
        Double tvDepth = creteRandomDouble(0, 3000, 2);
        Double hookload = creteRandomDouble(0, 300, 2);
        Double wob = creteRandomDouble(0, 40, 2);
        Double blockPos = creteRandomDouble(0, 50, 2);
        Double standpipePr = creteRandomDouble(0, 400, 2);

        return new SurfaceDataCreateDto(
                Timestamp.valueOf(LocalDateTime.now()),
                measureDepth,
                holeDepth,
                tvDepth,
                hookload,
                wob,
                blockPos,
                standpipePr,
                wellDataReadDto
        );
    }

    private WellDataCreateDto createRandomWellData(String companyName, String fieldName,
                                                   String wellCluster, String well) {
        return new WellDataCreateDto(
                companyName,
                fieldName,
                wellCluster,
                well
        );
    }


    private static double creteRandomDouble(int origin, int bound, int round) {
        double randomValue = random.nextDouble(origin, bound);
        return roundDouble(randomValue, round);
    }

    private static double roundDouble(double value, int round) {
        double multiplier = Math.pow(10, round);
        return Math.round(value * multiplier) / multiplier;
    }
}
