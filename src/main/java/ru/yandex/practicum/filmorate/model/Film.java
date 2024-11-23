package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.annotation.OnCreate;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {
    @PositiveOrZero
    int id;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    String name;
    @Size(max = 200)
    String description;
    @NotNull(groups = {OnCreate.class})
    @PastOrPresent
    LocalDate releaseDate;
    @PositiveOrZero
    int duration;
    Set<Integer> likes = new TreeSet<>();
    String genre;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    String rating;
}