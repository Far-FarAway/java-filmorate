package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaStorage mpaStorage;

    @PostMapping
    public Mpa postMpa(@RequestBody Mpa mpa) {
        return mpaStorage.postMpa(mpa);
    }

    @GetMapping
    public List<Mpa> getMpas() {
        return mpaStorage.getMpas();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {
        return mpaStorage.getMpa(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteMpa(@PathVariable int id) {
        return mpaStorage.deleteMpa(id);
    }
}
