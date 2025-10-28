-- Insert Pasta Palace
INSERT INTO restaurants (id, owner_id, name, street, number, postal_code, country, contact_email, type, logo, override_status)
VALUES ('dab961f7-5441-4827-a55e-7fcbc86a8fb2', '803afbdd-a7dc-4735-bbf4-526d99cbf686', 'Pasta Palace', 'Cederlaan', 35,
        2600, 'Belgium', 'pasta@example.com', 'ITALIAN', 'pasta-logo.png','NONE');

-- Insert dishes for Pasta Palace
INSERT INTO dishes (id, restaurant_id, name, description, state, price)
VALUES ('27756a48-0b85-4499-aadb-c08a97943263', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'Spaghetti Bolognese',
        'Spaghetti with Bolognese sauce', 'PUBLISHED',14.15),
       ('6c273939-faf5-4eca-988d-08b14533849e', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'Fettuccine Alfredo',
        'Creamy Alfredo sauce with fettuccine pasta', 'PUBLISHED', 12.13),
       ('98be5020-eb67-49e0-815e-3b2f7e1808e0', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'Penne Arrabbiata',
        'Penne pasta with spicy tomato sauce', 'NOT_PUBLISHED',11.12);

-- Opening hours for Pasta Palace
INSERT INTO opening_hours (id, restaurant_id, day, opening_time, closing_time)
VALUES ('e8f2ab29-5a78-4f16-8a36-23fca9fdbd21', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'MONDAY', '11:30', '22:00'),
       ('f3e79dc3-8e11-4a7e-98b0-55a63e7cbf32', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'WEDNESDAY', '11:30', '22:00'),
       ('d4e69a85-1293-4f28-a9c9-3b6f88f9d413', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'THURSDAY', '11:30', '22:00'),
       ('c7a39ef0-6574-4c02-a22e-4a48e9fa8c15', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'FRIDAY', '11:30', '22:00'),
       ('a3b1c78d-8de4-4f8e-9a59-1b4b79b2d613', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'SATURDAY', '11:30', '22:00'),
       ('b4f2d915-3c24-4d8f-b7a7-8c26f9ea5a42', 'dab961f7-5441-4827-a55e-7fcbc86a8fb2', 'SUNDAY', '11:30', '21:00');

-- Insert Sushi World
INSERT INTO restaurants (id, owner_id, name, street, number, postal_code, country, contact_email, type, logo, override_status )
VALUES ('7f3037c6-a58d-402b-bcfc-def8157fdbfc', '82090ea4-05d5-4d95-97de-e33760ac73f9', 'Sushi World', 'Kapellestraat',
        12, 1000, 'Belgium', 'sushi@example.com', 'JAPANESE', 'sushi-logo.png', 'NONE');

-- Insert dishes for Sushi World
INSERT INTO dishes (id, restaurant_id, name, description, state, price)
VALUES ('18cb4eec-8ccb-4f65-a20a-dced4e1b0e4b', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'California Roll',
        'Crab, avocado, and cucumber roll', 'PUBLISHED', 6.7),
       ('3c69651d-9afb-48ea-8efc-242054e9ed63', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'Salmon Nigiri',
        'Fresh salmon over rice', 'NOT_AVAILABLE', 3.4),
       ('c1010a78-a6bb-44b6-8569-6b2fc33f3d31', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'Tempura Udon',
        'Udon noodles with tempura shrimp', 'NOT_PUBLISHED', 18.80);

-- Opening hours for Sushi World
INSERT INTO opening_hours (id, restaurant_id, day, opening_time, closing_time)
VALUES ('f6c0e1d3-52b8-4cb9-99cf-54b3a0e9a87f', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'TUESDAY', '12:00', '21:30'),
       ('c9a1f6b4-56a4-4d5f-982e-3e73cba7c512', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'WEDNESDAY', '12:00', '21:30'),
       ('b2d9a4c8-2c75-48d2-8f87-9b4a7dc5b712', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'THURSDAY', '12:00', '21:30'),
       ('a6e1f2c3-3f17-49b4-94e5-8f7b1c9a4e90', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'FRIDAY', '12:00', '22:00'),
       ('d3c2b4a7-62f8-4f52-8a9b-1c3e7e9a5c10', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'SATURDAY', '12:00', '22:00'),
       ('e1a8c5b9-71e6-4e29-8d12-7c8b5e9f3a20', '7f3037c6-a58d-402b-bcfc-def8157fdbfc', 'SUNDAY', '12:00', '21:00');

INSERT INTO restaurants (id, owner_id, name, street, number, postal_code, country, contact_email, type, logo, override_status)
VALUES ('11111111-2222-3333-4444-555555555555', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee',
        '24/7 Grill', 'Main Street', 1, 9000, 'Belgium', '247@example.com',
        'AMERICAN', '247-logo.png', 'NONE');

-- Insert dishes for 24/7 Grill
INSERT INTO dishes (id, restaurant_id, name, description, state, price)
VALUES ('aa111111-2222-3333-4444-555555555551', '11111111-2222-3333-4444-555555555555',
        'Classic Cheeseburger', 'Juicy beef patty with cheddar cheese', 'PUBLISHED', 9.99),
       ('aa111111-2222-3333-4444-555555555552', '11111111-2222-3333-4444-555555555555',
        'Grillmaster Ribs', 'Slow-cooked BBQ ribs with special sauce', 'PUBLISHED', 17.49),
       ('aa111111-2222-3333-4444-555555555553', '11111111-2222-3333-4444-555555555555',
        'Chicken Wings', 'Crispy wings with a choice of sauces', 'PUBLISHED', 8.50),
       ('aa111111-2222-3333-4444-555555555554', '11111111-2222-3333-4444-555555555555',
        'Midnight Fries', 'Golden Belgian fries served any time', 'PUBLISHED', 4.25),
       ('aa111111-2222-3333-4444-555555555555', '11111111-2222-3333-4444-555555555555',
        'Pancake Stack', 'Fluffy pancakes with syrup — breakfast all day!', 'PUBLISHED', 6.75);