DROP SCHEMA IF EXISTS demo;

CREATE SCHEMA IF NOT EXISTS demo;

USE demo;

CREATE TABLE IF NOT EXISTS user
(
	id int PRIMARY KEY AUTO_INCREMENT,
	username varchar(255),
	password varchar(45),
	salt BLOB,
	email varchar(45)
);
