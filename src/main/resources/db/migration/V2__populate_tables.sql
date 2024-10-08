INSERT INTO accident (severity, description, registration_date) VALUES
('LOW', 'Accidente leve, sem feridos.', NOW()),
('MEDIUM', 'Accidente moderado com alguns feridos.', NOW()),
('HIGH', 'Accidente com danos significativos.', NOW());

INSERT INTO optional_item (name, quantity_available, rental_value) VALUES
('Cadeira', 10, 15.50),
('Mesa', 5, 35.00),
('Projetor', 3, 75.00);
