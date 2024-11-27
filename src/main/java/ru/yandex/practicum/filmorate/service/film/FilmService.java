package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmsgenres.FilmsGenresStorage;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikesStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.Array;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmsGenresStorage filmsGenresStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final FilmsLikesStorage likesStorage;

    public Film getFilm(int id) {
        Film film = filmStorage.findById(id);
        film.setGenres(new ArrayList<>());
        for (FilmsGenres genre : filmsGenresStorage.getGenreByFilm(id)) {
            film.getGenres().add(genreStorage.getGenre(genre.getGenreId()));
        }

        return film;
    }

    public List<Film> getFilms() {
        List<Film> filmList = filmStorage.getFilms();
        filmList.forEach(film -> {
            List<FilmsGenres> filmsGenres = filmsGenresStorage.getGenreByFilm(film.getId());
            film.setGenres(new ArrayList<>());

            if (filmsGenres != null) {
                for (FilmsGenres filmGenres : filmsGenres) {
                    film.getGenres().add(genreStorage.getGenre(filmGenres.getGenreId()));
                }
            }
        });
        return filmList;
    }

    public Film postFilm(Film film) {
        validateData(film);

        List<Genre> filmsGenres = film.getGenres();
        Film resultFilm = null;

        if (filmsGenres == null) {
            resultFilm = filmStorage.postFilm(film);
        } else {
            List<Genre> allGenres = new ArrayList<>();
            resultFilm = filmStorage.postFilm(film);

            for(Genre genre : filmsGenres) {
                if (!allGenres.contains(genre)) {
                    filmsGenresStorage.addGenre(resultFilm.getId(), genre.getId());
                    allGenres.add(genreStorage.getGenre(genre.getId()));
                }
            }

            resultFilm.setGenres(allGenres);
        }

        return resultFilm;
    }

    public Film updateFilm(Film film) {
        validateData(film);
        
        Film oldFilm = filmStorage.findById(film.getId());
        oldFilm.setGenres(new ArrayList<>());
        for (FilmsGenres genre : filmsGenresStorage.getGenreByFilm(oldFilm.getId())) {
            oldFilm.getGenres().add(genreStorage.getGenre(genre.getGenreId()));
        }

        List<Genre> oldGenres = oldFilm.getGenres();
        List<Genre> genres = film.getGenres();
        Film resultFilm = null;

        if (genres == null && oldGenres == null) {
            resultFilm = filmStorage.updateFilm(film, oldFilm);
        } else if (genres == null){
            resultFilm = filmStorage.updateFilm(film, oldFilm);
            resultFilm.setGenres(oldGenres);
        } else if (oldGenres == null) {
            resultFilm = filmStorage.updateFilm(film, oldFilm);
            List<Genre> resultGenres = new ArrayList<>();

            for (FilmsGenres filmGenre : filmsGenresStorage.getGenreByFilm(resultFilm.getId())) {
                Genre genre = genreStorage.getGenre(filmGenre.getGenreId());
                if (!resultGenres.contains(genre)) {
                    filmsGenresStorage.addGenre(filmGenre.getFilmId(), filmGenre.getGenreId());
                    resultGenres.add(genre);
                }
            }
            resultFilm.setGenres(resultGenres);
        } else {
            resultFilm = filmStorage.updateFilm(film, oldFilm);
            List<Genre> resultGenres = new ArrayList<>();

            genres.forEach(genre -> {
                for (Genre oldGenre : oldGenres) {
                    genre.setName(!genre.getName().equals(oldGenre.getName()) ?
                            genre.getName() : oldGenre.getName());
                    filmsGenresStorage.deleteGenreByFilm(oldFilm.getId(), oldGenre.getId());
                }
                if (!resultGenres.contains(genre)) {
                    filmsGenresStorage.addGenre(film.getId(), genre.getId());
                    resultGenres.add(genreStorage.getGenre(genre.getId()));
                }
            });

            resultFilm.setGenres(resultGenres);
        }

        return resultFilm;

    }

    public int addLike(int filmId, int userId, FilmsLikesStorage likesStorage) {
        log.info("Добавление лайка фильму(id: {}) от пользователя(id: {})", filmId, userId);
        return likesStorage.addLike(filmId, userId);
    }

    public boolean deleteLike(int filmId, int userId, FilmsLikesStorage likeStorage) {
        log.info("Удаление лайка пользователя(id: {}) у фильма(id: {}) ", userId, filmId);
        return likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        Comparator<Film> comparator = new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return -1 * (likesStorage.getLikesByFilm(o1.getId()).size() -
                        likesStorage.getLikesByFilm(o2.getId()).size());
            }
        };

        List<Film> resultFilms = new ArrayList<>();

        if (likesStorage.getLikes().size() > 1) {
            resultFilms.addAll(getFilms());
            resultFilms.sort(comparator);
        } else {
            resultFilms.addAll(getFilms());
        }

        if (resultFilms.size() >= count) {
            return resultFilms.subList(0, count);
        } else {
            return resultFilms;
        }
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

    public List<Genre> getGenreByFilm(int filmId, FilmStorage filmStorage) {
        return filmStorage.findById(filmId).getGenres();
    }

    public Mpa getMpaByFilm(int filmId, FilmStorage filmStorage) {
        return filmStorage.findById(filmId).getMpa();
    }


    public void validateData(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ConditionNotMetException("Дата не может быть раньше 12.28.1985");
        }

        if (film.getMpa() != null && !mpaStorage.getMpas().contains(film.getMpa())) {
            throw new ConditionNotMetException("Рейтинга с  таким id " + film.getMpa().getId() + " не существует ");
        }

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (!genreStorage.getGenres().contains(genre)) {
                    throw new ConditionNotMetException("Жанра с таким id " + genre.getId() + " не существует");
                }
            }
        }
    }

}
