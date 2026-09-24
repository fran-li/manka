-- Manka catalog seed.
-- Intentionally does NOT insert users, history, pantry or favorites.
-- Those tables start empty and are filled through the API.

INSERT INTO protein_categories (id, name) VALUES
(1, 'Pollo'),
(2, 'Res'),
(3, 'Pescado'),
(4, 'Vegetariano')
ON CONFLICT DO NOTHING;

INSERT INTO ingredients (id, name, parent_id) VALUES
(1,  'Pollo', NULL),
(2,  'Pechuga de pollo', 1),
(3,  'Pierna de pollo', 1),
(4,  'Arroz', NULL),
(5,  'Huevo', NULL),
(6,  'Cebolla china', NULL),
(7,  'Sillao', NULL),
(8,  'Aceite vegetal', NULL),
(9,  'Carne de res', NULL),
(10, 'Cebolla roja', NULL),
(11, 'Tomate', NULL),
(12, 'Papa amarilla', NULL),
(13, 'Ají amarillo', NULL),
(14, 'Leche evaporada', NULL),
(15, 'Pan', NULL),
(16, 'Queso fresco', NULL),
(17, 'Galleta soda', NULL),
(18, 'Pescado', NULL),
(19, 'Limón', NULL),
(20, 'Ajo', NULL),
(21, 'Culantro', NULL),
(22, 'Frejol canario', NULL),
(23, 'Tallarín', NULL),
(24, 'Albahaca', NULL),
(25, 'Espinaca', NULL),
(26, 'Nueces', NULL),
(27, 'Queso parmesano', NULL),
(28, 'Zanahoria', NULL),
(29, 'Arveja', NULL),
(30, 'Pimiento', NULL),
(31, 'Choclo', NULL),
(32, 'Mayonesa', NULL),
(33, 'Palta', NULL),
(34, 'Harina', NULL),
(35, 'Caldo de pollo', NULL),
(36, 'Comino', NULL),
(37, 'Vinagre', NULL),
(38, 'Orégano', NULL),
(39, 'Perejil', NULL),
(40, 'Sal', NULL),
(41, 'Pimienta', NULL),
(42, 'Ají panca', NULL)
ON CONFLICT DO NOTHING;

INSERT INTO dishes (id, name, preparation_minutes, difficulty, popularity_score, preparation, protein_category_id) VALUES
(1, 'Arroz chaufa de pollo', 20, 'FACIL', 95,
 'Saltear el pollo, agregar huevo, arroz cocido, sillao y cebolla china. Mezclar a fuego alto y servir.', 1),
(2, 'Lomo saltado', 30, 'MEDIA', 98,
 'Sellar la carne a fuego alto, saltear cebolla y tomate, sazonar con sillao y vinagre y acompañar con papa.', 2),
(3, 'Estofado de pollo', 60, 'MEDIA', 88,
 'Dorar el pollo, preparar un aderezo con cebolla, ajo y tomate, agregar verduras y caldo y cocinar hasta ablandar.', 1),
(4, 'Ají de gallina', 60, 'MEDIA', 96,
 'Cocinar y deshilachar el pollo. Preparar la crema de ají amarillo con pan, leche y queso y mezclar con el pollo.', 1),
(5, 'Tallarines verdes con bistec', 45, 'MEDIA', 90,
 'Licuar albahaca, espinaca, leche, queso y nueces. Mezclar la salsa con los tallarines y servir con bistec.', 2),
(6, 'Arroz con pollo', 45, 'MEDIA', 94,
 'Dorar el pollo, preparar aderezo de culantro y ají amarillo, incorporar arroz, verduras y caldo y cocinar tapado.', 1),
(7, 'Papa a la huancaína', 30, 'FACIL', 92,
 'Licuar ají amarillo, queso, leche, galleta y aceite hasta formar una crema. Servir sobre papa cocida.', 4),
(8, 'Causa rellena de pollo', 45, 'MEDIA', 93,
 'Amasar papa con limón y ají amarillo. Rellenar con pollo, mayonesa, cebolla y palta y formar capas.', 1),
(9, 'Pescado a la chorrillana', 45, 'MEDIA', 86,
 'Dorar el pescado y cubrir con un salteado de cebolla, tomate, ají amarillo, ajo y limón.', 3),
(10, 'Seco de res con frejoles', 60, 'DIFICIL', 89,
 'Dorar la carne, cocinar con culantro, cebolla, ajo y ajíes hasta quedar tierna. Servir con frejoles y arroz.', 2),
(11, 'Pollo saltado', 25, 'FACIL', 84,
 'Saltear pollo, cebolla, tomate y pimiento a fuego alto. Sazonar con sillao, vinagre y ajo.', 1),
(12, 'Tortilla de verduras', 20, 'FACIL', 80,
 'Batir huevos, mezclar con verduras picadas y cocinar la tortilla por ambos lados hasta dorar.', 4)
ON CONFLICT DO NOTHING;

INSERT INTO dish_ingredients (id, dish_id, ingredient_id, quantity_text, is_required) VALUES
-- 1 Arroz chaufa de pollo: 6 ingredientes
(1, 1, 1, '250 g', true),
(2, 1, 4, '2 tazas', true),
(3, 1, 5, '2 unidades', true),
(4, 1, 6, '1/2 taza', true),
(5, 1, 7, '3 cucharadas', true),
(6, 1, 8, '2 cucharadas', true),

-- 2 Lomo saltado
(7, 2, 9, '300 g', true),
(8, 2, 10, '1 unidad', true),
(9, 2, 11, '2 unidades', true),
(10, 2, 12, '2 unidades', true),
(11, 2, 7, '2 cucharadas', true),
(12, 2, 20, '2 dientes', true),
(13, 2, 37, '1 cucharada', true),
(14, 2, 8, '2 cucharadas', true),

