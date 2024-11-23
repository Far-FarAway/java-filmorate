package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.annotation.OnCreate;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;


@Data
public class User {
    @PositiveOrZero
    int id;
    @Email
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    String email;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    String login;
    String name;
    @PastOrPresent
    LocalDate birthday;
    FriendStatus friendStatus;
}
