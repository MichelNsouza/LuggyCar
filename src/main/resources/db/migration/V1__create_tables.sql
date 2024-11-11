CREATE TABLE category (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NULL,
  registration DATE NULL,
  CONSTRAINT pk_category PRIMARY KEY (id),
  CONSTRAINT uc_category_name UNIQUE (name)
);
CREATE TABLE vehicle (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NULL,
  manufacturer ENUM('TOYOTA', 'FORD', 'CHEVROLET', 'HONDA', 'VOLKSWAGEN', 'BMW', 'MERCEDES_BENZ', 'AUDI', 'NISSAN', 'HYUNDAI', 'KIA', 'PEUGEOT', 'FIAT', 'JEEP', 'SUBARU', 'MAZDA', 'PORSCHE', 'VOLVO', 'TESLA', 'FERRARI', 'BYD') NOT NULL,
  version VARCHAR(255) NULL,
  category_id BIGINT NOT NULL,
  url_fipe VARCHAR(255) NULL,
  plate VARCHAR(255) NULL,
  color ENUM('WHITE', 'BLACK', 'GRAY', 'BLUE', 'YELLOW', 'RED', 'GREEN', 'ORANGE', 'PURPLE', 'BROWN', 'PINK', 'SILVER') NULL,
  transmission ENUM('MANUAL', 'AUTOMATIC', 'AUTOMATED') NULL,
  current_km DOUBLE NOT NULL,
  passanger_capacity VARCHAR(255) NULL,
  trunk_capacity VARCHAR(255) NULL,
  daily_rate DOUBLE NOT NULL,
  status_vehicle ENUM('AVAILABLE', 'UNAVAILABLE') NOT NULL,
  registration_date DATE NULL,
  CONSTRAINT pk_vehicle PRIMARY KEY (id),
  CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id)
);
CREATE TABLE accident (
  id BIGINT AUTO_INCREMENT NOT NULL,
  severity ENUM('LOW', 'MEDIUM', 'HIGH') NULL,
  description VARCHAR(255) NOT NULL,
  vehicle_id BIGINT NULL,
  registration_date DATETIME NULL,
  CONSTRAINT pk_accident PRIMARY KEY (id),
  CONSTRAINT fk_accident_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE client (
  id BIGINT AUTO_INCREMENT NOT NULL,
  person_type ENUM('PF', 'PJ') NULL,
  date_birth DATETIME NULL,
  cep VARCHAR(255) NULL,
  endereco VARCHAR(255) NULL,
  registration DATE NULL,
  email VARCHAR(255) NULL,
  cpf VARCHAR(255) NULL,
  gender ENUM('MASCULINO', 'FEMININO') NULL,
  natural_person_name VARCHAR(255) NULL,
  cnpj VARCHAR(255) NULL,
  company_name VARCHAR(255) NULL,
  drivers_license_number VARCHAR(255) NULL,
  drivers_license_validity DATETIME NULL,
  CONSTRAINT pk_client PRIMARY KEY (id)
);
CREATE TABLE rent (
  id BIGINT AUTO_INCREMENT NOT NULL,
  status ENUM('COMPLETED', 'PENDING', 'CANCELED', 'IN_PROGRESS') NULL,
  user VARCHAR(255) NULL,
  client_id BIGINT NULL,
  vehicle_id BIGINT NOT NULL,
  total_days INT NOT NULL,
  security_deposit DOUBLE NULL,
  start_date DATE NULL,
  expected_completion_date DATE NULL,
  finished_date DATE NULL,
  daily_rate DOUBLE NULL,
  total_value DOUBLE NULL,
  total_value_optional_items DOUBLE NULL,
  km_initial DOUBLE NULL,
  km_final DOUBLE NULL,
  accident_id BIGINT NULL,
  create_at DATE NULL,
  update_at DATE NULL,
  CONSTRAINT pk_rent PRIMARY KEY (id),
  CONSTRAINT fk_rent_client FOREIGN KEY (client_id) REFERENCES client(id),
  CONSTRAINT fk_rent_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id),
  CONSTRAINT fk_rent_accident FOREIGN KEY (accident_id) REFERENCES accident(id)
);
CREATE TABLE restriction_rental (
  id BIGINT AUTO_INCREMENT NOT NULL,
  rent_id BIGINT NULL,
  restriction_type ENUM('VEHICLE_DAMAGED', 'VEHICLE_STOLEN', 'VEHICLE_DESTROYED', 'VEHICLE_DETAINED', 'OPTIONAL_ITEM_DAMAGED', 'OPTIONAL_ITEM_LOST_STOLEN', 'OPTIONAL_ITEM_DESTROYED', 'OPTIONAL_ITEM_DETAINED') NOT NULL,
  description VARCHAR(255) NULL,
  CONSTRAINT pk_restrictionrental PRIMARY KEY (id)
);

CREATE TABLE optional_item (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NOT NULL,
  rental_value DOUBLE NOT NULL,
  quantity_available DOUBLE NOT NULL,
  category_id BIGINT NOT NULL,
  CONSTRAINT pk_optionalitem PRIMARY KEY (id),
  CONSTRAINT uc_optionalitem_name UNIQUE (name),
  CONSTRAINT FK_OPTIONALITEM_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id)
);

