package ru.yandex.practicum.filmorate.storage.filmslikes;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmsLikes;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Qualifier("filmsLikesRowMapper")
public class FilmsLikesRowMapper implements RowMapper<FilmsLikes> {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Override
    public FilmsLikes mapRow(ResultSet results, int rowNum) throws SQLException {
        FilmsLikes filmsLikes = new FilmsLikes();
        filmsLikes.setFilm(filmStorage.findById(results.getInt("film_id")));
        filmsLikes.setUser(userStorage.findById(results.getInt("user_id")));

        return filmsLikes;
    }
}
