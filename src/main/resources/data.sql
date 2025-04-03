-- Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Clear existing data
DELETE FROM seats;
DELETE FROM showtimes;
DELETE FROM screens;
DELETE FROM theaters;
DELETE FROM movies;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- =========================================
-- Insert MOVIES
-- =========================================
-- We'll have 5 total:
--   1) Dune: Part Two        (Now Playing)
--   2) Godzilla x Kong       (Now Playing)
--   3) The Fall Guy          (Now Playing)
--   4) Furiosa: A Mad Max Saga (Coming Soon)
--   5) Inside Out 2          (Coming Soon)

-- “Now Playing” release_date: set to 2023 so it's "past" for the app
-- “Coming Soon” release_date: set to 2025

INSERT INTO movies (id, title, description, duration_minutes, genre, rating, poster_image_url, release_date, trailer_url)
VALUES 
-- 1) Dune: Part Two
(1, 
 'Dune: Part Two', 
 'Epic continuation of the Dune saga. Paul Atreides unites with Chani and the Fremen against new conspirators.', 
 166, 
 'Sci-Fi, Adventure', 
 'PG-13', 
 'https://via.placeholder.com/1200x600', 
 '2023-12-01', 
 'https://youtu.be/exampleDune2'),

-- 2) Godzilla x Kong: The New Empire
(2, 
 'Godzilla x Kong: The New Empire', 
 'Epic monsterverse clash between Godzilla and Kong, with a terrifying new threat beneath the earth.', 
 135, 
 'Action, Sci-Fi', 
 'PG-13', 
 'https://via.placeholder.com/1200x600', 
 '2023-12-10', 
 'https://youtu.be/exampleGodzilla'),

-- 3) The Fall Guy
(3, 
 'The Fall Guy', 
 'Action-packed stuntman adventure with comedic twists.', 
 126, 
 'Action, Comedy', 
 'PG-13', 
 'https://via.placeholder.com/1200x600', 
 '2023-12-15', 
 'https://youtu.be/exampleFallGuy'),

-- 4) Furiosa: A Mad Max Saga (Coming Soon)
(4,
 'Furiosa: A Mad Max Saga',
 'A saga of the post-apocalyptic Wasteland, following the fierce warrior Furiosa.',
 150,
 'Action, Adventure',
 'R',
 'https://via.placeholder.com/1200x600',
 '2025-05-24',
 'https://youtu.be/exampleFuriosa'),

-- 5) Inside Out 2 (Coming Soon)
(5,
 'Inside Out 2',
 'Sequel to the beloved Pixar film, exploring new emotions and adventures inside Riley’s mind.',
 105,
 'Animation, Family',
 'PG',
 'https://via.placeholder.com/1200x600',
 '2025-06-14',
 'https://youtu.be/exampleInsideOut2');


-- =========================================
-- Insert THEATERS
-- =========================================
INSERT INTO theaters (id, name, address, city, state, postal_code, phone_number) 
VALUES
 (1, 'Cineplex Avalon Mall',   '48 Kenmount Rd',    'St. John''s', 'NL', 'A1B2C3', '123-456-7890'),
 (2, 'Cineplex Village Mall',  '430 Topsail Rd',    'St. John''s', 'NL', 'A1B2C3', '709-555-1111'),
 (3, 'Cineplex Sobeys Square', '70 Kelsey Dr',      'St. John''s', 'NL', 'A1B2C3', '709-555-2222');

-- =========================================
-- Insert SCREENS
-- =========================================
INSERT INTO screens (id, screen_number, capacity, screen_type, theater_id)
VALUES
 (1, 1, 40, 'Standard', 1),
 (2, 1, 40, 'Standard', 2),
 (3, 1, 40, 'Standard', 3);

-- =========================================
-- Insert SHOWTIMES
--   For the 3 "Now Playing" movies (1,2,3):
--   plus optional showtimes for the 2 "Coming Soon" (4,5) if you want them to appear
--   in seat picking or not. 
-- =========================================

