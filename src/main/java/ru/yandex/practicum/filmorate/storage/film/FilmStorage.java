package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getFilms();

    boolean postFilm(Film film);

    boolean updateFilm(Film film);

    boolean deleteFilm(int filmId);

    Film findById(int filmId);
}
