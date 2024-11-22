
CREATE TABLE accident (
  id BIGINT AUTO_INCREMENT NOT NULL,
  severity ENUM('LOW', 'MEDIUM', 'HIGH') NULL,
  description VARCHAR(255) NOT NULL,
  vehicle_id BIGINT NULL,
  registration_date DATETIME NULL,
  CONSTRAINT pk_accident PRIMARY KEY (id),
  CONSTRAINT fk_accident_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE optional_item (
  id BIGINT AUTO_INCREMENT NOT NULL,
  name VARCHAR(255) NOT NULL,
  rental_value DOUBLE NOT NULL,
  quantity_available DOUBLE NOT NULL,
  CONSTRAINT pk_optionalitem PRIMARY KEY (id),
  CONSTRAINT uc_optionalitem_name UNIQUE (name)
);

