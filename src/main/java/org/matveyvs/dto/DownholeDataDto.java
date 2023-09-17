package org.matveyvs.dto;

import org.matveyvs.entity.Directional;
import org.matveyvs.entity.Gamma;

import java.sql.Timestamp;

public record DownholeDataDto(Long id, Timestamp timestamp, Directional directional, Gamma gamma) {

}
