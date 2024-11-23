package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class FilmsLikes {
    @PositiveOrZero
    private int filmId;
    @PositiveOrZero
    private int userId;
}
