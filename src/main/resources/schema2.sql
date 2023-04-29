CREATE TABLE IF NOT EXISTS SOME_TABLE(
    ID NUMBER,
    SALES_DATE DATE,
    QUANTITY NUMBER,
    PRICE NUMBER
);

CREATE OR REPLACE VIEW V_SOME_TABLE AS (SELECT * FROM SOME_TABLE);

drop ALIAS if exists TO_DATE;
CREATE ALIAS TO_DATE as '
import java.text.*;
@CODE
java.util.Date toDate(String s, String dateFormat) throws Exception {
  return new SimpleDateFormat(dateFormat).parse(s);
}
'