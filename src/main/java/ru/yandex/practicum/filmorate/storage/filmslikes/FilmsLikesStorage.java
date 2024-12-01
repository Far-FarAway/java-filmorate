package ru.yandex.practicum.filmorate.storage.filmslikes;

import ru.yandex.practicum.filmorate.model.FilmsLikes;

import java.util.List;

public interface FilmsLikesStorage {
    int addLike(int filmId, int userId);

    List<FilmsLikes> getLikes();

    List<FilmsLikes> getLikesByFilm(int filmId);

    boolean deleteLike(int filmId, int userId);
}
