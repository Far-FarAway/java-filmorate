package ru.yandex.practicum.filmorate.storage.filmslikes;

import java.util.List;
import java.util.Map;

public interface FilmsLikesStorage {
    boolean addLike(int filmId, int userId);
    Map<String, List<String>> getLikes();
    Map<String, List<String>> getLikesByFilm(int filmId);
    boolean deleteLike(int filmId, int userId);
}
