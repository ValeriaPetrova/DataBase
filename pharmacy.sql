CREATE TABLE provider (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    address VARCHAR(128) NOT NULL,
    products VARCHAR(512) NOT NULL
);

CREATE TABLE request (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    provider_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (provider_id) REFERENCES provider(id)
);

CREATE TABLE medicament_type (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type_name VARCHAR(128) NOT NULL
);

CREATE TABLE patient (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(128) NOT NULL ,
    birth_date DATE,
    phone_number CHAR(11) UNIQUE NOT NULL ,
    address VARCHAR(128)
);

CREATE TABLE doctor (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(128) NOT NULL
);

CREATE TABLE prescription (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    number_of_medicines BIGINT,
    direction_for_use VARCHAR(128),
    diagnosis VARCHAR(128) NOT NULL ,
    doctor_id BIGINT,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    patient_id BIGINT,
    FOREIGN KEY (patient_id) REFERENCES patient(id)
);

CREATE TABLE medicament (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(128) NOT NULL ,
    usage VARCHAR(128) NOT NULL ,
    production_time INTERVAL,
    shelf_life DATE NOT NULL ,
    critical_rate BIGINT NOT NULL ,
    actual_balance BIGINT NOT NULL ,
    price NUMERIC(10, 2) NOT NULL ,
    type_id BIGINT NOT NULL,
    FOREIGN KEY (type_id) REFERENCES medicament_type(id),
    prescription_id BIGINT,
    FOREIGN KEY (prescription_id) REFERENCES prescription(id)
);

CREATE TABLE request_structure (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    medicament_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (medicament_id) REFERENCES medicament(id),
    request_id BIGINT NOT NULL,
    FOREIGN KEY (request_id) REFERENCES request(id),
    amount BIGINT NOT NULL
);

CREATE TABLE handbook_of_drug_preparation_technologies (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    medicament_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (medicament_id) REFERENCES medicament(id),
    method_of_preparation VARCHAR(128) NOT NULL
);

CREATE TABLE exam (
    patient_id BIGINT,
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    doctor_id BIGINT,
    FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    PRIMARY KEY (patient_id, doctor_id)
);

CREATE TABLE pharmacy_employee (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(128)
);

CREATE TABLE _order (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    structure VARCHAR(128) NOT NULL ,
    is_ready BOOLEAN NOT NULL ,
    is_received BOOLEAN NOT NULL
);

CREATE TABLE making_an_order (
    order_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES _order(id),
    pharmacy_employee_id BIGINT,
    FOREIGN KEY (pharmacy_employee_id) REFERENCES pharmacy_employee(id),
    patient_id BIGINT,
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    PRIMARY KEY (order_id, pharmacy_employee_id, patient_id)
);