-- DUNE: PART TWO (MOVIE_ID=1) at Avalon Mall (screen_id=1)
INSERT INTO showtimes (id, movie_id, screen_id, start_time, end_time, date, price)
VALUES
 (101, 1, 1, '2023-12-20 10:30:00', '2023-12-20 12:46:00', '2023-12-20', 15.00),
 (102, 1, 1, '2023-12-20 13:15:00', '2023-12-20 15:31:00', '2023-12-20', 15.00),
 (103, 1, 1, '2023-12-20 16:00:00', '2023-12-20 18:16:00', '2023-12-20', 15.00),
 (104, 1, 1, '2023-12-20 19:30:00', '2023-12-20 21:46:00', '2023-12-20', 15.00),
 (105, 1, 1, '2023-12-20 22:45:00', '2023-12-21 01:01:00', '2023-12-20', 15.00);

-- GODZILLA x KONG (MOVIE_ID=2) at Avalon Mall (screen_id=1)
INSERT INTO showtimes (id, movie_id, screen_id, start_time, end_time, date, price)
VALUES
 (201, 2, 1, '2023-12-20 11:00:00', '2023-12-20 13:15:00', '2023-12-20', 14.00),
 (202, 2, 1, '2023-12-20 14:30:00', '2023-12-20 16:45:00', '2023-12-20', 14.00),
 (203, 2, 1, '2023-12-20 17:45:00', '2023-12-20 20:00:00', '2023-12-20', 14.00),
 (204, 2, 1, '2023-12-20 20:15:00', '2023-12-20 22:30:00', '2023-12-20', 14.00),
 (205, 2, 1, '2023-12-20 23:00:00', '2023-12-21 01:15:00', '2023-12-20', 14.00);

-- THE FALL GUY (MOVIE_ID=3) at Avalon Mall (screen_id=1)
INSERT INTO showtimes (id, movie_id, screen_id, start_time, end_time, date, price)
VALUES
 (301, 3, 1, '2023-12-20 10:15:00', '2023-12-20 12:21:00', '2023-12-20', 13.00),
 (302, 3, 1, '2023-12-20 12:45:00', '2023-12-20 14:51:00', '2023-12-20', 13.00),
 (303, 3, 1, '2023-12-20 15:30:00', '2023-12-20 17:36:00', '2023-12-20', 13.00),
 (304, 3, 1, '2023-12-20 18:15:00', '2023-12-20 20:21:00', '2023-12-20', 13.00),
 (305, 3, 1, '2023-12-20 21:00:00', '2023-12-20 23:06:00', '2023-12-20', 13.00);


-- Showtimes at Village Mall (theater 2) or Sobeys Square (3)
-- e.g. more showtimes for Godzilla(2) or Fall Guy(3)
INSERT INTO showtimes (id, movie_id, screen_id, start_time, end_time, date, price)
VALUES
 (106, 2, 2, '2023-12-20 14:00:00', '2023-12-20 16:15:00', '2023-12-20', 14.00),
 (107, 3, 2, '2023-12-20 17:00:00', '2023-12-20 19:06:00', '2023-12-20', 13.00),
 (108, 3, 3, '2023-12-20 10:00:00', '2023-12-20 12:06:00', '2023-12-20', 13.00),
 (109, 1, 3, '2023-12-20 13:30:00', '2023-12-20 15:46:00', '2023-12-20', 15.00);

