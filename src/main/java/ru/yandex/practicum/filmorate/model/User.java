package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.OnCreate;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;


@Data
@EqualsAndHashCode(of = {"id"})
public class User {
    @PositiveOrZero
    private int id;
    @Email
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    private String email;
    @NotNull(groups = {OnCreate.class})
    @NotBlank(groups = {OnCreate.class})
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private FriendStatus friendStatus;
}
