CREATE TABLE IF NOT EXISTS users (
  user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR,
  login VARCHAR NOT NULL,
  email VARCHAR NOT NULL,
  birthday TIMESTAMP WITH TIME ZONE,
  friend_status VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
  user_id INTEGER REFERENCES users(user_id),
  friend_id INTEGER REFERENCES users(user_id),
  CHECK (user_id <> friend_id)
);

CREATE TABLE IF NOT EXISTS films (
  film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL,
  description VARCHAR,
  release_date TIMESTAMP WITH TIME ZONE NOT NULL,
  duration INTEGER NOT NULL,
  genre VARCHAR,
  rating VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS films_likes (
  film_id INTEGER REFERENCES films(film_id),
  user_id INTEGER REFERENCES users(user_id)
);