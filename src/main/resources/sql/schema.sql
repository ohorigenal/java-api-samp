CREATE TABLE IF NOT EXISTS LOCATION
(
id int AUTO_INCREMENT PRIMARY KEY,
city varchar(20) NOT NULL
);


CREATE TABLE IF NOT EXISTS WEATHER
(
dat char(8),
weather int NOT NULL,
location int NOT NULL,
location_id int NOT NULL,
comment varchar(255),
PRIMARY KEY(dat, location_id),
FOREIGN KEY(location_id) REFERENCES LOCATION(id)
);
