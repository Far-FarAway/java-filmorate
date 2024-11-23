package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getFilms();

    int postFilm(Film film);

    boolean updateFilm(Film film);

    boolean deleteFilm(int filmId);

    Optional<Film> findById(int filmId);
}
