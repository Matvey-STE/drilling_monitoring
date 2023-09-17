package org.matveyvs.dto;

public record WellDataDto(Long id, String companyName,
                          String fieldName, String wellCluster, String well,
                          Long surfaceDataId, Long downholeDataId) {
}
