package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;

@Repository
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaStorage {
    private static final String POST_MPA_QUERY = "INSERT INTO mpas(name) VALUES (?)";
    private static final String FIND_MPA_QUERY = "SELECT * FROM mpas WHERE mpa_id = ?";
    private static final String FIND_MPAS_QUERY = "SELECT * FROM mpas";
    private static final String DELETE_MPA_QUERY = "DELETE FROM mpas WHERE mpa_id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, @Qualifier("mpaRowMapper") RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public Mpa postMpa(Mpa mpa) {
        if (!getMpas().contains(mpa)) {
            int id = insert(POST_MPA_QUERY, mpa.getName());
            mpa.setId(id);
            return mpa;
        } else {
            throw new DuplicateDataException("Рейтинг " + mpa.getName() + " уже существует");
        }
    }

    public Mpa getMpa(int mpaId) {
        return findOne(FIND_MPA_QUERY, mpaId).orElseThrow(() -> new NotFoundException("Рейтинг н найден"));
    }

    public List<Mpa> getMpas() {
        return findMany(FIND_MPAS_QUERY);
    }

    public boolean deleteMpa(int mpaId) {
        return delete(DELETE_MPA_QUERY, mpaId);
    }
}
