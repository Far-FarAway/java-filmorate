package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Qualifier("filmRowMapper")
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final GenreDbStorage genreStorage;

    @Override
    public Film mapRow(ResultSet results, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(results.getInt("film_id"));
        film.setName(results.getString("name"));
        film.setDescription(results.getString("description"));
        film.setReleaseDate(results.getTimestamp("release_date").toLocalDateTime().toLocalDate());
        film.setDuration(results.getInt("duration"));

        Set<Genre> genresList = new HashSet<>();
        genresList.add(genreStorage.getGenre(results.getInt("genre_id")));
        film.setGenres(genresList);

        film.setMpa(results.getString("rating"));

        return film;
    }
}
