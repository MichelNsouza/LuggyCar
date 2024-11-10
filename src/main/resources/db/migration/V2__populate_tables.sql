
INSERT INTO category (name, description, registration) VALUES
('Sedan', 'Carro como um sedan', '2024-01-01'),
('Coupe', 'Carro como um Coupe', '2024-01-02'),
('Hatch', 'Carro como um hatch', '2024-01-03'),
('Cabriolet', 'Carro como um cabriolet', '2024-01-04'),
('Minivan', 'Carro como um minivan', '2024-01-05'),
('SUV', 'Carro como um SUV', '2024-01-06');

INSERT INTO vehicle (
name, manufacturer, version, category_id, url_fipe, plate, color, transmission, current_km,
passanger_capacity, trunk_capacity, daily_rate, accessories, registration_date, status_vehicle)
VALUES
('C200', 'MERCEDES_BENZ', 'AVANTGARD', 1, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12345', 'XPT0G45', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-01', '{"CENTRAL_MULTIMIDIA", "AR_CONDICIONADO_DIGITAL"}', 'ACTIVE'),
('X5', 'BMW', 'M', 6, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12346', 'ABC1D23', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-02', '{"GPS", "AIRBAGS", "VIDROS_ELETRICOS"}', 'INACTIVE'),
('A4', 'AUDI', 'SLINE', 1, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12347', 'LKM3R17', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-03', '{"AIRBAGS", "CENTRAL_MULTIMIDIA"}', 'ACTIVE'),
('PASSAT', 'VOLKSWAGEN', 'VARIANT', 1, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12348', 'RYZ9W83', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-04', '{"GPS", "AQUECIMENTO_BANCOS"}', 'ACTIVE'),
('ESCORT', 'FORD', 'GL', 3, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12350', 'NHD5L81', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-05', '{"VIDROS_ELETRICOS"}', 'INACTIVE'),
('COROLLA', 'TOYOTA', 'XEI', 1, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12351', 'ZFW6V30', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-06', '{"AIRBAGS", "GPS"}', 'ACTIVE'),
('CIVIC', 'HONDA', 'SI', 1, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12352', 'GJH2T54', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-07', '{"AR_CONDICIONADO_DIGITAL", "GPS"}', 'ACTIVE'),
('KICKS', 'NISSAN', 'ACTIVE', 6, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12353', 'KLP4X72', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-08', '{"CENTRAL_MULTIMIDIA", "AIRBAGS"}', 'INACTIVE'),
('OPALA', 'CHEVROLET', 'COMODORO', 1, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12354', 'WST8Q67', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-09', '{"AQUECIMENTO_BANCOS", "VIDROS_ELETRICOS"}', 'ACTIVE'),
('FERRARI 488', 'FERRARI', 'SPIDER', 2, 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12355', 'FERR123', 'RED', 'AUTOMATIC', 0, 2, 0, 500.00, '2024-01-10', '{"GPS", "AIRBAGS"}', 'ACTIVE');


INSERT INTO client (
person_type, date_birth, cep, endereco, registration, email, cpf, gender, natural_person_name, cnpj,
company_name, drivers_license_number, drivers_license_validity, license) VALUES
('PF', '1990-05-20', '12345-678', 'Rua Exemplo, 123', '2024-01-01', 'cliente1@email.com', '123.456.789-00', 'MASCULINO',
 'João da Silva', NULL, NULL, '1234567890', '2025-12-31'),
('PF', '1985-07-15', '23456-789', 'Avenida Exemplo, 456', '2024-01-02', 'cliente2@email.com', '987.654.321-00',
'FEMININO', 'Maria Oliveira', NULL, NULL, '9876543210', '2025-12-31'),
('PJ', NULL, '34567-890', 'Rua Comercial, 789', '2024-01-03', 'empresa1@email.com', NULL, 'MASCULINO',
NULL, '12.345.678/0001-90', 'Empresa Exemplo Ltda', '2345678901', '2025-12-31'),
('PJ', NULL, '45678-901', 'Avenida Corporativa, 101', '2024-01-04', 'empresa2@email.com', NULL, 'FEMININO', NULL, '98.765.432/0001-01', 'Corporation Exemplo S.A.', '1098765432', '2025-12-31');

INSERT INTO driver_license_categories (driver_id, license_category) VALUES
(1, 'B'),
(1, 'A');

INSERT INTO accident (severity, description, vehicle_id, registration_date) VALUES
('LOW', 'Accidente leve, sem feridos.', 1, NOW()),
('MEDIUM', 'Accidente moderado com alguns feridos.', 2, NOW()),
('HIGH', 'Colisão traseira em área urbana', 3, NOW());

INSERT INTO delay_penalty (id, days, percentage, category_id) VALUES
(1, 1, 5.00, 1), -- 5% de penalidade para 1 dia de atraso na categoria 'Sedan'
(2, 3, 10.00, 2), -- 10% de penalidade para 3 dias de atraso na categoria 'SUV'
(3, 5, 15.00, 3); -- 15% de penalidade para 5 dias de atraso na categoria 'Hatch'

INSERT INTO optional_item (name, quantity_available, rental_value) VALUES
('GPS', 15.00, 10),
('Cadeirinha', 25.00, 5),
('Seguro', 50.00, 20),
('Assento Elevado', 20.00, 7);


INSERT INTO rent (
status, user, client_id, vehicle_id, total_days, security_deposit, start_date, expected_completion_date,
finished_date, daily_rate, total_value, total_value_optional_items, km_initial, km_final, accident_id, create_at, update_at)
VALUES(
('PENDING', 'user1', 1, 1, 5, 200.00, '2024-11-10', '2024-11-15', NULL, 100.00, 500.00, 50.00, 0, 100, NULL, '2024-11-10', '2024-11-10'),
('IN_PROGRESS', 'user2', 2, 2, 7, 300.00, '2024-11-05', '2024-11-12', NULL, 150.00, 1050.00, 100.00, 50, 150, NULL, '2024-11-05', '2024-11-05'),
);







