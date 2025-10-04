-- Insert Pasta Palace
INSERT INTO restaurants (id, owner_id, name, street, number, postal_code, country, contact_email, type, logo)
VALUES ('dab961f7-5441-4827-a55e-7fcbc86a8fb2', '5e66f930-f76e-476a-a1be-20e1ceccdf57', 'Pasta Palace', 'Cederlaan', 35,
        2600, 'Belgium', 'pasta@example.com', 'ITALIAN', 'pasta-logo.png');

-- Insert dishes for Pasta Palace
INSERT INTO dishes (id, restaurant_id, name, description, state)
VALUES ('27756a48-0b85-4499-aadb-c08a97943263', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'Spaghetti Bolognese',
        'Spaghetti with Bolognese sauce', 'PUBLISHED'),
       ('6c273939-faf5-4eca-988d-08b14533849e', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'Fettuccine Alfredo',
        'Creamy Alfredo sauce with fettuccine pasta', 'PUBLISHED'),
       ('98be5020-eb67-49e0-815e-3b2f7e1808e0', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'Penne Arrabbiata',
        'Penne pasta with spicy tomato sauce', 'NOT_PUBLISHED');

-- Insert Sushi World
INSERT INTO restaurants (id, owner_id, name, street, number, postal_code, country, contact_email, type, logo)
VALUES ('7f3037c6-a58d-402b-bcfc-def8157fdbfc', '8d12f930-f76e-476a-a1be-20e1ceccdf99', 'Sushi World', 'Kapellestraat',
        12, 1000, 'Belgium', 'sushi@example.com', 'JAPANESE', 'sushi-logo.png');

-- Insert dishes for Sushi World
INSERT INTO dishes (id, restaurant_id, name, description, state)
VALUES ('18cb4eec-8ccb-4f65-a20a-dced4e1b0e4b', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'California Roll',
        'Crab, avocado, and cucumber roll', 'PUBLISHED'),
       ('3c69651d-9afb-48ea-8efc-242054e9ed63', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'Salmon Nigiri',
        'Fresh salmon over rice', 'NOT_AVAILABLE'),
       ('c1010a78-a6bb-44b6-8569-6b2fc33f3d31', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'Tempura Udon',
        'Udon noodles with tempura shrimp', 'NOT_PUBLISHED');
