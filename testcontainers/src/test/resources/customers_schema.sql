DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    sur_name VARCHAR(128) NOT NULL,
    details VARCHAR(128) NOT NULL
);
