package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private final Comparator<Film> comparator = new Comparator<Film>() {
        @Override
        public int compare(Film o1, Film o2) {
            return -1 * (o1.getLikes().size() - o2.getLikes().size());
        }
    };

    public Collection<Integer> addLike(int filmId, int userId, UserStorage userStorage, FilmStorage storage) {
        log.info("Добавление лайка фильму(id: {}) от пользователя(id: {})", filmId, userId);
        Set<Integer> filmLikes = storage.findById(filmId).getLikes();
        userStorage.findById(userId);
        filmLikes.add(userId);
        return filmLikes;
    }

    public Collection<Integer> deleteLike(int filmId, int userId, FilmStorage storage) {
        log.info("Удаление лайка пользователя(id: {}) у фильма(id: {}) ", userId, filmId);
        Set<Integer> filmLikes = storage.findById(filmId).getLikes();
        if (filmLikes.contains(userId)) {
            filmLikes.remove(userId);
            return filmLikes;
        } else {
            log.warn("пользователь(id: {}) не найден в списке лайкнувших фильм(id: {})", userId, filmId);
            throw new NotFoundException("пользователь(id: " + userId +
                    ") не найден в списке лайкнувших фильм(id: " + filmId + ")");
        }
    }

    public Collection<Film> getPopularFilms(int count, FilmStorage storage) {
        return storage.getFilms().stream().limit(count).sorted(comparator).toList();
    }
}