-- Optionally add showtimes for the “Coming Soon” movies (id=4, id=5) if you want them to appear in seat picking:
-- (Up to you, but here's an example for Furiosa)
INSERT INTO showtimes (id, movie_id, screen_id, start_time, end_time, date, price)
VALUES
 (401, 4, 1, '2025-05-24 13:00:00', '2025-05-24 15:30:00', '2025-05-24', 15.50),
 (402, 4, 1, '2025-05-24 19:00:00', '2025-05-24 21:30:00', '2025-05-24', 15.50);

-- Inside Out 2
INSERT INTO showtimes (id, movie_id, screen_id, start_time, end_time, date, price)
VALUES
 (501, 5, 1, '2025-06-14 11:00:00', '2025-06-14 12:45:00', '2025-06-14', 12.00),
 (502, 5, 1, '2025-06-14 14:00:00', '2025-06-14 15:45:00', '2025-06-14', 12.00);

-- =========================================
-- Insert SEATS
--   We keep your existing seat layout for screens 1,2,3
-- =========================================

-- Screen 1 seats
INSERT INTO seats (id, screen_id, row_letter, seat_number, seat_type, available)
VALUES
-- Row A
(301, 1, 'A', 1, 'Regular', true),
(302, 1, 'A', 2, 'Regular', true),
(303, 1, 'A', 3, 'Regular', true),
(304, 1, 'A', 4, 'Regular', true),
(305, 1, 'A', 5, 'Regular', true),
(306, 1, 'A', 6, 'Regular', true),
(307, 1, 'A', 7, 'Regular', true),
(308, 1, 'A', 8, 'Regular', true),

-- Row B
(309, 1, 'B', 1, 'Regular', true),
(310, 1, 'B', 2, 'Regular', true),
(311, 1, 'B', 3, 'Regular', true),
(312, 1, 'B', 4, 'Regular', true),
(313, 1, 'B', 5, 'Regular', true),
(314, 1, 'B', 6, 'Regular', true),
(315, 1, 'B', 7, 'Regular', true),
(316, 1, 'B', 8, 'Regular', true),

-- Row C
(317, 1, 'C', 1, 'Regular', true),
(318, 1, 'C', 2, 'Regular', true),
(319, 1, 'C', 3, 'Regular', true),
(320, 1, 'C', 4, 'Regular', true),
(321, 1, 'C', 5, 'Regular', true),
(322, 1, 'C', 6, 'Regular', true),
(323, 1, 'C', 7, 'Regular', true),
(324, 1, 'C', 8, 'Regular', true);

-- Screen 2 seats
INSERT INTO seats (id, screen_id, row_letter, seat_number, seat_type, available)
VALUES
(401, 2, 'A', 1, 'Regular', true),
(402, 2, 'A', 2, 'Regular', true),
(403, 2, 'A', 3, 'Regular', true),
(404, 2, 'A', 4, 'Regular', true),
(405, 2, 'A', 5, 'Regular', true),
(406, 2, 'A', 6, 'Regular', true),
(407, 2, 'A', 7, 'Regular', true),
(408, 2, 'A', 8, 'Regular', true),
(409, 2, 'B', 1, 'Regular', true),
(410, 2, 'B', 2, 'Regular', true),
(411, 2, 'B', 3, 'Regular', true),
(412, 2, 'B', 4, 'Regular', true),
(413, 2, 'B', 5, 'Regular', true),
(414, 2, 'B', 6, 'Regular', true),
(415, 2, 'B', 7, 'Regular', true),
(416, 2, 'B', 8, 'Regular', true),
(417, 2, 'C', 1, 'Regular', true),
(418, 2, 'C', 2, 'Regular', true),
(419, 2, 'C', 3, 'Regular', true),
(420, 2, 'C', 4, 'Regular', true),
(421, 2, 'C', 5, 'Regular', true),
(422, 2, 'C', 6, 'Regular', true),
(423, 2, 'C', 7, 'Regular', true),
(424, 2, 'C', 8, 'Regular', true);

-- Screen 3 seats
INSERT INTO seats (id, screen_id, row_letter, seat_number, seat_type, available)
VALUES
(425, 3, 'A', 1, 'Regular', true),
(426, 3, 'A', 2, 'Regular', true),
(427, 3, 'A', 3, 'Regular', true),
(428, 3, 'A', 4, 'Regular', true),
(429, 3, 'A', 5, 'Regular', true),
(430, 3, 'A', 6, 'Regular', true),
(431, 3, 'A', 7, 'Regular', true),
(432, 3, 'A', 8, 'Regular', true),
(433, 3, 'B', 1, 'Regular', true),
(434, 3, 'B', 2, 'Regular', true),
(435, 3, 'B', 3, 'Regular', true),
(436, 3, 'B', 4, 'Regular', true),
(437, 3, 'B', 5, 'Regular', true),
(438, 3, 'B', 6, 'Regular', true),
(439, 3, 'B', 7, 'Regular', true),
(440, 3, 'B', 8, 'Regular', true),
(441, 3, 'C', 1, 'Regular', true),
(442, 3, 'C', 2, 'Regular', true),
(443, 3, 'C', 3, 'Regular', true),
(444, 3, 'C', 4, 'Regular', true),
(445, 3, 'C', 5, 'Regular', true),
(446, 3, 'C', 6, 'Regular', true),
(447, 3, 'C', 7, 'Regular', true),
(448, 3, 'C', 8, 'Regular', true);
