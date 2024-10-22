INSERT INTO accident (severity, description, registration_date) VALUES
('LOW', 'Accidente leve, sem feridos.', NOW()),
('MEDIUM', 'Accidente moderado com alguns feridos.', NOW()),
('HIGH', 'Accidente com danos significativos.', NOW());

INSERT INTO optional_item (name, quantity_available, rental_value) VALUES
('Cadeira', 10, 15.50),
('Mesa', 5, 35.00),
('Projetor', 3, 75.00);

INSERT INTO CATEGORY (NAME, DESCRIPTION, IMAGE) VALUES
('Sedan', 'Carro como um sedão', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD'),
('Coupe', 'Carro como um Coupe', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD'),
('Hatch', 'Carro como um hatch', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD'),
('Cabriolet', 'Carro como um cabriolet', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD'),
('Minivan', 'Carro como um minivan', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD'),
('SUV', 'Carro como um SUV', 'https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.google.com%2Fsearch%2Fcss%2Fhome.css&psig=AOvVaw2-3-7-0-1-6-2w-4-5&ust=1675029628972000&source=images&cd=vfe&ved=0CAIQjRxqFwoTCNDr4n8w4oVVwAAAAAdAAAAABAD');

INSERT INTO vehicle (name, manufacturer, version, category_id, url_fipe, plate, color, transmission, current_km, passanger_capacity, trunk_capacity, daily_rate) VALUES
('C200', 'MERCEDES', 'AVANTGARD', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12345', 'XPT0G45',  'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00),
('X5', 'BMW', 'M', '6', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12346', 'ABC1D23', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00),
('A4', 'AUDI', 'SLINE', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12347', 'LKM3R17', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00),
('PASSAT', 'VOLKSWAGEN', 'VARIANT', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12348', 'RYZ9W83', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00),
('CLIO', 'RENAULT', 'RL', '3', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12349', 'TQR7B92', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00),
('ESCORT', 'FORD', 'GL', '3', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12350', 'NHD5L81', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00),
('COROLLA', 'TOYOTA', 'XEI', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12351', 'ZFW6V30', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00),
('CIVIC', 'HONDA', 'SI', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12352', 'GJH2T54', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00),
('KICKS', 'NISSAN', 'ACTIVE', '6', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12353', 'KLP4X72', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00),
('OPALA', 'CHEVROLET', 'COMODORO', '1', 'https://www.carrosnaweb.com.br/fichadetalhe.asp?codigo=12354', 'WST8Q67', 'BLUE', 'AUTOMATIC', 0, 4, 0, 100.00);


