CREATE TABLE users (
    `id` INT(10) AUTO_INCREMENT PRIMARY KEY,
    `login` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) NOT NULL
);