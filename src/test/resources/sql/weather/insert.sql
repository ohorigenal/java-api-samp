DELETE FROM WEATHER;
DELETE FROM LOCATION;

INSERT INTO LOCATION(id, city) VALUES
(1, '東京'),
(2, '四谷');

INSERT INTO WEATHER
(dat, weather, location,location_id, comment) VALUES
('20210101', 1, 1, 1, 'comment for test'),
('20210102', 2, 2, 2, 'comment2 for test');
