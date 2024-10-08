CREATE TABLE accident (
  id BIGINT AUTO_INCREMENT NOT NULL,
   severity VARCHAR(255) NULL,
   `description` VARCHAR(255) NULL,
   registration_date datetime NULL,
   CONSTRAINT pk_accident PRIMARY KEY (id)
);

CREATE TABLE optional_item (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   rental_value DOUBLE NOT NULL,
   quantity_available DOUBLE NOT NULL,
   CONSTRAINT pk_optionalitem PRIMARY KEY (id)
);