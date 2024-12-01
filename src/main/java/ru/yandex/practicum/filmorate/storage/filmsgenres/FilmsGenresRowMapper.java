package ru.yandex.practicum.filmorate.storage.filmsgenres;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmsGenres;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Qualifier("filmsGenresRowMapper")
public class FilmsGenresRowMapper implements RowMapper<FilmsGenres> {
    @Override
    public FilmsGenres mapRow(ResultSet results, int rowNum) throws SQLException {
        FilmsGenres filmsGenres = new FilmsGenres();
        filmsGenres.setFilmId(results.getInt("film_id"));
        filmsGenres.setGenreId(results.getInt("genre_id"));

        return filmsGenres;
    }
}
