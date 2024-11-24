package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmsLikesModel {
    private Film film;
    private User user;
}
