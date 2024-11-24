package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {
    private final JdbcTemplate jdbc;
    private final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected boolean delete(String query, Object... params) {
        int rows = jdbc.update(query, params);
        return rows > 0;
    }

    protected boolean update(String query, Object... params) {
        int rows = jdbc.update(query, params);
        if (rows == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        } else {
            return true;
        }
    }

    protected boolean insert(String query, Object... params) {
        int rows = jdbc.update(query, params);

        if (rows == 0) {
            throw new InternalServerException("Не удалось вставить данные");
        } else {
            return true;
        }
    }
}
