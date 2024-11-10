INSERT INTO category (name, description, registration) VALUES
('SUV', 'Veículo esportivo utilitário', '2023-01-10'),
('Sedan', 'Carro de passeio', '2023-02-15'),
('Hatch', 'Veículo compacto', '2023-03-20');

INSERT INTO vehicle (name, manufacturer, version, category_id, url_fipe, plate, color, transmission, current_km, passanger_capacity, trunk_capacity, daily_rate, status_vehicle, registration_date) VALUES
('Carro A', 'Fabricante A', '2021', 1, 'http://fipe-url-a.com', 'AAA-1111', 'Branco', 'Automático', 10000, '5', '500L', 150.00, 1, '2023-04-01'),
('Carro B', 'Fabricante B', '2022', 2, 'http://fipe-url-b.com', 'BBB-2222', 'Preto', 'Manual', 20000, '5', '450L', 120.00, 1, '2023-05-01'),
('Carro C', 'Fabricante C', '2020', 3, 'http://fipe-url-c.com', 'CCC-3333', 'Prata', 'Automático', 30000, '4', '400L', 100.00, 1, '2023-06-01');

INSERT INTO client (person_type, date_birth, cep, endereco, registration, email, cpf, gender, natural_person_name, cnpj, company_name, drivers_license_number, drivers_license_validity) VALUES
('Pessoa Física', '1990-01-01', '12345-678', 'Rua A, 123', '2023-01-10', 'clienteA@example.com', '111.111.111-11', 'Masculino', 'Cliente A', NULL, NULL, '12345678901', '2024-01-01'),
('Pessoa Física', '1985-05-15', '87654-321', 'Avenida B, 456', '2023-02-20', 'clienteB@example.com', '222.222.222-22', 'Feminino', 'Cliente B', NULL, NULL, '98765432101', '2025-05-01');

INSERT INTO optional_item (name, rental_value, quantity_available) VALUES
('GPS', 10.00, 5),
('Bebê Conforto', 15.00, 3),
('Wi-Fi', 20.00, 2);

INSERT INTO accident (severity, description, vehicle_id, registration_date) VALUES
('Leve', 'Pequeno arranhão na lateral', 1, '2023-06-15'),
('Grave', 'Amassado na traseira', 2, '2023-07-20');

INSERT INTO rent (status, user, client_id, vehicle_id, total_days, security_deposit, start_date, expected_completion_date, finished_date, daily_rate, total_value, total_value_optional_items, km_initial, km_final, accident_id, create_at, update_at) VALUES
('Concluído', 'Usuário A', 1, 1, 5, 500.00, '2023-08-01', '2023-08-06', '2023-08-06', 150.00, 750.00, 30.00, 10000, 10200, 1, '2023-08-01', '2023-08-06'),
('Em andamento', 'Usuário B', 2, 2, 3, 300.00, '2023-09-01', '2023-09-04', NULL, 120.00, NULL, 20.00, 20000, NULL, NULL, '2023-09-01', NULL);

INSERT INTO driver_license_categories (driver_id, license_category) VALUES
(1, 'B'),
(2, 'C');

INSERT INTO rent_optional_item (rent_id, optional_item_id, quantity) VALUES
(1, 1, 1),
(1, 2, 1),
(2, 1, 1),
(2, 3, 1);

INSERT INTO restriction_rental (rent_id, restriction_type, description) VALUES
(1, 'Idade mínima', 'Cliente deve ter ao menos 21 anos'),
(1, 'Cartão de crédito', 'Necessário apresentar cartão de crédito válido para caução'),
(2, 'CNH válida', 'Cliente deve ter CNH válida para a categoria do veículo');






