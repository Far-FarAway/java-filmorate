package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Qualifier("filmRowMapper")
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Override
    public Film mapRow(ResultSet results, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(results.getInt("film_id"));
        film.setName(results.getString("name"));
        film.setDescription(results.getString("description"));
        film.setReleaseDate(results.getTimestamp("release_date").toLocalDateTime().toLocalDate());
        film.setDuration(results.getInt("duration"));

        if (results.getInt("mpa_id") != 0) {
            film.setMpa(mpaStorage.getMpa(results.getInt("mpa_id")));
        }
        return film;
    }
}
