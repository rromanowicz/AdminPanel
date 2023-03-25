CREATE TABLE IF NOT EXISTS DB_TRIGGER(
    ID INT PRIMARY KEY,
    QUERY TEXT,
    TYPE VARCHAR(255),
    TRIGGER VARCHAR(255),
    ENABLED INT
);

CREATE SEQUENCE "DB_TRIGGER_SEQ" MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 50 START WITH 99 NOCACHE NOCYCLE;

insert into DB_TRIGGER(id, enabled, query, type, trigger) values (1, 1, 'select * from dual;', 'test',  'test');
insert into DB_TRIGGER(id, enabled, query, type, trigger) values (2, 1, 'select * from dual;', 'test',  'test');
COMMIT;
--select 1;