package ru.yandex.practicum.filmorate.storage.filmslikes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmsLikes;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FilmsLikesDbStorage extends BaseRepository<FilmsLikes> implements FilmsLikesStorage {
    private final static String ADD_LIKE_QUERY = "INSERT INTO films_likes(film_id, user_id) " +
            "VALUES (?, ?)";
    private final static String GET_LIKES_QUERY = "SELECT * FROM films_likes";
    private final static String GET_LIKES_BY_FILM_QUERY = "SELECT * FROM films_likes WHERE film_id = ?";
    private final static String DELETE_LIKE_QUERY = "DELETE FROM films_likes WHERE film_id = ? and user_id = ?";

    @Autowired
    public FilmsLikesDbStorage(JdbcTemplate jdbc, @Qualifier("filmsLikesRowMapper") RowMapper<FilmsLikes> mapper) {
        super (jdbc, mapper);
    }

    public boolean addLike(int filmId, int userId) {
        return insert(ADD_LIKE_QUERY, filmId, userId);
    }

    public Map<String, List<String>> getLikes() {
        Map<String, List<String>> filmsLikes = new HashMap<>();
        List<String> likesList = null;
        String filmName = "";

        for(FilmsLikes like : findMany(GET_LIKES_QUERY)) {
            if (!filmName.equals(like.getFilm().getName())) {
                likesList = new ArrayList<>();
                filmName = like.getFilm().getName();
            }

            likesList.add(like.getUser().getName());
            filmsLikes.put(filmName, likesList);
        }

        return filmsLikes;
    }

    public Map<String, List<String>> getLikesByFilm(int filmId) {
        Map<String, List<String>> filmsLikes = new HashMap<>();
        List<String> likesList = new ArrayList<>();

        for(FilmsLikes like : findMany(GET_LIKES_BY_FILM_QUERY, filmId)) {
            likesList.add(like.getUser().getName());
            filmsLikes.put(like.getFilm().getName(), likesList);
        }

        return filmsLikes;
    }

    public boolean deleteLike(int filmId, int userId) {
        return delete(DELETE_LIKE_QUERY, filmId, userId);
    }
}
