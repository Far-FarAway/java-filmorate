package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Qualifier("filmRowMapper")
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet results, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(results.getInt("film_id"));
        film.setName(results.getString("name"));
        film.setDescription(results.getString("description"));
        film.setReleaseDate(results.getTimestamp("release_date").toInstant());
        film.setDuration(results.getInt("duration"));
        film.setGenre(results.getString("genre"));
        film.setRating(results.getString("rating"));

        return film;
    }
}
