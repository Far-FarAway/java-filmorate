package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private final static String FIND_FILM_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private final static String FIND_FILMS_QUERY = "SELECT * FROM films";
    private final static String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private final static String DELETE_FILM_LIKES_QUERY = "DELETE FROM films_likes WHERE film_id = ?";
    private final static String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?," +
            " release_date = ?, duration = ?, genre_id = ?, mpa_id = ? " +
            "WHERE film_id = ?";
    private final static String INSERT_FILM_QUERY = "INSERT INTO films(name, description, release_date, duration," +
            " genre_id, mpa_id) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, @Qualifier("filmRowMapper") RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> getFilms() {
        //Список вроде не хранит данные по порядку, так что момент  проверкой id может выдать ошибку
        List<Film> filmList = findMany(FIND_FILMS_QUERY);
        List<Film> newFilmList = new ArrayList<>();

        if (filmList.size() > 1) {
            Film groupingFilm = filmList.getFirst();
            int id = groupingFilm.getId();

            for(Film film : filmList) {
                if (id != film.getId()) {
                    newFilmList.add(groupingFilm);
                    groupingFilm = film;
                    id = film.getId();
                }

                groupingFilm.getGenres().addAll(film.getGenres());
            }

            return newFilmList;
        }

        return newFilmList;
    }

    public Film postFilm(Film film) {
        Set<Genre> genres = film.getGenres();
        int id = -3;
        if (genres == null) {
            id = insert(INSERT_FILM_QUERY,
                    film.getName(),
                    film.getDescription(),
                    Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    film.getDuration(),
                    null,
                    film.getMpa());
            film.setId(id);
            return film;
        } else {
            for (Genre genre : genres) {
                id = insert(INSERT_FILM_QUERY,
                        film.getName(),
                        film.getDescription(),
                        Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        film.getDuration(),
                        genre.getId(),
                        film.getMpa());
            }
            film.setId(id);
            return film;
        }
    }

    public boolean updateFilm(Film film) {
        Film oldFilm = findById(film.getId());
        deleteFilm(film.getId());

        Set<Genre> genres = film.getGenres();
        int id = film.getId();

        if (genres == null) {
            for (Genre genre : oldFilm.getGenres()) {
                id = insert(INSERT_FILM_QUERY,
                        film.getName().isBlank() ? oldFilm.getName() : film.getName(),
                        film.getDescription().isBlank() ? oldFilm.getDescription() : film.getDescription(),
                        Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        film.getDuration(),
                        genre.getId(),
                        film.getMpa() == null ? oldFilm.getMpa() : film.getMpa());
            }

            return true;
        } else if (oldFilm.getGenres() == null){
            id = insert(INSERT_FILM_QUERY,
                    film.getName().isBlank() ? oldFilm.getName() : film.getName(),
                    film.getDescription().isBlank() ? oldFilm.getDescription() : film.getDescription(),
                    Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    film.getDuration(),
                    null,
                    film.getMpa() == null ? oldFilm.getMpa() : film.getMpa());

            return true;
        } else {
            for (Genre genre : film.getGenres()) {
                id = insert(INSERT_FILM_QUERY,
                        film.getName().isBlank() ? oldFilm.getName() : film.getName(),
                        film.getDescription().isBlank() ? oldFilm.getDescription() : film.getDescription(),
                        Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        film.getDuration(),
                        genre.getId(),
                        film.getMpa() == null ? oldFilm.getMpa() : film.getMpa());
            }

            return true;
        }
    }

    public boolean deleteFilm(int filmId) {
        delete(DELETE_FILM_LIKES_QUERY, filmId);
        return delete(DELETE_FILM_QUERY, filmId);
    }

    public Film findById(int filmId) {
        List<Film> filmList = findMany(FIND_FILM_QUERY, filmId);
        if (filmList.isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        }

        if (filmList.size() > 1) {
            Film resultFilm = filmList.getFirst();
            filmList.forEach(film -> resultFilm.getGenres().addAll(film.getGenres()));
            return resultFilm;
        } else {
            return filmList.getFirst();
        }

        //return findOne(FIND_FILM_QUERY, filmId).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }
}
