package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film postFilm(Film film);

    Film updateFilm(Film film, Film oldFilm);

    boolean deleteFilm(int filmId);

    Film findById(int filmId);
}
