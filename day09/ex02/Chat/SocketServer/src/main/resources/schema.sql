
CREATE SCHEMA IF NOT EXISTS app;


CREATE TABLE IF NOT EXISTS app.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS app.message (
    id serial PRIMARY KEY,
    message text NOT NULL,
    time timestamp NOT NULL,
    user_id integer NOT NULL,
    room_id integer NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app.users(id),
    FOREIGN KEY (room_id) REFERENCES app.rooms(id)
);
CREATE TABLE IF NOT EXISTS app.users_rooms (
    room_id INTEGER REFERENCES app.rooms(id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES app.users(id) ON DELETE CASCADE,
    PRIMARY KEY (room_id, user_id)
);
