package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Friends {
    @PositiveOrZero
    private int userId;
    @PositiveOrZero
    private int friendsId;
}
