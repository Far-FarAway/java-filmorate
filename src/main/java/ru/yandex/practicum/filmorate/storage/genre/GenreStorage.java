package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage {
    Genre getGenre(int genreId);
    Set<Genre> getGenres();
    boolean deleteGenre(int genreId);
}
