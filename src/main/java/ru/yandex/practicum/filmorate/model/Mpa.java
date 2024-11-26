package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Mpa {
    @PositiveOrZero
    private int id;
    private String name;
}
