package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ControllerUtility;
import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate CINEMA_BIRTH = LocalDate.of(1895, 12, 28);

    @Override
    public Collection<Film> getFilms() {
        log.info("Получение списка фильмов");
        return films.values();
    }

    @Override
    public Film postFilm(Film film) {
        log.info("Добавление нового фильма");

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Имя не введено");
            throw new ConditionNotMetException("Имя не должно быть пустым");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.warn("описание не введено");
            throw new ConditionNotMetException("Описание не должно быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.warn("Длинна описание превышает 200 символов: {}", film.getDescription().length());
            throw new ConditionNotMetException("Длинна описания не должна быть больше 200 символов - " +
                    film.getDescription().length());
        }

        if (film.getReleaseDate() == null) {
            log.warn("Дата релиза не введена");
            throw new ConditionNotMetException("Дата релиза не должна быть пустой");
        } else {
            validReleaseDate(film.getReleaseDate());
        }

        if (film.getDuration() < 0) {
            log.warn("Продолжительность фмльма отрицательна: {}", film.getDuration());
            throw new ConditionNotMetException("Продолжительность фильма не должна быть меньше нуля");
        }

        int id = ControllerUtility.getNextId(films.keySet());
        log.debug("Присвоение id {} фильму {}", id, film.getName());
        film.setId(id);
        log.debug("Добавление фильма {} в список фильмов(размер списка {})", film.getName(), films.size());
        films.put(film.getId(), film);

        log.info("Фильм {} с id {} успешно добавлен", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление данных фильма с id {}", film.getId());
        if (film.getId() < 0 && !films.containsKey(film.getId())) {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден");
        }

        Film oldFilm = films.get(film.getId());

        if (film.getName() != null && !film.getName().isBlank() && !oldFilm.getName().equals(film.getName())) {
            log.debug("Замена старого названия фильма {} на новое {}", oldFilm.getName(), film.getName());
            oldFilm.setName(film.getName());
        }

        if (film.getDescription() != null && !film.getDescription().isBlank() &&
                !oldFilm.getDescription().equals(film.getDescription())) {
            if (film.getDescription().length() > 200) {
                log.warn("Длинна описание превышает 200 символов: {}", film.getDescription().length());
                throw new ConditionNotMetException("Длинна описания не должна быть больше 200 символов");
            } else {
                log.debug("Замена старого описания фильма {} на новое {}",
                        oldFilm.getDescription(), film.getDescription());
                oldFilm.setDescription(film.getDescription());
            }
        }

        if (film.getReleaseDate() != null && !oldFilm.getReleaseDate().equals(film.getReleaseDate()) &&
                validReleaseDate(film.getReleaseDate())) {
            log.debug("Замена старой даты релиза фильма {} на новую {}",
                    oldFilm.getReleaseDate(), film.getReleaseDate());
            oldFilm.setReleaseDate(film.getReleaseDate());
        }

        if (oldFilm.getDuration() != film.getDuration()) {
            if (film.getDuration() >= 0) {
                log.debug("Замена старой продолжительности фильма {} на новую {}",
                        oldFilm.getDuration(), film.getDuration());
                oldFilm.setDuration(film.getDuration());
            } else {
                log.warn("Продолжительность фмльма отрицательна: {}", film.getDuration());
                throw new ConditionNotMetException("Продолжительность фильма не должна быть меньше нуля");
            }
        }


        log.info("Фильм {} с id {} успешно обновлен", oldFilm.getName(), oldFilm.getId());
        return oldFilm;
    }

    @Override
    public Film deleteFilm(int filmId) {
        if (films.containsKey(filmId)) {
            Film deletedFilm = films.get(filmId);
            log.debug("Удаление фильма: {}", deletedFilm);
            films.remove(filmId);

            log.info("Фильм с id {} успешно удален", filmId);
            return deletedFilm;
        } else {
            log.warn("Фильм с id {} не найден", filmId);
            throw new ConditionNotMetException("Фильм с id " + filmId + " не найден");
        }
    }


    private boolean validReleaseDate(LocalDate date) {
        if (date.isAfter(CINEMA_BIRTH)) {
            return true;
        } else {
            log.warn("Дата релиза фильма раньше {}: {}", CINEMA_BIRTH, date);
            throw new ConditionNotMetException("Дата не может быть раньше 28.12.1985");
        }
    }
}
