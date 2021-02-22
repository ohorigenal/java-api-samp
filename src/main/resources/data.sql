CREATE TABLE IF NOT EXISTS LOCATION
(
id int AUTO_INCREMENT PRIMARY KEY,
city varchar(20) NOT NULL
);

INSERT INTO LOCATION(id, city) VALUES
(1, '新宿'),
(2, '中野');

CREATE TABLE IF NOT EXISTS WEATHER
(
dat char(8),
weather int NOT NULL,
location_id int NOT NULL,
comment varchar(255),
PRIMARY KEY(dat, location_id),
FOREIGN KEY(location_id) REFERENCES LOCATION(id)
);

INSERT INTO WEATHER
(dat, weather, location,location_id, comment) VALUES
('20200101', 1, 1, 1, 'test comment'),
('20200111', 1, 2, 2, 'test comment2');
