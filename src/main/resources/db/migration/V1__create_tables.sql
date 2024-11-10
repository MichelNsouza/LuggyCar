
CREATE TABLE category (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   `description` VARCHAR(255) NULL,
   registration date NULL,
   CONSTRAINT pk_category PRIMARY KEY (id)
);

ALTER TABLE category ADD CONSTRAINT uc_category_name UNIQUE (name);

CREATE TABLE vehicle (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NULL,
   manufacturer VARCHAR(255) NULL,
   version VARCHAR(255) NULL,
   category_id BIGINT NOT NULL,
   url_fipe VARCHAR(255) NULL,
   plate VARCHAR(255) NULL,
   color VARCHAR(255) NULL,
   transmission VARCHAR(255) NULL,
   current_km DOUBLE NOT NULL,
   passanger_capacity VARCHAR(255) NULL,
   trunk_capacity VARCHAR(255) NULL,
    accessories VARCHAR(255) NULL,
   daily_rate DOUBLE NOT NULL,
   status_vehicle SMALLINT NULL,
   registration_date date NULL,
   CONSTRAINT pk_vehicle PRIMARY KEY (id)
);

ALTER TABLE vehicle ADD CONSTRAINT FK_VEHICLE_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);


CREATE TABLE client (
  id BIGINT AUTO_INCREMENT NOT NULL,
   person_type VARCHAR(255) NULL,
   date_birth datetime NULL,
   cep VARCHAR(255) NULL,
   endereco VARCHAR(255) NULL,
   registration date NULL,
   email VARCHAR(255) NULL,
   cpf VARCHAR(255) NULL,
   gender VARCHAR(255) NULL,
   natural_person_name VARCHAR(255) NULL,
   cnpj VARCHAR(255) NULL,
   company_name VARCHAR(255) NULL,
   drivers_license_number VARCHAR(255) NULL,
   drivers_license_validity datetime NULL,
   CONSTRAINT pk_client PRIMARY KEY (id)
);

CREATE TABLE driver_license_categories (
  driver_id BIGINT NOT NULL,
   license_category VARCHAR(255) NULL
);

ALTER TABLE driver_license_categories ADD CONSTRAINT fk_driver_license_categories_on_client FOREIGN KEY (driver_id) REFERENCES client (id);

CREATE TABLE accident (
  id BIGINT AUTO_INCREMENT NOT NULL,
   severity VARCHAR(255) NULL,
   `description` VARCHAR(255) NOT NULL,
   vehicle_id BIGINT NULL,
   registration_date datetime NULL,
   CONSTRAINT pk_accident PRIMARY KEY (id)
);

ALTER TABLE accident ADD CONSTRAINT uc_accident_description UNIQUE (`description`);

ALTER TABLE accident ADD CONSTRAINT FK_ACCIDENT_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id);


CREATE TABLE optional_item (
  id BIGINT AUTO_INCREMENT NOT NULL,
   name VARCHAR(255) NOT NULL,
   rental_value DOUBLE NOT NULL,
   quantity_available DOUBLE NOT NULL,
   CONSTRAINT pk_optionalitem PRIMARY KEY (id)
);

ALTER TABLE optional_item ADD CONSTRAINT uc_optionalitem_name UNIQUE (name);


CREATE TABLE delay_penalty (
  id BIGINT NOT NULL,
   days INT NULL,
   percentage DOUBLE NULL,
   category_id BIGINT NULL,
   CONSTRAINT pk_delaypenalty PRIMARY KEY (id)
);

ALTER TABLE delay_penalty ADD CONSTRAINT FK_DELAYPENALTY_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

CREATE TABLE rent (
  id BIGINT AUTO_INCREMENT NOT NULL,
   status VARCHAR(255) NULL,
   user VARCHAR(255) NULL,
   client_id BIGINT NULL,
   vehicle_id BIGINT NOT NULL,
   total_days INT NOT NULL,
   security_deposit DOUBLE NULL,
   start_date date NULL,
   expected_completion_date date NULL,
   finished_date date NULL,
   daily_rate DOUBLE NULL,
   total_value DOUBLE NULL,
   total_value_optional_items DOUBLE NULL,
   km_initial DOUBLE NULL,
   km_final DOUBLE NULL,
   accident_id BIGINT NULL,
   create_at date NULL,
   update_at date NULL,
   CONSTRAINT pk_rent PRIMARY KEY (id)
);

ALTER TABLE rent ADD CONSTRAINT FK_RENT_ON_ACCIDENT FOREIGN KEY (accident_id) REFERENCES accident (id);

ALTER TABLE rent ADD CONSTRAINT FK_RENT_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client (id);

ALTER TABLE rent ADD CONSTRAINT FK_RENT_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id);