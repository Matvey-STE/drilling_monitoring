package org.matveyvs.entity;

public record WellData(Long id, String companyName, String fieldName, String wellCluster,
                       String well, SurfaceData surfaceData, DownholeData downholeData) {
    public WellData(String companyName, String fieldName, String wellCluster,
                    String well, SurfaceData surfaceData, DownholeData downholeData) {
        this(null, companyName, fieldName, wellCluster, well, surfaceData, downholeData);
    }
}
