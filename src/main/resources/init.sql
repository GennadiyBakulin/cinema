create table movie
(
    id          serial PRIMARY KEY,
    name        varchar(20) not null unique,
    description varchar
);

create table place
(
    id   smallserial PRIMARY KEY,
    name varchar(3) not null unique
);

create table session
(
    id       serial PRIMARY KEY,
    movie_id int REFERENCES movie (id),
    datetime timestamp not null,
    price    money     not null check ( cast(price as numeric) >= 0 )
);

create table ticket
(
    id         serial PRIMARY KEY,
    place_id   smallint REFERENCES place (id),
    session_id int REFERENCES session (id)
);