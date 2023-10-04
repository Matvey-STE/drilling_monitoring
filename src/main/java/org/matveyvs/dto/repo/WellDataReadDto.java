package org.matveyvs.dto.repo;

public record WellDataReadDto(Integer id, String companyName,
                              String fieldName, String wellCluster, String well) {
}
