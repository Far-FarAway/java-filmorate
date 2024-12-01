package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmsLikes {
    private Film film;
    private User user;
}
