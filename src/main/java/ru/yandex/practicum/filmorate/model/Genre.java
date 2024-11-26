package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Genre {
    @PositiveOrZero
    int id;
    String name;
}
