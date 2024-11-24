package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.yandex.practicum.filmorate.annotation.OnCreate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmslikes.FilmsLikes;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    FilmStorage filmStorage;
    FilmsLikes likesStorage;
    FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/likes")
    public Map<String, List<String>> getLikes() {
        return likesStorage.getLikes();
    }

    @GetMapping("/likes/{filmId}")
    public Map<String, List<String>> getLikes(@PathVariable int filmId) {
        return likesStorage.getLikesByFilm(filmId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count, filmStorage, likesStorage);
    }

    @PostMapping
    @Validated({OnCreate.class})
    public boolean postFilm(@Valid @RequestBody Film film) {
        return filmStorage.postFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public boolean addLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.addLike(filmId, userId, likesStorage);
    }

    @PutMapping
    public boolean updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping("/{filmId}")
    public boolean deleteFilm(@PathVariable int filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public boolean deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.deleteLike(filmId, userId, likesStorage);
    }
}
