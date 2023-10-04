package org.matveyvs.dto.repo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record DownholeDataCreateDto(
        @NotNull
        @Valid
        WellDataReadDto wellDataReadDto) {
}
