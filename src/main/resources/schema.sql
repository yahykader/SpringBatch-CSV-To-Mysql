DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id INT auto_increment NOT NULL PRIMARY KEY,
    first_name VARCHAR(40),
    last_name VARCHAR(40),
    email VARCHAR(100),
    age INT
);