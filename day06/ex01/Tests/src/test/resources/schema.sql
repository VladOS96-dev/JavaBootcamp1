CREATE SCHEMA if NOT EXISTS  electronic;

CREATE TABLE if NOT EXISTS  electronic.product (
    identifier INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL
);