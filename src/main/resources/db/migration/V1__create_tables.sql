
CREATE TABLE accident (
    id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    severity ENUM('HIGH', 'LOW', 'MEDIUM'),
    description VARCHAR(250),
    registration_date DATETIME NOT NULL
);

CREATE TABLE optional (
        id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
        name VARCHAR(255),
        quantity_available FLOAT(53) NOT NULL,
        rental_value FLOAT(53) NOT NULL,
 );