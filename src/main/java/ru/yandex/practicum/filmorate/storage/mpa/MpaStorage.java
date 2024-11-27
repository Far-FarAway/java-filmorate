package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa postMpa(Mpa mpa);

    Mpa getMpa(int mpaId);

    List<Mpa> getMpas();

    boolean deleteMpa(int mpaId);
}
