package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsLikes;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikesStorage;

import java.util.*;
import java.util.stream.Collectors;

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

    public Map<String, List<String>> getLikes(FilmsLikesStorage filmsLikesStorage) {
        Map<String, List<String>> filmsLikes = new HashMap<>();
        List<String> likesList = null;
        String filmName = "";

        for(FilmsLikes like : filmsLikesStorage.getLikes()) {
            if (!filmName.equals(like.getFilm().getName())) {
                likesList = new ArrayList<>();
                filmName = like.getFilm().getName();
            }

            likesList.add(like.getUser().getName());
            filmsLikes.put(filmName, likesList);
        }

        return filmsLikes;
    }

    public Map<String, List<String>> getLikesByFilm(int filmId, FilmsLikesStorage filmsLikesStorage) {
        Map<String, List<String>> filmsLikes = new HashMap<>();
        List<String> likesList = new ArrayList<>();

        for(FilmsLikes like : filmsLikesStorage.getLikesByFilm(filmId)) {
            likesList.add(like.getUser().getName());
            filmsLikes.put(like.getFilm().getName(), likesList);
        }

        return filmsLikes;
    }

    public Collection<String> getGenres(FilmStorage filmStorage) {
        return filmStorage.getFilms()
                .stream()
                .map(Film::getGenre)
                .collect(Collectors.toSet());
    }

    public String getGenreByFilm(int filmId, FilmStorage filmStorage) {
        return filmStorage.findById(filmId).getGenre();
    }

    public Collection<String> getRating(FilmStorage filmStorage) {
        return filmStorage.getFilms()
                .stream()
                .map(Film::getRating)
                .collect(Collectors.toSet());
    }

    public String getRatingByFilm(int filmId, FilmStorage filmStorage) {
        return filmStorage.findById(filmId).getRating();
    }
}
