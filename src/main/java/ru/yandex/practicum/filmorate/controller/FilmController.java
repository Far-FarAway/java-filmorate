package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.yandex.practicum.filmorate.annotation.OnCreate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikesStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    FilmStorage filmStorage;
    FilmsLikesStorage likesStorage;
    FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmStorage.findById(id);
    }

    @GetMapping("/likes")
    public Map<String, List<String>> getLikes() {
        return filmService.getLikes(likesStorage);
    }

    @GetMapping("/likes/{filmId}")
    public Map<String, List<String>> getLikes(@PathVariable int filmId) {
        return filmService.getLikesByFilm(filmId, likesStorage);
    }

    @GetMapping("/popular")
    public Set<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count, filmStorage, likesStorage);
    }

    @PostMapping
    @Validated({OnCreate.class})
    public Film postFilm(@Valid @RequestBody Film film) {
        return filmStorage.postFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public int addLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.addLike(filmId, userId, likesStorage);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{filmId}")
    public boolean deleteFilm(@PathVariable int filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public boolean deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.deleteLike(filmId, userId, likesStorage);
    }

    @GetMapping("/genres/{id}")
    public Set<Genre> getGenreByFilm(@PathVariable int id) {
        return filmService.getGenreByFilm(id, filmStorage);
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaByFilm(@PathVariable int id) {
        return filmService.getMpaByFilm(id, filmStorage);
    }
}
