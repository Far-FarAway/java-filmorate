package ru.yandex.practicum.filmorate.storage.filmslikes;

import ru.yandex.practicum.filmorate.model.FilmsLikes;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmsLikesStorage {
    boolean addLike(int filmId, int userId);
    Collection<FilmsLikes> getLikes();
    Collection<FilmsLikes> getLikesByFilm(int filmId);
    boolean deleteLike(int filmId, int userId);
}
