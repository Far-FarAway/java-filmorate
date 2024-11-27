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
import java.util.HashSet;
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
            " mpa_id) " +
            "VALUES (?, ?, ?, ?, ?)";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, @Qualifier("filmRowMapper") RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> getFilms() {
        return findMany(FIND_FILMS_QUERY);
    }

    public Film postFilm(Film film) {
        Integer mpaId = null;

        if (film.getMpa() != null) {
            mpaId = film.getMpa().getId();
        }

        int id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                film.getDuration(),
                mpaId);
        film.setId(id);

        return film;
    }

    public Film updateFilm(Film film, Film oldFilm) {
        //deleteFilm(film.getId());
        Integer mpaId = null;

        if (oldFilm.getMpa() != null) {
            mpaId = oldFilm.getMpa().getId();
        }

        film.setName(film.getName().isBlank() ? oldFilm.getName() : film.getName());
        film.setDescription(film.getDescription().isBlank() ? oldFilm.getDescription() : film.getDescription());
        film.setMpa(film.getMpa() == null ? oldFilm.getMpa() : film.getMpa());

        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                film.getDuration(),
                film.getMpa() == null ? mpaId : film.getMpa().getId());

        return film;
    }

    public boolean deleteFilm(int filmId) {
        delete(DELETE_FILM_LIKES_QUERY, filmId);
        return delete(DELETE_FILM_QUERY, filmId);
    }

    public Film findById(int filmId) {
        return findOne(FIND_FILM_QUERY, filmId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        /*List<Film> filmList = findMany(FIND_FILM_QUERY, filmId);
        if (filmList.isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        }

        if (filmList.size() > 1) {
            Film resultFilm = filmList.getFirst();
            filmList.forEach(film -> resultFilm.getGenres().addAll(film.getGenres()));
            return resultFilm;
        } else {
            return filmList.getFirst();
        }*/
    }
}
