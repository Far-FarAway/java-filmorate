package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getFilms();

    Optional<Film> postFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<Film> deleteFilm(int filmId);

    Optional<Film> findById(int filmId);
}
