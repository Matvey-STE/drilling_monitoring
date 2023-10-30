package org.matveyvs.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public record DownholeDataReadDto(
        Integer id,
        @NotNull
        @Valid
        WellDataReadDto wellDataReadDto) {
}
