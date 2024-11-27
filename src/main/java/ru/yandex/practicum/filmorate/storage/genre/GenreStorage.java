package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre postGenre(Genre genre);

    Genre getGenre(int genreId);

    List<Genre> getGenres();

    boolean deleteGenre(int genreId);
}
