package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import ru.yandex.practicum.filmorate.annotation.OnCreate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmDbService;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
@Validated
public class FilmController {
    private final FilmDbService filmService;

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/likes")
    public Map<String, List<String>> getLikes() {
        return filmService.getLikes();
    }

    @GetMapping("/likes/{filmId}")
    public Map<String, List<String>> getLikes(@PathVariable int filmId) {
        return filmService.getLikesByFilm(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film postFilm(@Validated({OnCreate.class, Default.class}) @RequestBody Film film) {
        return filmService.postFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public int addLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.addLike(filmId, userId);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{filmId}")
    public boolean deleteFilm(@PathVariable int filmId) {
        return filmService.deleteFilm(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public boolean deleteLike(@PathVariable int filmId, @PathVariable int userId) {
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/genres/{id}")
    public List<Genre> getGenreByFilm(@PathVariable int id) {
        return filmService.getGenreByFilm(id);
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaByFilm(@PathVariable int id) {
        return filmService.getMpaByFilm(id);
    }
}
