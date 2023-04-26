CREATE TABLE IF NOT EXISTS T_TRIGGER(
    ID VARCHAR(50) PRIMARY KEY,
    ENV NUMBER,
    INPUT TEXT,
    INPUT_TYPE NUMBER,
    ACTION_TYPE NUMBER,
    CRON VARCHAR(50),
    ENABLED NUMBER,
    CYCLIC NUMBER
);

--CREATE SEQUENCE "TRIGGER_SEQ" MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 50 START WITH 99 NOCACHE NOCYCLE;

CREATE TABLE IF NOT EXISTS T_TEMPLATE(
    ID NUMBER PRIMARY KEY,
    ACTIVE NUMBER,
    TEMPLATE_JSON TEXT
);

CREATE SEQUENCE "T_TEMPLATE_SEQ" MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 50 START WITH 99 NOCACHE NOCYCLE;


CREATE TABLE IF NOT EXISTS T_USER(
    ID NUMBER PRIMARY KEY,
    USERNAME VARCHAR(50) UNIQUE,
    PASSWORD VARCHAR(150),
    EMAIL VARCHAR(50),
    ACTIVE NUMBER
);

CREATE TABLE IF NOT EXISTS T_ROLES(
    ID NUMBER,
    ROLES NUMBER,
    FOREIGN KEY (ID) REFERENCES T_USER(ID)
);

CREATE SEQUENCE "T_USER_SEQ" MINVALUE 1 MAXVALUE 999999999 INCREMENT BY 50 START WITH 99 NOCACHE NOCYCLE;



drop ALIAS if exists TO_DATE;
CREATE ALIAS TO_DATE as '
import java.text.*;
@CODE
java.util.Date toDate(String s, String dateFormat) throws Exception {
  return new SimpleDateFormat(dateFormat).parse(s);
}
'