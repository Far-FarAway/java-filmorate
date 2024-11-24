package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikesStorage;

import java.util.*;

@Service
@Slf4j
public class FilmService {
    public boolean addLike(int filmId, int userId, FilmsLikesStorage likesStorage) {
        log.info("Добавление лайка фильму(id: {}) от пользователя(id: {})", filmId, userId);
        return likesStorage.addLike(filmId, userId);
    }

    public boolean deleteLike(int filmId, int userId, FilmsLikesStorage likeStorage) {
        log.info("Удаление лайка пользователя(id: {}) у фильма(id: {}) ", userId, filmId);
        return likeStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(int count, FilmStorage storage, FilmsLikesStorage likesStorage) {
        Comparator<Film> comparator = new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return -1 * (likesStorage.getLikesByFilm(o1.getId()).size() -
                        likesStorage.getLikesByFilm(o2.getId()).size());
            }
        };

        Set<Film> list = new TreeSet<>(comparator);
        list.addAll(storage.getFilms());

        return list;
    }
}
