package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import ru.yandex.practicum.filmorate.annotation.OnCreate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private UserStorage userStorage;
    private FilmStorage filmStorage;
    private FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count, filmStorage);
    }

    @PostMapping
    @Validated({OnCreate.class})
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmStorage.postFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Collection<Integer> addLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.addLike(filmId, userId, userStorage, filmStorage);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping("/{filmId}")
    public Film deleteFilm(@PathVariable int filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Collection<Integer> deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.deleteLike(filmId, userId, filmStorage);
    }
}
