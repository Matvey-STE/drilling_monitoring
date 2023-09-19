package org.matveyvs.entity;

public record WellData(Long id, String companyName, String fieldName, String wellCluster,
                       String well) {
    public WellData(String companyName, String fieldName, String wellCluster,
                    String well) {
        this(null, companyName, fieldName, wellCluster, well);
    }
}
