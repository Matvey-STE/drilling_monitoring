package org.matveyvs.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DownholeDataCreateDto(
        @NotNull
        @Valid
        WellDataReadDto wellDataReadDto) {
}
