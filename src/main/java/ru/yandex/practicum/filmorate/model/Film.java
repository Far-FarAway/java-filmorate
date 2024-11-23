package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.annotation.OnCreate;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    Instant releaseDate;
    @PositiveOrZero
    int duration;
    List<User> likes = new ArrayList<>();
    String genre;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    String rating;
}