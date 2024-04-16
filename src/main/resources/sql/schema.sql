CREATE SCHEMA IF NOT EXISTS tartarus;

CREATE TABLE IF NOT EXISTS tartarus.users
(
    cod_user      integer not null
        primary key,
    date_of_birth date,
    email         varchar(255),
    first_name    varchar(255),
    last_name     varchar(255),
    password      varchar(255),
    user_name     varchar(255)
);

