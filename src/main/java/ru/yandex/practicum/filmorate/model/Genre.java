package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class Genre {
    @PositiveOrZero
    private int id;
    private String name;
}