-- 3 Estofado de pollo: 11 ingredientes; con solo pollo la cobertura ronda 9%
(15, 3, 3, '4 piezas', true),
(16, 3, 12, '3 unidades', true),
(17, 3, 28, '1 unidad', true),
(18, 3, 29, '1/2 taza', true),
(19, 3, 10, '1 unidad', true),
(20, 3, 11, '2 unidades', true),
(21, 3, 20, '2 dientes', true),
(22, 3, 35, '2 tazas', true),
(23, 3, 36, '1 cucharadita', true),
(24, 3, 40, 'al gusto', true),
(25, 3, 41, 'al gusto', true),

-- 4 Ají de gallina
(26, 4, 2, '300 g', true),
(27, 4, 13, '3 unidades', true),
(28, 4, 14, '1 taza', true),
(29, 4, 15, '3 rebanadas', true),
(30, 4, 17, '4 unidades', true),
(31, 4, 27, '50 g', true),
(32, 4, 20, '2 dientes', true),
(33, 4, 10, '1 unidad', true),
(34, 4, 35, '1 taza', true),
(35, 4, 8, '2 cucharadas', true),

-- 5 Tallarines verdes con bistec
(36, 5, 23, '400 g', true),
(37, 5, 24, '1 taza', true),
(38, 5, 25, '1 taza', true),
(39, 5, 14, '1/2 taza', true),
(40, 5, 26, '30 g', true),
(41, 5, 27, '60 g', true),
(42, 5, 9, '300 g', true),
(43, 5, 20, '2 dientes', true),
(44, 5, 8, '2 cucharadas', true),
(45, 5, 40, 'al gusto', true),

-- 6 Arroz con pollo
(46, 6, 3, '4 piezas', true),
(47, 6, 4, '2 tazas', true),
(48, 6, 21, '1 taza', true),
(49, 6, 29, '1/2 taza', true),
(50, 6, 28, '1 unidad', true),
(51, 6, 30, '1 unidad', true),
(52, 6, 20, '2 dientes', true),
(53, 6, 35, '3 tazas', true),
(54, 6, 13, '1 unidad', true),
(55, 6, 8, '2 cucharadas', true),

-- 7 Papa a la huancaína
(56, 7, 12, '4 unidades', true),
(57, 7, 13, '3 unidades', true),
(58, 7, 14, '1/2 taza', true),
(59, 7, 16, '200 g', true),
(60, 7, 17, '6 unidades', true),
(61, 7, 8, '3 cucharadas', true),
(62, 7, 40, 'al gusto', true),

-- 8 Causa rellena de pollo
(63, 8, 12, '5 unidades', true),
(64, 8, 2, '250 g', true),
(65, 8, 13, '2 unidades', true),
(66, 8, 19, '2 unidades', true),
(67, 8, 32, '4 cucharadas', true),
(68, 8, 33, '1 unidad', true),
(69, 8, 10, '1/2 unidad', true),
(70, 8, 40, 'al gusto', true),

-- 9 Pescado a la chorrillana
(71, 9, 18, '4 filetes', true),
(72, 9, 10, '1 unidad', true),
(73, 9, 11, '2 unidades', true),
(74, 9, 13, '1 unidad', true),
(75, 9, 19, '1 unidad', true),
(76, 9, 20, '2 dientes', true),
(77, 9, 8, '2 cucharadas', true),
(78, 9, 40, 'al gusto', true),

-- 10 Seco de res con frejoles
(79, 10, 9, '400 g', true),
(80, 10, 21, '1 taza', true),
(81, 10, 22, '2 tazas', true),
(82, 10, 10, '1 unidad', true),
(83, 10, 20, '2 dientes', true),
(84, 10, 13, '1 unidad', true),
(85, 10, 42, '1 cucharada', true),
(86, 10, 36, '1 cucharadita', true),
(87, 10, 4, '2 tazas', true),
(88, 10, 40, 'al gusto', true),

-- 11 Pollo saltado
(89, 11, 2, '300 g', true),
(90, 11, 10, '1 unidad', true),
(91, 11, 11, '2 unidades', true),
(92, 11, 30, '1 unidad', true),
(93, 11, 7, '2 cucharadas', true),
(94, 11, 37, '1 cucharada', true),
(95, 11, 20, '2 dientes', true),
(96, 11, 8, '2 cucharadas', true),

-- 12 Tortilla de verduras
(97, 12, 5, '4 unidades', true),
(98, 12, 28, '1 unidad', true),
(99, 12, 29, '1/2 taza', true),
(100, 12, 30, '1 unidad', true),
(101, 12, 10, '1/2 unidad', true),
(102, 12, 39, '2 cucharadas', true),
(103, 12, 8, '1 cucharada', true),
(104, 12, 40, 'al gusto', true)
ON CONFLICT DO NOTHING;

-- Keep PostgreSQL identity sequences ahead of the explicit seed IDs.
SELECT setval(pg_get_serial_sequence('protein_categories', 'id'), COALESCE((SELECT MAX(id) FROM protein_categories), 1), true);
SELECT setval(pg_get_serial_sequence('ingredients', 'id'), COALESCE((SELECT MAX(id) FROM ingredients), 1), true);
SELECT setval(pg_get_serial_sequence('dishes', 'id'), COALESCE((SELECT MAX(id) FROM dishes), 1), true);
SELECT setval(pg_get_serial_sequence('dish_ingredients', 'id'), COALESCE((SELECT MAX(id) FROM dish_ingredients), 1), true);
