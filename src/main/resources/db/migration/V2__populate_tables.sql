INSERT INTO accident (severity, description, registration_date) VALUES
('LOW', 'Accidente leve, sem feridos.', NOW()),
('MEDIUM', 'Accidente moderado com alguns feridos.', NOW()),
('HIGH', 'Accidente com danos significativos.', NOW());

INSERT INTO optional_item (name, quantity_available, rental_value) VALUES
('Cadeira', 10, 15.50),
('Mesa', 5, 35.00),
('Projetor', 3, 75.00);

INSERT INTO category (name, description, image, registration) VALUES
('Sedan', 'Carro como um sedan', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD', '2024-01-01'),
('Coupe', 'Carro como um Coupe', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD', '2024-01-02'),
('Hatch', 'Carro como um hatch', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD', '2024-01-03'),
('Cabriolet', 'Carro como um cabriolet', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD', '2024-01-04'),
('Minivan', 'Carro como um minivan', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD', '2024-01-05'),
('SUV', 'Carro como um SUV', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD', '2024-01-06');

INSERT INTO vehicle (name, manufacturer, version, category_id, url_fipe, plate, color, transmission, current_km, passanger_capacity, trunk_capacity, daily_rate, registration_date) VALUES
('C200', 'MERCEDES_BENZ', 'AVANTGARD', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12345', 'XPT0G45', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-01'),
('X5', 'BMW', 'M', '6', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12346', 'ABC1D23', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-02'),
('A4', 'AUDI', 'SLINE', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12347', 'LKM3R17', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-03'),
('PASSAT', 'VOLKSWAGEN', 'VARIANT', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12348', 'RYZ9W83', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-04'),
('ESCORT', 'FORD', 'GL', '3', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12350', 'NHD5L81', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-05'),
('COROLLA', 'TOYOTA', 'XEI', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12351', 'ZFW6V30', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-06'),
('CIVIC', 'HONDA', 'SI', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12352', 'GJH2T54', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-07'),
('KICKS', 'NISSAN', 'ACTIVE', '6', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12353', 'KLP4X72', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-08'),
('OPALA', 'CHEVROLET', 'COMODORO', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12354', 'WST8Q67', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00, '2024-01-09'),
('FERRARI 488', 'FERRARI', 'SPIDER', '2', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12355', 'FERR123', 'RED', 'AUTOMATIC', 0, 2, 0, 500.00, '2024-01-10');


INSERT INTO client (person_type, natural_person_name, cpf, email, gender, date_birth, cep, endereco, registration) VALUES
('PF', 'Ana Paula Souza', '123.456.789-10', 'ana.souza@email.com', 'FEMININO', '1990-08-15 00:00:00', '12345-678', 'Rua das Flores, 123, Bairro Jardim, Cidade A', '2024-01-20'),
('PF', 'Carlos Eduardo Lima', '987.654.321-00', 'carlos.lima@email.com', 'MASCULINO', '1985-05-30 00:00:00', '54321-987', 'Avenida Central, 456, Bairro Centro, Cidade B', '2024-02-15');

INSERT INTO client (person_type, company_name, cnpj, email, cep, endereco, registration) VALUES
('PJ', 'Loja XYZ Ltda', '12.345.678/0001-90', 'contato@lojaxyz.com', '78945-123', 'Rua Comercial, 789, Bairro Industrial, Cidade C', '2024-03-10'),
('PJ', 'Construtora ABC S.A.', '98.765.432/0001-55', 'contato@abcconstrutora.com', '65432-098', 'Avenida das Empresas, 321, Cidade D', '2024-04-22');

INSERT INTO rent (daily_rate, total_days, deposit, km_initial, km_final, create_at, update_at, user, client_id, vehicle_id, status) VALUES
(100, 7, 500, 10000, 10500, '2024-01-10', '2024-01-10', 'usuario1', 1, 1, 'COMPLETED'),
(120, 5, 600, 12000, 12500, '2024-02-15', '2024-02-15', 'usuario2', 2, 2, 'PENDING'),
(150, 3, 700, 13000, 13200, '2024-03-20', '2024-03-20', 'usuario3', 3, 3, 'IN_PROGRESS'),
(180, 10, 800, 14000, 14500, '2024-04-12', '2024-04-12', 'usuario4', 4, 4, 'COMPLETED'),
(160, 12, 500, 15000, 15600, '2024-05-08', '2024-05-08', 'usuario5', 1, 5, 'CANCELED');






