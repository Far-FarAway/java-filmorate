package ru.yandex.practicum.filmorate.storage.filmsgenres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmsGenres;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class FilmsGenresDbStorage extends BaseRepository<FilmsGenres> implements FilmsGenresStorage {
    private static final String ADD_GENRE_TO_FILM_QUERY = "INSERT INTO films_genres(film_id, genre_id) VALUES (?, ?)";
    private static final String FIND_GENRES_BY_FILM_QUERY = "SELECT * FROM films_genres WHERE film_id = ?";
    private static final String UPDATE_GENRES_BY_FILM_QUERY = "UPDATE films_genres SET film_id = ?, genre_id = ? " +
            "WHERE film_id = ? AND genre_id = ?";
    private static final String DELETE_GENRES_BY_FILM_QUERY =
            "DELETE FROM films_genres WHERE film_id = ? AND genre_id = ?";
    private static final String DELETE_FILM_QUERY =
            "DELETE FROM films_genres WHERE film_id = ?";

    @Autowired
    public FilmsGenresDbStorage(JdbcTemplate jdbc, @Qualifier("filmsGenresRowMapper") RowMapper<FilmsGenres> mapper) {
        super(jdbc, mapper);
    }

    public int addGenre(int filmId, int genreId) {
        return insert(ADD_GENRE_TO_FILM_QUERY, filmId, genreId);
    }

    public List<FilmsGenres> getGenreByFilm(int filmId) {
        return findMany(FIND_GENRES_BY_FILM_QUERY, filmId);
    }

    public boolean deleteGenreByFilm(int filmId, int genreId) {
        return delete(DELETE_GENRES_BY_FILM_QUERY, filmId, genreId);
    }

    public FilmsGenres updateGenre(int filmId, int genreId, int newFilmId, int newGenreId) {
        update(UPDATE_GENRES_BY_FILM_QUERY, filmId, genreId, newFilmId, newGenreId);
        FilmsGenres filmsGenres = new FilmsGenres();
        filmsGenres.setFilmId(newFilmId);
        filmsGenres.setGenreId(newGenreId);

        return filmsGenres;
    }

    public boolean deleteFilm(int filmId) {
        return delete(DELETE_FILM_QUERY, filmId);
    }
}
