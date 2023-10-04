package org.matveyvs.dto.repo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record DownholeDataReadDto(
        Integer id,
        @NotNull
        @Valid
        WellDataReadDto wellDataReadDto) {
}
