-- Inserting categories
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Electronics category', 'Electronics');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Clothing category', 'Clothing');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Books category', 'Books');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Home Appliances category', 'Home Appliances');
-- Add more categories as needed
-- Inserting products
INSERT INTO product (id, description, name, avaialable_quantity, price, category_id) VALUES (nextval('product_seq'), 'Smartphone with latest features', 'Smartphone X', 100, 899.99, (SELECT id FROM category WHERE name = 'Electronics'));
INSERT INTO product (id, description, name, avaialable_quantity, price, category_id) VALUES (nextval('product_seq'), 'Men''s casual shirt', 'Casual Shirt', 50, 39.99, (SELECT id FROM category WHERE name = 'Clothing'));
INSERT INTO product (id, description, name, avaialable_quantity, price, category_id) VALUES (nextval('product_seq'), 'Bestseller mystery novel', 'Mystery Novel', 25, 14.99, (SELECT id FROM category WHERE name = 'Books'));
INSERT INTO product (id, description, name, avaialable_quantity, price, category_id) VALUES (nextval('product_seq'), 'High-capacity washing machine', 'Washing Machine XL', 10, 699.99, (SELECT id FROM category WHERE name = 'Home Appliances'));
