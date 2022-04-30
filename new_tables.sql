CREATE TABLE provider (
    provider_id NUMBER(10) PRIMARY KEY,
    provider_name VARCHAR(128) NOT NULL,
    provider_address VARCHAR(128) NOT NULL,
    products VARCHAR(512) NOT NULL
);

CREATE TABLE request (
    request_id NUMBER(10) PRIMARY KEY,
    provider_id NUMBER(10) NOT NULL,
    CONSTRAINT provider_id FOREIGN KEY (provider_id) REFERENCES provider(provider_id)
);

CREATE TABLE medicament_type (
    medicament_type_id NUMBER(10) PRIMARY KEY,
    type_name VARCHAR(128) NOT NULL
);

CREATE TABLE patient (
    patient_id NUMBER(10) PRIMARY KEY,
    patient_firstname VARCHAR(128) NOT NULL ,
    patient_surname VARCHAR(128) NOT NULL ,
    patient_birthdate DATE,
    patient_phone_number VARCHAR(11) UNIQUE NOT NULL ,
    patient_address VARCHAR(128) NOT NULL,
    registration_date DATE DEFAULT sysdate,
    CONSTRAINT patient_birthdate CHECK (patient_birthdate < registration_date)
);

CREATE TABLE doctor (
    doctor_id NUMBER(10) PRIMARY KEY,
    doctor_firstname VARCHAR(128) NOT NULL ,
    doctor_surname VARCHAR(128) NOT NULL
);

CREATE TABLE prescription (
    prescription_id NUMBER(10) PRIMARY KEY,
    number_of_medicines NUMBER(10),
    direction_for_use VARCHAR(128),
    diagnosis VARCHAR(128) NOT NULL,
    doctor_id NUMBER(10) NOT NULL,
    patient_id NUMBER(10) NOT NULL,
    CONSTRAINT patient_id FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    CONSTRAINT doctor_id FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

CREATE TABLE medicament (
    medicament_id NUMBER(10) PRIMARY KEY,
    title VARCHAR(128) NOT NULL ,
    usage VARCHAR(128) NOT NULL ,
    production_time NUMBER(10),
    CONSTRAINT production_time CHECK (production_time > 1),
    shelf_life DATE NOT NULL ,
    type_id NUMBER(10) NOT NULL,
    CONSTRAINT type_id FOREIGN KEY (type_id) REFERENCES medicament_type(medicament_type_id),
    prescription_id NUMBER(10),
    CONSTRAINT prescription_id FOREIGN KEY (prescription_id) REFERENCES prescription(prescription_id)
);

CREATE TABLE storage (
    storage_id NUMBER(10) PRIMARY KEY,
    medicament_id NUMBER(10) NOT NULL,
    critical_rate NUMBER(10) NOT NULL ,
    actual_balance NUMBER(10) NOT NULL ,
    price NUMERIC(10, 2) NOT NULL ,
    CONSTRAINT medicament_id FOREIGN KEY (medicament_id) REFERENCES medicament(medicament_id)
);

CREATE TABLE request_structure (
    request_structure_id NUMBER(10) PRIMARY KEY,
    med_id NUMBER(10) NOT NULL,
    CONSTRAINT med_id FOREIGN KEY (med_id) REFERENCES medicament(medicament_id),
    request_id NUMBER(10) NOT NULL,
    CONSTRAINT request_id FOREIGN KEY (request_id) REFERENCES request(request_id),
    amount NUMBER(10) NOT NULL
);

CREATE TABLE preparation_technology (
    preparation_technology_id NUMBER(10) PRIMARY KEY,
    medic_id NUMBER(10) NOT NULL,
    CONSTRAINT medic_id FOREIGN KEY (medic_id) REFERENCES medicament(medicament_id),
    method_of_preparation VARCHAR(128) NOT NULL
);

CREATE TABLE exam (
    pat_id NUMBER(10),
    doc_id NUMBER(10),
    CONSTRAINT pat_id FOREIGN KEY (pat_id) REFERENCES patient(patient_id),
    CONSTRAINT doc_id FOREIGN KEY (doc_id) REFERENCES doctor(doctor_id),
    CONSTRAINT exam_id PRIMARY KEY (pat_id, doc_id)
);

CREATE TABLE pharmacy_employee (
    pharmacy_employee_id NUMBER(10) PRIMARY KEY,
    pharmacy_employee_firstname VARCHAR(128) NOT NULL ,
    pharmacy_employee_surname VARCHAR(128) NOT NULL
);

CREATE TABLE order_ (
    order_id NUMBER(10) PRIMARY KEY,
    structure VARCHAR(128) NOT NULL ,
    is_ready VARCHAR(128) NOT NULL ,
    CONSTRAINT is_ready CHECK (is_ready IN('YES', 'NO')),
    is_received VARCHAR(128) NOT NULL, 
    CONSTRAINT is_received CHECK (is_received IN('YES', 'NO')),
    start_date DATE DEFAULT sysdate
);

CREATE TABLE making_an_order (
    order_id NUMBER(10),
    CONSTRAINT order_id FOREIGN KEY (order_id) REFERENCES order_(order_id),
    pharmacy_employee_id NUMBER(10),
    CONSTRAINT pharmacy_employee_id FOREIGN KEY (pharmacy_employee_id) REFERENCES pharmacy_employee(pharmacy_employee_id),
    patn_id NUMBER(10),
    CONSTRAINT patn_id FOREIGN KEY (patn_id) REFERENCES patient(patient_id),
    CONSTRAINT making_an_order_id PRIMARY KEY (order_id, pharmacy_employee_id, patn_id)
)
