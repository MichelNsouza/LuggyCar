INSERT INTO category (name, description, registration) VALUES
('SUV', 'Veículo esportivo utilitário', '2023-01-10'),
('Sedan', 'Carro de passeio', '2023-02-15'),
('Hatch', 'Veículo compacto', '2023-03-20');

INSERT INTO vehicle (name, manufacturer, version, category_id, url_fipe, plate, color, transmission, current_km, passanger_capacity, trunk_capacity, daily_rate, status_vehicle, registration_date) VALUES
('Carro A', 'TOYOTA', '2021', 1, 'http://fipe-url-a.com', 'ABC1234', 'WHITE', 'AUTOMATIC', 10000, 5, '500L', 150.00, 'AVAILABLE', '2023-04-01'),
('Carro B', 'FORD', '2022', 2, 'http://fipe-url-b.com', 'BBB2222', 'BLACK', 'MANUAL', 20000, 5, '450L', 120.00, 'AVAILABLE', '2023-05-01'),
('Carro C', 'CHEVROLET', '2020', 3, 'http://fipe-url-c.com', 'CCC3333', 'GRAY', 'AUTOMATIC', 30000, 4, '400L', 100.00, 'AVAILABLE', '2023-06-01');

INSERT INTO accident (severity, description, vehicle_id, registration_date) VALUES
('LOW', 'Pequeno arranhão na lateral', 1, '2023-06-15'),
('HIGH', 'Amassado na traseira', 2, '2023-07-20');

INSERT INTO client (person_type, date_birth, cep, endereco, registration, email, cpf, gender, natural_person_name, cnpj, company_name, drivers_license_number, drivers_license_validity) VALUES
('PF', '1990-01-01', '12345-678', 'Rua A, 123', '2023-01-10', 'clienteA@example.com', '111.111.111-11', 'MASCULINO', 'Cliente A', NULL, NULL, '12345678901', '2024-01-01'),
('PF', '1985-05-15', '87654-321', 'Avenida B, 456', '2023-02-20', 'clienteB@example.com', '222.222.222-22', 'FEMININO', 'Cliente B', NULL, NULL, '98765432101', '2025-05-01');

INSERT INTO rent (status, user, client_id, vehicle_id, total_days, security_deposit, start_date, expected_completion_date, finished_date, daily_rate, total_value, total_value_optional_items, km_initial, km_final, accident_id, create_at, update_at)VALUES
('IN_PROGRESS', 'john_doe', 1, 1, 5, 200.00, '2023-11-01', '2023-11-06', NULL, 100.00, 500.00, 50.00, 10000, 10500, NULL, '2023-11-01', '2023-11-01'),
('COMPLETED', 'jane_doe', 2, 2, 7, 300.00, '2023-11-05', '2023-11-12', '2023-11-12', 120.00, 840.00, 80.00, 12000, 12200, 1, '2023-11-05', '2023-11-12');

INSERT INTO restriction_rental (rent_id, restriction_type, description) VALUES
(1, 'VEHICLE_DAMAGED', 'Veículo danificado durante a locação'),
(1, 'OPTIONAL_ITEM_DAMAGED', 'Bebê conforto danificado'),
(2, 'VEHICLE_STOLEN', 'Veículo roubado durante a locação');

INSERT INTO optional_item (name, rental_value, quantity_available, category_id) VALUES
('GPS', 10.00, 5, 1),
('Bebê Conforto', 15.00, 3, 2),
('Wi-Fi', 20.00, 2, 3);








