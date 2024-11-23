package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Friends {
    @PositiveOrZero
    int user_id;
    @PositiveOrZero
    int friends_id;
}
