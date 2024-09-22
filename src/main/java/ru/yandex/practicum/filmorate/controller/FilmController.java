package ru.yandex.practicum.filmorate.controller;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate CINEMA_BIRTH = LocalDate.of(1895, 12, 28);
    private static final Logger log = (Logger) LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        log.setLevel(Level.DEBUG);
        log.info("Добавление нового фильма");
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Имя не введено");
            throw new ConditionNotMetException("Имя не долнжно быть пустым");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.warn("описание не введено");
            throw new ConditionNotMetException("Описание не долнжно быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.warn("Длинна описание превышает 200 символов: {}", film.getDescription().length());
            throw new ConditionNotMetException("Длинна описания не должна быть больше 200 символов - " +
                    film.getDescription().length());
        }

        if (film.getReleaseDate() == null || validReleaseDate(film.getReleaseDate())) {
            log.warn("Дата релиза не введена");
            throw new ConditionNotMetException("Дата релиза не долнжна быть пустой");
        }

        if (film.getDuration() != null) {
            if (film.getDuration().isNegative()) {
                log.warn("Продолжительность фмльма отрицательна: {}", film.getDuration());
                throw new ConditionNotMetException("Продолжительность фильма не должна быть меньше нуля");
            }
        } else {
            log.warn("Продолжительность фильма не введена");
            throw new ConditionNotMetException("Продолжительность фильма не должна быть пустой");
        }

        int id = ControllerUtility.getNextId(films.keySet());
        log.debug("Присвоение id {} фильму {}", id, film.getName());
        film.setId(id);
        log.debug("Добавление фильма {} в список фильмов(размер списка {})", film.getName(), films.size());
        films.put(film.getId(), film);

        log.info("Фильм {} с id {} успешно добавлен", film.getName(), film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.setLevel(Level.DEBUG);
        log.info("Обновление данных фильма с id {}", film.getId());
        if (film.getId() < 0 && !films.containsKey(film.getId())) {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден");
        }

        Film oldFilm = films.get(film.getId());

        if (film.getName() != null && !film.getName().isBlank()) {
            log.debug("Замена старого названия фильма {} на новое {}", oldFilm.getName(), film.getName());
            oldFilm.setName(film.getName());
        }

        if (film.getDescription() != null && !film.getDescription().isBlank()) {
            if (film.getDescription().length() > 200) {
                log.warn("Длинна описание превышает 200 символов: {}", film.getDescription().length());
                throw new ConditionNotMetException("Длинна описания не должна быть больше 200 символов");
            } else {
                log.debug("Замена старого описания фильма {} на новое {}",
                        oldFilm.getDescription(), film.getDescription());
                oldFilm.setDescription(film.getDescription());
            }
        }

        if (film.getReleaseDate() != null && validReleaseDate(film.getReleaseDate())) {
            log.debug("Замена старой даты релиза фильма {} на новую {}",
                    oldFilm.getReleaseDate(), film.getReleaseDate());
            oldFilm.setReleaseDate(film.getReleaseDate());
        }

        if (film.getDuration() != null) {
            if (film.getDuration().isPositive()) {
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

    private boolean validReleaseDate(LocalDate date) {
        if (date.isBefore(CINEMA_BIRTH)) {
            return true;
        } else {
            log.warn("Дата релиза фильма раньше {}: {}", CINEMA_BIRTH, date);
            throw new ConditionNotMetException("Дата не может быть раньше 28.12.1985");
        }
    }
}
