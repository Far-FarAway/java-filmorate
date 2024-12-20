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
import java.time.ZoneId;
import java.util.List;

@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_FILM_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String FIND_FILMS_QUERY = "SELECT * FROM films";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String DELETE_FILM_LIKES_QUERY = "DELETE FROM films_likes WHERE film_id = ?";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?," +
            " release_date = ?, duration = ?, mpa_id = ? " +
            "WHERE film_id = ?";
    private static final String INSERT_FILM_QUERY = "INSERT INTO films(name, description, release_date, duration," +
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

        film.setName(film.getName().isBlank() ? oldFilm.getName() : film.getName());
        film.setDescription(film.getDescription().isBlank() ? oldFilm.getDescription() : film.getDescription());

        Integer mpaId = null;
        if (film.getMpa() == null) {
            if (oldFilm.getMpa() != null) {
                mpaId = oldFilm.getMpa().getId();
            }
        }

        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Timestamp.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                film.getDuration(),
                mpaId,
                film.getId());

        return film;
    }

    public boolean deleteFilm(int filmId) {
        delete(DELETE_FILM_LIKES_QUERY, filmId);
        return delete(DELETE_FILM_QUERY, filmId);
    }

    public Film findById(int filmId) {
        return findOne(FIND_FILM_QUERY, filmId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
