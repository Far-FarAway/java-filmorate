package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FriendList {
    private User user;
    private User friend;
}
