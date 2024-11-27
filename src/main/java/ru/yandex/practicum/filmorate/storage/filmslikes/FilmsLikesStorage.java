package ru.yandex.practicum.filmorate.storage.filmslikes;

import ru.yandex.practicum.filmorate.model.FilmsLikes;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmsLikesStorage {
    int addLike(int filmId, int userId);

    List<FilmsLikes> getLikes();

    List<FilmsLikes> getLikesByFilm(int filmId);

    boolean deleteLike(int filmId, int userId);
}
