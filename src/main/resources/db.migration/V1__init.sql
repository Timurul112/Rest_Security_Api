CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(128) NOT NULL,
    password VARCHAR(256) NOT NULL,
    role     VARCHAR(50)  NOT NULL,
    status   VARCHAR(25) DEFAULT 'ACTIVE'
);

CREATE TABLE files
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(128) NOT NULL,
    location VARCHAR(128) NOT NULL,
    status   VARCHAR(128) NOT NULL DEFAULT 'SAVED'
);

CREATE TABLE events
(
    id      SERIAL PRIMARY KEY,
    status  VARCHAR(128) NOT NULL DEFAULT 'ACTIVE',
    user_id INTEGER      NOT NULL REFERENCES users (id),
    file_id INTEGER      NOT NULL REFERENCES files (id)
);

