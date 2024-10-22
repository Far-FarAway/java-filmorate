package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getFilms();
    Film postFilm(Film film);
    Film updateFilm(Film film);
    Film deleteFilm(int filmId);
    Film findById(int filmId);
}
