package ru.yandex.practicum.filmorate.storage.filmsgenres;

import ru.yandex.practicum.filmorate.model.FilmsGenres;

import java.util.List;

public interface FilmsGenresStorage {
    int addGenre(int filmId, int genreId);

    List<FilmsGenres> getGenreByFilm(int filmId);

    boolean deleteGenreByFilm(int filmId, int genreId);

    FilmsGenres updateGenre(int filmId, int genreId, int newFilmId, int newGenreId);

    boolean deleteFilm(int filmId);
}
