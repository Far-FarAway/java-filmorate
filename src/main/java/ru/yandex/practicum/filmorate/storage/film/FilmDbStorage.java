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
import java.util.Collection;
import java.util.List;

@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private final static String FIND_FILM_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private final static String FIND_FILMS_QUERY = "SELECT * FROM films";
    private final static String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private final static String DELETE_FILM_LIKES_QUERY = "DELETE FROM films_likes WHERE film_id = ?";
    private final static String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?," +
            " release_date = ?, duration = ?, genre = ?, rating = ? " +
            "WHERE film_id = ?";
    private final static String INSERT_FILM_QUERY = "INSERT INTO films(name, description, release_date, duration," +
            " genre, rating) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbc, @Qualifier("filmRowMapper") RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> getFilms() {
        return findMany(FIND_FILMS_QUERY);
    }

    public Film postFilm(Film film) {
            int id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate()),
                film.getDuration(),
                film.getGenre(),
                film.getRating());
            film.setId(id);
            return film;
    }

    public boolean updateFilm(Film film) {
        Film oldFilm = findById(film.getId());
        return update(UPDATE_FILM_QUERY,
                film.getName().isBlank() ? oldFilm.getName() : film.getName(),
                film.getDescription().isBlank() ? oldFilm.getDescription() : film.getDescription(),
                Timestamp.from(film.getReleaseDate()),
                film.getDuration(),
                film.getGenre().isBlank() ? oldFilm.getGenre() : film.getGenre(),
                film.getRating().isBlank() ? oldFilm.getRating() : film.getRating(),
                film.getId());
    }

    public boolean deleteFilm(int filmId) {
        delete(DELETE_FILM_LIKES_QUERY, filmId);
        return delete(DELETE_FILM_QUERY, filmId);
    }

    public Film findById(int filmId) {
        return findOne(FIND_FILM_QUERY, filmId).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }
}
