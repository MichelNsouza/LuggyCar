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

CREATE TABLE category
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    description VARCHAR(255) NULL,
    image VARCHAR(255) NULL,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

ALTER TABLE category
    ADD CONSTRAINT uc_category_name UNIQUE (name);

CREATE TABLE vehicle
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NULL,
    manufacturer VARCHAR(255) NULL,
    version VARCHAR(255) NULL,
    category_id BIGINT NOT NULL,
    url_fipe VARCHAR(255) NULL,
    plate VARCHAR(255) NULL,
    color VARCHAR(255) NULL,
    transmission VARCHAR(255) NULL,
    current_km VARCHAR(255) NULL,
    passanger_capacity VARCHAR(255) NULL,
    trunk_capacity VARCHAR(255) NULL,
    daily_rate DOUBLE NOT NULL,
    registration_date  date NULL,
    CONSTRAINT pk_vehicle PRIMARY KEY (id)
);

ALTER TABLE vehicle
    ADD CONSTRAINT FK_VEHICLE_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);