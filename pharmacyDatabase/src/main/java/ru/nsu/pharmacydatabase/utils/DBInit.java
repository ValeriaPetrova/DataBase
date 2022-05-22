package ru.nsu.pharmacydatabase.utils;

import ru.nsu.pharmacydatabase.utils.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DBInit {
    private static final String[] tableNamesArray = {
                    "Doctor.sql",
                    "Exam.sql",
                    "Making_an_order.sql",
                    "Medicament.sql",
                    "Medicament_type.sql",
                    "Order_.sql",
                    "Patient.sql",
                    "Pharmacy_employee.sql",
                    "Preparation_technology.sql",
                    "Prescription.sql",
                    "Provider.sql",
                    "Request.sql",
                    "Request_structure.sql",
                    "Storage.sql"
            };

    private final Connection connection;
    private final List<String> tablesName;

    public DBInit(Connection connection) {
        this.connection = connection;
        tablesName = new LinkedList<>();
        tablesName.addAll(Arrays.asList(tableNamesArray));
    }

    public void clear() {
        dropTables();
        dropSequences();
    }

    public void init() throws SQLException {
        System.out.println("..creating table..");
        createTables();
        createTriggers();
        System.out.println("..creating sequences..");
        createSequences();
        insertInfo();
        createProcedure();
        System.out.println("..adding base information..");
        System.out.println("-----database successfully created-----");
    }

    public static int getIdFrom(String item) {
        Integer id = Integer.valueOf(getSubstring(" ID=", "ID=", item));
        return id.intValue();
    }

    public static String getSubstring(String start1, String start2, String item) {
        String start = start1;
        int substringStartIndex = item.indexOf(start);
        if (substringStartIndex < 0) {
            start = start2;
            substringStartIndex = item.indexOf(start);
        }
        int endIndex = item.indexOf(',', substringStartIndex);
        if (endIndex < 0) {
            endIndex = item.indexOf('}', substringStartIndex);
        }
        return item.substring(substringStartIndex + start.length(), endIndex);
    }

    private void execute(List<String> queries) {
        for (String query: queries) {
            try {
                connection.executeQuery(query);
            } catch (SQLIntegrityConstraintViolationException ignored) {
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTriggers() throws SQLException {
        String trigger1 = "CREATE OR REPLACE TRIGGER types " +
                "before INSERT ON medicament_type " +
                "FOR each row " +
                "BEGIN  " +
                "SELECT sq_medicament_type.NEXTVAL  " +
                "INTO :new.medicament_type_id  FROM dual; " +
                "END;";
        connection.executeQuery(trigger1);
        String trigger2 = "CREATE OR REPLACE TRIGGER delete_doctor " +
                "BEFORE DELETE " +
                "ON prescription " +
                "FOR EACH ROW " +
                "BEGIN " +
                "DELETE from doctor " +
                "WHERE prescription.doctor_id =:OLD.doctor_id; " +
                "END";
        connection.executeQuery(trigger2);
    }

    public void createProcedure() throws SQLException {
        String sql = "create or replace procedure get_const_values( " +
                "min_date out date, " +
                "max_date out date, " +
                "default_date out date " +
                ") " +
                "is " +
                "begin " +
                "min_date := to_date('01-01-1800', 'dd-mm-yyyy'); " +
                "max_date := to_date('01-01-4021', 'dd-mm-yyyy'); " +
                "default_date := sysdate; " +
                "end;";
        connection.executeQuery(sql);
        String sql1 = "create or replace procedure get_min_time( " +
                "min_date out date " +
                ") " +
                "is " +
                "begin " +
                " min_date := to_date('01-01-1800', 'dd-mm-yyyy'); " +
                " end;";
        connection.executeQuery(sql1);
    }

    public void createIndex(String tableName, String columnName, String indexName) {
        PreparedStatement preparedStatement = null;
        String sqlDropTable = "CREATE INDEX " + indexName +
                " ON " + tableName + " (" + columnName + ") ";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlDropTable);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
            System.err.println("can't create index ");
        }
    }

    public void dropTables() {
        dropTable("making_an_order");
        dropTable("order_");
        dropTable("pharmacy_employee");
        dropTable("exam");
        dropTable("preparation_technology");
        dropTable("request_structure");
        dropTable("storage");
        dropTable("medicament");
        dropTable("prescription");
        dropTable("doctor");
        dropTable("patient");
        dropTable("medicament_type");
        dropTable("request");
        dropTable("provider");
    }

    public void dropTable(String tableName) {
        PreparedStatement preparedStatement = null;
        String sqlDropTable = "DROP TABLE " + tableName;
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlDropTable);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
            System.err.println("can't drop " + tableName + " table");
        }
    }

    public void dropSequences() {
        dropSeq("sq_making_an_order");
        dropSeq("sq_order");
        dropSeq("sq_pharmacy_employee");
        dropSeq("sq_exam");
        dropSeq("sq_preparation_technology");
        dropSeq("sq_request_structure");
        dropSeq("sq_storage");
        dropSeq("sq_medicament");
        dropSeq("sq_prescription");
        dropSeq("sq_doctor");
        dropSeq("sq_patient");
        dropSeq("sq_medicament_type");
        dropSeq("sq_request");
        dropSeq("sq_provider");
    }

    public void dropSeq(String seqName) {
        String sqlDropSeq = "DROP SEQUENCE " + seqName;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlDropSeq);
            preparedStatement.executeUpdate();
        } catch (SQLException ignored) {
            System.err.println("can't drop " + seqName + " sequence");
        }
    }

    public void createTables() {
        PreparedStatement preparedStatement = null;

        String sqlCreateProviderTable = "CREATE TABLE provider ( " +
                "provider_id NUMBER(10) PRIMARY KEY, " +
                "provider_name VARCHAR(128) NOT NULL, " +
                "provider_address VARCHAR(128) NOT NULL, " +
                "products VARCHAR(512) NOT NULL " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateProviderTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create provider table");
            throwables.printStackTrace();
        }

        String sqlCreateRequestTable = "CREATE TABLE request ( " +
                "request_id NUMBER(10) PRIMARY KEY, " +
                "provider_id NUMBER(10) NOT NULL, " +
                "CONSTRAINT provider_id FOREIGN KEY (provider_id) REFERENCES provider(provider_id) " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateRequestTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create request table");
            throwables.printStackTrace();
        }

        String sqlCreateMedicamentTypeTable = "CREATE TABLE medicament_type ( " +
                "medicament_type_id NUMBER(10) PRIMARY KEY, " +
                "type_name VARCHAR(128) NOT NULL " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateMedicamentTypeTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create medicament_type table");
            throwables.printStackTrace();
        }

        String sqlCreatePatientTable = "CREATE TABLE patient ( " +
                "patient_id NUMBER(10) PRIMARY KEY, " +
                "patient_firstname VARCHAR(128) NOT NULL , " +
                "patient_surname VARCHAR(128) NOT NULL , " +
                "patient_birthdate DATE, " +
                "patient_phone_number VARCHAR(11) UNIQUE NOT NULL , " +
                "patient_address VARCHAR(128) NOT NULL, " +
                "registration_date DATE DEFAULT sysdate, " +
                "CONSTRAINT patient_birthdate CHECK (patient_birthdate < registration_date) " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreatePatientTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create patient table");
            throwables.printStackTrace();
        }

        String sqlCreateDoctorTable = "CREATE TABLE doctor ( " +
                "doctor_id NUMBER(10) PRIMARY KEY, " +
                "doctor_firstname VARCHAR(128) NOT NULL , " +
                "doctor_surname VARCHAR(128) NOT NULL " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateDoctorTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create doctor table");
            throwables.printStackTrace();
        }

        String sqlCreatePrescriptionTable = "CREATE TABLE prescription ( " +
                "prescription_id NUMBER(10) PRIMARY KEY, " +
                "number_of_medicines NUMBER(10), " +
                "direction_for_use VARCHAR(128), " +
                "diagnosis VARCHAR(128) NOT NULL, " +
                "doctor_id NUMBER(10) NOT NULL, " +
                "patient_id NUMBER(10) NOT NULL, " +
                "CONSTRAINT patient_id FOREIGN KEY (patient_id) REFERENCES patient(patient_id), " +
                "CONSTRAINT doctor_id FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id) " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreatePrescriptionTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create prescription table");
            throwables.printStackTrace();
        }

        String sqlCreateMedicamentTable = "CREATE TABLE medicament ( " +
                "medicament_id NUMBER(10) PRIMARY KEY, " +
                "title VARCHAR(128) NOT NULL , " +
                "usage VARCHAR(128) NOT NULL , " +
                "production_time NUMBER(10), " +
                "CONSTRAINT production_time CHECK (production_time > 1), " +
                "shelf_life DATE NOT NULL , " +
                "type_id NUMBER(10) NOT NULL, " +
                "CONSTRAINT type_id FOREIGN KEY (type_id) REFERENCES medicament_type(medicament_type_id), " +
                "prescription_id NUMBER(10), " +
                "CONSTRAINT prescription_id FOREIGN KEY (prescription_id) REFERENCES prescription(prescription_id) " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateMedicamentTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create medicament table");
            throwables.printStackTrace();
        }

        String sqlCreateStorageTable = "CREATE TABLE storage ( " +
                "storage_id NUMBER(10) PRIMARY KEY, " +
                "medicament_id NUMBER(10) NOT NULL, " +
                "critical_rate NUMBER(10) NOT NULL ," +
                "actual_balance NUMBER(10) NOT NULL , " +
                "price NUMERIC(10, 2) NOT NULL , " +
                "CONSTRAINT medicament_id FOREIGN KEY (medicament_id) REFERENCES medicament(medicament_id) " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateStorageTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create storage table");
            throwables.printStackTrace();
        }

        String sqlCreateRequestStructureTable = "CREATE TABLE request_structure ( " +
                "request_structure_id NUMBER(10) PRIMARY KEY, " +
                "med_id NUMBER(10) NOT NULL, " +
                "CONSTRAINT med_id FOREIGN KEY (med_id) REFERENCES medicament(medicament_id), " +
                "request_id NUMBER(10) NOT NULL, " +
                "CONSTRAINT request_id FOREIGN KEY (request_id) REFERENCES request(request_id), " +
                "amount NUMBER(10) NOT NULL " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateRequestStructureTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create request_structure table");
            throwables.printStackTrace();
        }

        String sqlCreatePreparationTechnologyTable = "CREATE TABLE preparation_technology ( " +
                "preparation_technology_id NUMBER(10) PRIMARY KEY, " +
                "medic_id NUMBER(10) NOT NULL, " +
                "CONSTRAINT medic_id FOREIGN KEY (medic_id) REFERENCES medicament(medicament_id), " +
                "method_of_preparation VARCHAR(128) NOT NULL " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreatePreparationTechnologyTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create preparation_technology table");
            throwables.printStackTrace();
        }

        String sqlCreateExamTable = "CREATE TABLE exam ( " +
                "pat_id NUMBER(10), " +
                "doc_id NUMBER(10), " +
                "CONSTRAINT pat_id FOREIGN KEY (pat_id) REFERENCES patient(patient_id), " +
                "CONSTRAINT doc_id FOREIGN KEY (doc_id) REFERENCES doctor(doctor_id), " +
                "CONSTRAINT exam_id PRIMARY KEY (pat_id, doc_id) " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateExamTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create exam table");
            throwables.printStackTrace();
        }

        String sqlCreatePharmacyEmployeeTable = "CREATE TABLE pharmacy_employee ( " +
                "pharmacy_employee_id NUMBER(10) PRIMARY KEY, " +
                "pharmacy_employee_firstname VARCHAR(128) NOT NULL , " +
                "pharmacy_employee_surname VARCHAR(128) NOT NULL " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreatePharmacyEmployeeTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create pharmacy_employee table");
            throwables.printStackTrace();
        }

        String sqlCreateOrderTable = "CREATE TABLE order_ ( " +
                "order_id NUMBER(10) PRIMARY KEY, " +
                "medicam_id NUMBER(10) NOT NULL , " +
                "CONSTRAINT medicam_id FOREIGN KEY (medicam_id) REFERENCES medicament(medicament_id), " +
                "is_ready VARCHAR(128) NOT NULL , " +
                "CONSTRAINT is_ready CHECK (is_ready IN('YES', 'NO')), " +
                "is_received VARCHAR(128) NOT NULL,  " +
                "CONSTRAINT is_received CHECK (is_received IN('YES', 'NO')), " +
                "start_date DATE DEFAULT sysdate " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateOrderTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create order_ table");
            throwables.printStackTrace();
        }

        String sqlCreateMakingAnOrderTable = "CREATE TABLE making_an_order ( " +
                "order_id NUMBER(10), " +
                "CONSTRAINT order_id FOREIGN KEY (order_id) REFERENCES order_(order_id), " +
                "pharmacy_employee_id NUMBER(10), " +
                "CONSTRAINT pharmacy_employee_id FOREIGN KEY (pharmacy_employee_id) REFERENCES pharmacy_employee(pharmacy_employee_id), " +
                "patn_id NUMBER(10), " +
                "CONSTRAINT patn_id FOREIGN KEY (patn_id) REFERENCES patient(patient_id), " +
                "CONSTRAINT making_an_order_id PRIMARY KEY (order_id, pharmacy_employee_id, patn_id) " +
                ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlCreateMakingAnOrderTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't create making_an_order table");
            throwables.printStackTrace();
        }
    }

    public void createSequences() throws SQLException {
        String sqlCreateProviderSequence = "CREATE SEQUENCE sq_provider " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE ";
        String sqlCreateRequestSequence = "CREATE SEQUENCE sq_request " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreateMedicamentTypeSequence = "CREATE SEQUENCE sq_medicament_type " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreatePatientSequence = "CREATE SEQUENCE sq_patient " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreateDoctorSequence = "CREATE SEQUENCE sq_doctor " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreatePrescriptionSequence = "CREATE SEQUENCE sq_prescription " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreateMedicamentSequence = "CREATE SEQUENCE sq_medicament " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreateStorageSequence = "CREATE SEQUENCE sq_storage " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreateRequestStructureSequence = "CREATE SEQUENCE sq_request_structure " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreatePreparationTechnologySequence = "CREATE SEQUENCE sq_preparation_technology " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreateExamSequence = "CREATE SEQUENCE sq_exam " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreatePharmacyEmployeeSequence = "CREATE SEQUENCE sq_pharmacy_employee " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreateOrderSequence = "CREATE SEQUENCE sq_order " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        String sqlCreateMakingAnOrderSequence = "CREATE SEQUENCE sq_making_an_order " +
                "MINVALUE 1 " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOMAXVALUE";
        PreparedStatement provider = connection.getConnection().prepareStatement(sqlCreateProviderSequence);
        PreparedStatement request = connection.getConnection().prepareStatement(sqlCreateRequestSequence);
        PreparedStatement medicamentType = connection.getConnection().prepareStatement(sqlCreateMedicamentTypeSequence);
        PreparedStatement patient = connection.getConnection().prepareStatement(sqlCreatePatientSequence);
        PreparedStatement doctor = connection.getConnection().prepareStatement(sqlCreateDoctorSequence);
        PreparedStatement prescription = connection.getConnection().prepareStatement(sqlCreatePrescriptionSequence);
        PreparedStatement medicament = connection.getConnection().prepareStatement(sqlCreateMedicamentSequence);
        PreparedStatement storage = connection.getConnection().prepareStatement(sqlCreateStorageSequence);
        PreparedStatement requestStructure = connection.getConnection().prepareStatement(sqlCreateRequestStructureSequence);
        PreparedStatement preparationTechnology = connection.getConnection().prepareStatement(sqlCreatePreparationTechnologySequence);
        PreparedStatement exam = connection.getConnection().prepareStatement(sqlCreateExamSequence);
        PreparedStatement pharmacyEmployee = connection.getConnection().prepareStatement(sqlCreatePharmacyEmployeeSequence);
        PreparedStatement order = connection.getConnection().prepareStatement(sqlCreateOrderSequence);
        PreparedStatement makingAnOrder = connection.getConnection().prepareStatement(sqlCreateMakingAnOrderSequence);

        provider.executeUpdate();
        request.executeUpdate();
        medicamentType.executeUpdate();
        patient.executeUpdate();
        doctor.executeUpdate();
        prescription.executeUpdate();
        medicament.executeUpdate();
        storage.executeUpdate();
        requestStructure.executeUpdate();
        preparationTechnology.executeUpdate();
        exam.executeUpdate();
        pharmacyEmployee.executeUpdate();
        order.executeUpdate();
        makingAnOrder.executeUpdate();
    }

    public void insertInfo() {
        insertProvider("Главфарм", "Московская, 25", "Абуцел, Авиа-море, Агри, Адаптол, Адвантан, Аевит, Бронхикум, Виброцил, Диазолин");
        insertProvider("Биоритм", "Черемуховая, 15", "Кромицил, Димедрол, Преднизолон, Лоратадин, Давзолин");
        insertProvider("Интермедсервис", "Малая Калужская 15/16", "ПАРАЦЕТ, ПАЦЕТАМОЛ АВЕКСИМА, ПАРАЦЕТАМОЛ ВЕЛФАРМ, ПАРАЦЕТАМОЛ ДЕТСКИЙ, Декстрометорфан, ПАРАЦЕТАМОЛ МЕДИСОРБ");
        insertProvider("Гарант", "Морской проспект, 22", "Пабал, Панкреатин, Назол, Найсулид, Найз, Нанопласт, Бронхиред");
        insertProvider("Медицина Альба", "Лунная, 19", "КАВИНТАЗОЛ, КАВИНТОН, КАВИНТОН КОМФОРТЕ, КАВИНТОН ФОРТЕ, КАВИТ ЮНИОР, КАГОЦЕЛ, КАДМИУМ МЕТАЛЛИКУМ, КАДМИУМ СУЛЬФУРИКУМ");

        insertRequest(1);
        insertRequest(3);
        insertRequest(5);
        insertRequest(1);
        insertRequest(2);
        insertRequest(3);
        insertRequest(4);
        insertRequest(4);
        insertRequest(3);

        insertMedicamentType("Аллергия");
        insertMedicamentType("Витимины");
        insertMedicamentType("Дерматология");
        insertMedicamentType("ЖКТ");
        insertMedicamentType("Нервная система");
        insertMedicamentType("Обезболивающие");
        insertMedicamentType("Противовирусные");
        insertMedicamentType("Простуда и грипп");

        insertPatient("Василий", "Иванов", "16-12-1995", "81936481735", "Пирогова, 19", "17-01-2022");
        insertPatient("Анастасия", "Кудина", "16-10-1989", "88295610821", "Брестская, 9", "20-04-2022");
        insertPatient("Александра", "Баранова", "19-05-1999", "81209067890", "Ленина, 15", "29-04-2022");
        insertPatient("Николай", "Ремезов", "06-01-2000", "88295618201", "Морская, 91", "30-03-2022");
        insertPatient("Петр", "Дворников", "16-08-1960", "89125618201", "Терешкова, 29", "18-03-2022");
        insertPatient("Василиса", "Сидникова", "26-08-1989", "89128163201", "Пирога, 15", "28-04-2022");
        insertPatient("Иван", "Шишкин", "17-09-1998", "81234567890", "Комсомольская, 4", "28-04-2022");

        insertDoctor("Борис", "Петров");
        insertDoctor("Кирилл", "Лукомский");
        insertDoctor("Евгений", "Бочаров");
        insertDoctor("Марина", "Александрова");
        insertDoctor("Вероника", "Дым");

        insertPrescription(2, "после еды, внутрь", "ангина", 1, 1);
        insertPrescription(1, "для наружнего применения", "ушиб", 2, 4);
        insertPrescription(1, "для наружнего применения", "ушиб", 2, 2);
        insertPrescription(3, "во время еды, внутрь", "аллергия", 3, 3);
        insertPrescription(2, "для наружнего применения", "порез", 4, 5);

        insertMedicament("Назол", "внутрь", 36, "01-01-2025", 8, 1);
        insertMedicament("Аевит", "внутрь", 24, "01-10-2024", 2, 5);
        insertMedicament("Пантенол", "для наружнего применения", 72, "01-10-2026", 3, 5);
        insertMedicament("Граммидин", "внутрь", 72, "01-01-2024", 8, 1);
        insertMedicament("Найз", "для наружнего применения", 72, "01-01-2023", 6, 2);
        insertMedicament("Найз", "для наружнего применения", 72, "01-01-2023", 6, 3);
        insertMedicament("Фестал", "внутрь", 72, "31-01-2024", 4, 4);
        insertMedicament("Бепантен", "для наружнего применения", 32, "01-01-2025", 3, 3);
        insertMedicament("Зодак", "для наружнего применения", 32, "01-01-2025", 1, 5);
        insertMedicament("Супрастин", "внутрь", 72, "01-01-2024", 1, 4);
        insertMedicament("Супрастин", "внутрь", 72, "01-01-2024", 1, 5);
        insertMedicament("Зодак", "для наружнего применения", 32, "01-01-2025", 1, 2);
        insertMedicament("Кларитин", "для наружнего применения", 32, "01-01-2025", 1, 5);

        insertStorage(1, 10, 6, 711.0);
        insertStorage(2, 15, 0, 191.0);
        insertStorage(3, 13, 18, 891.0);
        insertStorage(4, 10, 20, 789.0);
        insertStorage(5, 10, 10, 509.0);
        insertStorage(6, 10, 11, 98.0);
        insertStorage(7, 15, 9, 100.0);

        insertRequestStructure(1, 1, 10);
        insertRequestStructure(2, 4, 10);

        insertPreparationTechnology(1, "технология приготовления 1");
        insertPreparationTechnology(2, "технология приготовления 2");
        insertPreparationTechnology(3, "технология приготовления 3");
        insertPreparationTechnology(4, "технология приготовления 4");
        insertPreparationTechnology(5, "технология приготовления 5");

        insertExam(1, 1);
        insertExam(5, 4);
        insertExam(6, 5);
        insertExam(4, 2);
        insertExam(2, 2);
        insertExam(2, 1);
        insertExam(3, 3);
        insertExam(7, 3);

        insertPharmacyEmployee("Мария", "Константинова");

        insertOrder(1, "YES", "NO", "28-04-2022");
        insertOrder(2, "NO", "NO", "20-04-2022");
        insertOrder(3, "NO", "YES", "25-04-2022");
        insertOrder(4, "YES", "YES", "25-04-2022");

        insertMakingAnOrder(1, 1);
        insertMakingAnOrder(1, 2);
        insertMakingAnOrder(1, 3);
        insertMakingAnOrder(1, 4);
    }

    public void insertProvider (String provider_name, String provider_address, String products) {
        PreparedStatement preparedStatement = null;
        String sqlInsertProviderTable = "INSERT INTO provider " +
                "VALUES (sq_provider.NEXTVAL, '" + provider_name + "', '" + provider_address + "', '" +  products + "')";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertProviderTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into provider table");
            throwables.printStackTrace();
        }
    }

    public void insertRequest(int providerId) {
        PreparedStatement preparedStatement = null;
        String sqlInsertRequestTable = "INSERT INTO request " +
                "VALUES (sq_request.NEXTVAL, " + providerId +")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertRequestTable);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into request table");
            throwables.printStackTrace();
        }
    }

    public void  insertMedicamentType(String typeName) {
        PreparedStatement preparedStatement = null;
        String sqlInsertMedicamentType = "INSERT INTO medicament_type(type_name) " +
                "VALUES ('" + typeName + "')";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertMedicamentType);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into medicament_type table");
            throwables.printStackTrace();
        }
    }

    public void insertPatient(String name, String surname, String birthDate, String phoneNumber, String address, String regDate) {
        PreparedStatement preparedStatement = null;
        birthDate = "to_date('"+ birthDate + "', 'DD-MM-YYYY')";
        regDate = "to_date('"+ regDate + "', 'DD-MM-YYYY')";
        String sqlInsertPatient = "INSERT INTO patient " +
                "VALUES (sq_patient.NEXTVAL, '" + name + "', '" + surname + "', " + birthDate + ", '" + phoneNumber + "', '" + address + "', " + regDate + ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertPatient);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into patient table");
            throwables.printStackTrace();
        }
    }

    public void insertDoctor(String name, String surname) {
        PreparedStatement preparedStatement = null;
        String sqlInsertDoctor = "INSERT INTO doctor " +
                "VALUES (sq_doctor.NEXTVAL, '" + name + "', '" + surname + "')";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertDoctor);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into doctor table");
            throwables.printStackTrace();
        }
    }

    public void insertPrescription(int count, String usage, String diagnosis, int doctorId, int patientId) {
        PreparedStatement preparedStatement = null;
        String sqlInsertPrescription = "INSERT INTO prescription " +
                "VALUES (sq_prescription.NEXTVAL, "+  count + ", '" +usage + "', '" + diagnosis + "', " + doctorId + ", " + patientId + ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertPrescription);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into prescription table");
            throwables.printStackTrace();
        }
    }

    public void insertMedicament(String title, String usage, int prodTime, String shelfLife, int typeId, int prescriptionId) {
        PreparedStatement preparedStatement = null;
        shelfLife = "to_date('"+ shelfLife + "', 'DD-MM-YYYY')";
        String sqlInsertMedicament = "INSERT INTO medicament " +
                "VALUES (sq_medicament.NEXTVAL, '" + title + "', '" + usage + "', " + prodTime + ", " + shelfLife + ", " + typeId + ", " + prescriptionId + ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertMedicament);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into medicament table");
            throwables.printStackTrace();
        }
    }

    public void insertStorage(int medicamentId, int criticalRate, int actualBalance, double price) {
        PreparedStatement preparedStatement = null;
        String sqlInsertStorage = "INSERT INTO storage " +
                "VALUES (sq_storage.NEXTVAL, " + medicamentId + ", " + criticalRate + ", " + actualBalance + ", " + price + ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertStorage);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into storage table");
            throwables.printStackTrace();
        }
    }

    public void insertRequestStructure(int medicamentId, int requestId, int amount) {
        PreparedStatement preparedStatement = null;
        String sqlInsertRequestStructure = "INSERT INTO request_structure " +
                "VALUES (sq_request_structure.NEXTVAL, " + medicamentId + ", " + requestId + ", " + amount + ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertRequestStructure);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into request_structure table");
            throwables.printStackTrace();
        }
    }

    public void insertPreparationTechnology(int medicamentId, String method) {
        PreparedStatement preparedStatement = null;
        String sqlInsertPreparationTechnology = "INSERT INTO preparation_technology " +
                "VALUES (sq_preparation_technology.NEXTVAL, " + medicamentId + ", '" + method + "')";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertPreparationTechnology);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into preparation_technology table");
            throwables.printStackTrace();
        }
    }

    public void insertExam(int patientId, int doctorId) {
        PreparedStatement preparedStatement = null;
        String sqlInsertExam = "INSERT INTO exam " +
                "VALUES (" + patientId + ", " + doctorId + ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertExam);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into exam table");
            throwables.printStackTrace();
        }
    }

    public void insertPharmacyEmployee(String name, String surname) {
        PreparedStatement preparedStatement = null;
        String sqlInsertPharmacyEmployee = "INSERT INTO pharmacy_employee " +
                "VALUES (sq_pharmacy_employee.NEXTVAL, '" + name + "', '" + surname + "')";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertPharmacyEmployee);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into pharmacy_employee table");
            throwables.printStackTrace();
        }
    }

    public void insertOrder(int medicamentId, String isReady, String isReceived, String startDate) {
        PreparedStatement preparedStatement = null;
        startDate = "to_date('"+ startDate + "', 'DD-MM-YYYY')";
        String sqlInsertOrder = "INSERT INTO order_ " +
                "VALUES (sq_order.NEXTVAL, " + medicamentId + ", '" + isReady + "', '" + isReceived + "', " + startDate + ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertOrder);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into order table");
            throwables.printStackTrace();
        }
    }

    public void insertMakingAnOrder(int pharmacyEmployeeId, int patientId) {
        PreparedStatement preparedStatement = null;
        String sqlInsertMakingAnOrder = "INSERT INTO making_an_order " +
                "VALUES (sq_making_an_order.NEXTVAL, " + pharmacyEmployeeId + ", " + patientId + ")";
        try {
            preparedStatement = connection.getConnection().prepareStatement(sqlInsertMakingAnOrder);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            System.out.println("can't insert into making_an_order table");
            throwables.printStackTrace();
        }
    }

    public void updateProvider(int id, String provider_name, String provider_address, String products) {
        String sql = "UPDATE provider SET " +
                "provider_name = '" + provider_name + "', provider_address = '" + provider_address + "', products='" + products + "' " +
                "WHERE provider_id = "+ id;
        List<String> provider = new LinkedList<>();
        provider.add(sql);
        connection.insert(provider);
        System.out.println("UPDATE provider");
    }

    public void updateRequest(int id, int providerId) {
        String sql = "UPDATE request SET " +
                "provider_id = " + providerId +
                " WHERE request_id = "+ id;
        List<String> request = new LinkedList<>();
        request.add(sql);
        connection.insert(request);
        System.out.println("UPDATE request");
    }

    public void updateMedicamentType(int id, String typeName) {
        String sql = "UPDATE medicament_type SET " +
                "type_name = '" + typeName + "' " +
                " WHERE medicament_type_id = "+ id;
        List<String> medicamentType = new LinkedList<>();
        medicamentType.add(sql);
        connection.insert(medicamentType);
        System.out.println("UPDATE medType");
    }

    public void updatePatient(int id, String name, String surname, String birthDate, String phoneNumber, String address, String regDate) {
        birthDate = "to_date('"+ birthDate + "', 'DD-MM-YYYY')";
        regDate = "to_date('"+ regDate + "', 'DD-MM-YYYY')";
        String sql = "UPDATE patient SET " +
                "patient_firstname = '" + name + "', patient_surname = '" + surname + "', patient_birthdate=" + birthDate + ", patient_phone_number='" + phoneNumber + "', patient_address='" + address + "', registration_date="  + regDate +
                " WHERE patient_id = " + id;
        List<String> patient = new LinkedList<>();
        patient.add(sql);
        connection.insert(patient);
        System.out.println("UPDATE patient");
    }

    public void updateDoctor(int id, String name, String surname) {
        String sql = "UPDATE doctor SET " +
                "doctor_firstname = '" + name + "', doctor_surname = '" + surname + "' " +
                " WHERE doctor_id = " + id;
        List<String> doctor = new LinkedList<>();
        doctor.add(sql);
        connection.insert(doctor);
        System.out.println("UPDATE doctor");
    }

    public void updateOrder(int id, int medId, String isReady, String isReceived, String startDate) {
        startDate = "to_date('"+ startDate + "', 'DD-MM-YYYY')";
        String sql = "UPDATE order_ SET " +
                "medicam_id = " + medId + ", is_ready = '" + isReady + "', is_received= '" + isReceived + "', start_date =" + startDate +
                " WHERE order_id = " + id;
        List<String> doctor = new LinkedList<>();
        doctor.add(sql);
        connection.insert(doctor);
        System.out.println("UPDATE doctor");
    }

    public void updatePrescription(int id, int count, String usage, String diagnosis, int doctorId, int patientId) {
        String sql = "UPDATE prescription SET " +
                "number_of_medicines = " + count + ", direction_for_use = '" + usage + "', diagnosis= '" + diagnosis + "', doctor_id =" + doctorId + ", patient_id =" + patientId +
                " WHERE prescription_id = " + id;
        List<String> doctor = new LinkedList<>();
        doctor.add(sql);
        connection.insert(doctor);
        System.out.println("UPDATE doctor");
    }
}