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
    plate  VARCHAR(255) UNIQUE NULL,
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


CREATE TABLE client (
  id BIGINT AUTO_INCREMENT NOT NULL,
   person_type VARCHAR(255) NOT NULL,
   natural_person_name VARCHAR(255) NULL,
   cpf VARCHAR(255) NULL,
   cnpj VARCHAR(255) NULL,
   company_name VARCHAR(255) NULL,
   email VARCHAR(255) NULL,
   gender VARCHAR(255) NULL,
   date_birth datetime NULL,
   cep VARCHAR(255) NULL,
   endereco VARCHAR(255) NULL,
   registration date NULL,
   CONSTRAINT pk_client PRIMARY KEY (id)
);
CREATE TABLE rent (
  id BIGINT AUTO_INCREMENT NOT NULL,
   daily_rate DECIMAL NULL,
   total_days INT NOT NULL,
   deposit DECIMAL NULL,
   km_initial DECIMAL NULL,
   km_final DECIMAL NULL,
   registration date NULL,
   user VARCHAR(255) NULL,
   client_id BIGINT NULL,
   vehicle_id BIGINT NULL,
   status VARCHAR(255) NULL,
   CONSTRAINT pk_rent PRIMARY KEY (id)
);

ALTER TABLE rent ADD CONSTRAINT uc_rent_vehicle UNIQUE (vehicle_id);

ALTER TABLE rent ADD CONSTRAINT FK_RENT_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client (id);

ALTER TABLE rent ADD CONSTRAINT FK_RENT_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id);