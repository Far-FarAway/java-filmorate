package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;

@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private final static String FIND_FILM_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private final static String FIND_FILMS_QUERY = "SELECT * FROM films";
    private final static String DELETE_FILM_QUERY = "DELETE FROM films WHERE id = ?";
    private final static String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?," +
            " release_date = ?, duration = ?, genre = ?, rating = ?";
    private final static String INSERT_FILM_QUERY = "INSERT INTO films(name, description, release_date, duration," +
            " genre, rating) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, @Qualifier("filmRowMapper") RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Film> getFilms() {
        return findMany(FIND_FILMS_QUERY);
    }

    public boolean postFilm(Film film) {
        return insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate()),
                film.getDuration(),
                film.getGenre(),
                film.getRating());
    }

    public boolean updateFilm(Film film) {
        Film oldFilm = findById(film.getId());
        return update(UPDATE_FILM_QUERY,
                film.getName().isBlank() ? oldFilm.getName() : film.getName(),
                film.getDescription().isBlank() ? oldFilm.getDescription() : film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenre().isBlank() ? oldFilm.getGenre() : film.getGenre(),
                film.getRating().isBlank() ? oldFilm.getRating() : film.getRating());
    }

    public boolean deleteFilm(int filmId) {
        return delete(DELETE_FILM_QUERY, filmId);
    }

    public Film findById(int filmId) {
        return findOne(FIND_FILM_QUERY, filmId).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }
}
