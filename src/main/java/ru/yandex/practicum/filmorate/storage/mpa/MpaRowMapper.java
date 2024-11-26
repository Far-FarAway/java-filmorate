package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Qualifier("mpaRowMapper")
public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet results, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(results.getInt("mpa_id"));
        mpa.setName(results.getString("name"));
        return mpa;
    }
}
