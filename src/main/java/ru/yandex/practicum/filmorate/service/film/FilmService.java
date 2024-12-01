package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Map;

public interface FilmService {
    Film getFilm(int id);

    List<Film> getFilms();

    Film postFilm(Film film);

    Film updateFilm(Film film);

    int addLike(int filmId, int userId);

    boolean deleteLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);

    Map<String, List<String>> getLikes();

    Map<String, List<String>> getLikesByFilm(int filmId);

    List<Genre> getGenreByFilm(int filmId);

    Mpa getMpaByFilm(int filmId);

    boolean deleteFilm(int filmId);

}
