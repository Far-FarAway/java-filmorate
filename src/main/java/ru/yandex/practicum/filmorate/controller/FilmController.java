package ru.yandex.practicum.filmorate.controller;

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
import java.util.Random;

import ru.yandex.practicum.filmorate.exception.ConditionNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.ControllerUtility;

@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate CINEMA_BIRTH = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ConditionNotMetException("Имя не долнжно быть пустым");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw new ConditionNotMetException("Описание не долнжно быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ConditionNotMetException("Длинна описания не должна быть больше 200 символов");
        }

        if (film.getReleaseDate() == null || validReleaseDate(film.getReleaseDate())) {
            throw new ConditionNotMetException("Дата релиза не долнжна быть пустой");
        }

        if (film.getDuration() != null) {
            if (film.getDuration().isNegative()) {
                throw new ConditionNotMetException("Продолжительность фильма не должна быть меньше нуля");
            }
        } else {
            throw new ConditionNotMetException("Продолжительность не должна быть пустой");
        }

        film.setId(ControllerUtility.getNextId(films.keySet()));
        films.put(film.getId(), film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getId() < 0 && !films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с ID " + film.getId() + " не найден");
        }

        Film oldFilm = films.get(film.getId());

        if (film.getName() != null && !film.getName().isBlank()) {
            oldFilm.setName(film.getName());
        }

        if (film.getDescription() != null && !film.getDescription().isBlank()) {
            if (film.getDescription().length() > 200) {
                throw new ConditionNotMetException("Длинна описания не должна быть больше 200 символов");
            } else {
                oldFilm.setDescription(film.getDescription());
            }
        }

        if (film.getReleaseDate() != null && validReleaseDate(film.getReleaseDate())) {
            oldFilm.setReleaseDate(film.getReleaseDate());
        }

        if (film.getDuration() != null) {
            if (film.getDuration().isPositive()) {
                oldFilm.setDuration(film.getDuration());
            } else {
                throw new ConditionNotMetException("Продолжительность фильма не должна быть меньше нуля");
            }
        }

        return oldFilm;
    }

    private boolean validReleaseDate(LocalDate date) {
        if (date.isBefore(CINEMA_BIRTH)) {
            return true;
        } else {
            throw new ConditionNotMetException("Дата не может быть раньше 28.12.1985");
        }
    }
}
