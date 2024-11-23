package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Friends {
    @PositiveOrZero
    long user_id;
    @PositiveOrZero
    long friends_id;
}
