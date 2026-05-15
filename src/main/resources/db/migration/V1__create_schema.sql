CREATE TABLE vets (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  first_name VARCHAR(30),
  last_name VARCHAR(30)
);
CREATE INDEX vets_last_name ON vets (last_name);

CREATE TABLE specialties (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(80)
);
CREATE INDEX specialties_name ON specialties (name);

CREATE TABLE vet_specialties (
  vet_id INTEGER NOT NULL,
  specialty_id INTEGER NOT NULL,
  PRIMARY KEY (vet_id, specialty_id),
  CONSTRAINT fk_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vets (id),
  CONSTRAINT fk_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id)
);

CREATE TABLE types (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(80)
);
CREATE INDEX types_name ON types (name);

CREATE TABLE owners (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  first_name VARCHAR(30),
  last_name VARCHAR(30) COLLATE NOCASE,
  address VARCHAR(255),
  city VARCHAR(80),
  telephone VARCHAR(20),
  email VARCHAR(255) NOT NULL
);
CREATE INDEX owners_last_name ON owners (last_name);

CREATE TABLE pets (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(30),
  birth_date DATE,
  type_id INTEGER NOT NULL,
  owner_id INTEGER,
  CONSTRAINT fk_pets_owners FOREIGN KEY (owner_id) REFERENCES owners (id),
  CONSTRAINT fk_pets_types FOREIGN KEY (type_id) REFERENCES types (id)
);
CREATE INDEX pets_name ON pets (name);

CREATE TABLE visits (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  pet_id INTEGER,
  visit_date DATE,
  description VARCHAR(255),
  CONSTRAINT fk_visits_pets FOREIGN KEY (pet_id) REFERENCES pets (id)
);
CREATE INDEX visits_pet_id ON visits (pet_id);
