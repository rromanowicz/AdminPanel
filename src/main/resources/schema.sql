CREATE TABLE IF NOT EXISTS TRIGGER(
    ID VARCHAR(50) PRIMARY KEY,
    INPUT TEXT,
    INPUT_TYPE NUMBER,
    ACTION_TYPE NUMBER,
    CRON VARCHAR(50),
    ENABLED NUMBER
);

--CREATE SEQUENCE "TRIGGER_SEQ" MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 50 START WITH 99 NOCACHE NOCYCLE;

CREATE TABLE IF NOT EXISTS TEMPLATE(
    ID NUMBER PRIMARY KEY,
    ACTIVE NUMBER,
    TEMPLATE TEXT
);

--CREATE SEQUENCE "TEMPLATE_SEQ" MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 50 START WITH 99 NOCACHE NOCYCLE;


CREATE TABLE IF NOT EXISTS T_USER(
    ID NUMBER PRIMARY KEY,
    USERNAME VARCHAR(50) UNIQUE,
    PASSWORD VARCHAR(50),
    EMAIL VARCHAR(50),
    ACTIVE NUMBER
);

CREATE TABLE IF NOT EXISTS T_ROLES(
    ID NUMBER,
    ROLES NUMBER,
    FOREIGN KEY (ID) REFERENCES T_USER(ID)
);

--CREATE SEQUENCE "T_USER_SEQ" MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 50 START WITH 99 NOCACHE NOCYCLE;



CREATE TABLE IF NOT EXISTS SOME_TABLE(
    ID NUMBER,
    SALES_DATE DATE,
    QUANTITY NUMBER,
    PRICE NUMBER
);

CREATE OR REPLACE VIEW V_SOME_TABLE AS (SELECT * FROM SOME_TABLE);