
CREATE TABLE accident (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    severity VARCHAR(120) NOT NULL,
    description TEXT,
    registration_date DATETIME NOT NULL
);