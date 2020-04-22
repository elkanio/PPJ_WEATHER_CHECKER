CREATE TABLE states
(
    id_state INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    PRIMARY KEY (id_state)
);
CREATE TABLE cities
(
	id_city INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    id_state INT NOT NULL,
    PRIMARY KEY (id_city)

);
ALTER TABLE cities ADD FOREIGN KEY (id_state) REFERENCES states (id_state);
CREATE INDEX fk_city_state_id ON cities (id_state);