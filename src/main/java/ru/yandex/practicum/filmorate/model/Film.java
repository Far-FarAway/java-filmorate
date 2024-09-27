package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.annotation.OnCreate;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    @PositiveOrZero
    int id;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    String name;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    @Size(max = 200)
    String description;
    @NotNull(groups = {OnCreate.class})
    @PastOrPresent
    LocalDate releaseDate;
    @PositiveOrZero
    int duration;
}