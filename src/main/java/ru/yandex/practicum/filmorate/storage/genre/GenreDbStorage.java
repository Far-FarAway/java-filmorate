package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private static final String POST_GENRE_QUERY = "INSERT INTO genres(name) VALUES (?)";
    private static final String FIND_GENRE_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String FIND_GENRES_QUERY = "SELECT * FROM genres";
    private static final String DELETE_GENRE_QUERY = "DELETE FROM genres WHERE genre_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, @Qualifier("genreRowMapper") RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Genre postGenre(Genre genre) {
        if (findMany(FIND_GENRE_QUERY, genre.getId()).isEmpty()) {
            int id = insert(POST_GENRE_QUERY, genre.getName());
            genre.setId(id);
            return genre;
        } else {
            throw new DuplicateDataException("Жанр " + genre.getName() + " уже существует");
        }
    }

    public Genre getGenre(int genreId) {
        return findOne(FIND_GENRE_QUERY, genreId)
                .orElseThrow(() -> new NotFoundException("Жанр с id " + genreId + " не найден"));
    }

    public List<Genre> getGenres() {
        return new ArrayList<>(findMany(FIND_GENRES_QUERY));
    }

    public boolean deleteGenre(int genreId) {
        return delete(DELETE_GENRE_QUERY, genreId);
    }
}
