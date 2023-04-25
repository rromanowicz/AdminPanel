INSERT INTO T_TRIGGER(ID, ENABLED, INPUT, INPUT_TYPE, ACTION_TYPE, CRON) VALUES ('734b1ce9-05eb-46b8-8d87-50cdbc4bbce1', 0, 'SELECT * FROM TRIGGER;', 0, 0,  '0,30 * * * * *');
INSERT INTO T_TRIGGER(ID, ENABLED, INPUT, INPUT_TYPE, ACTION_TYPE, CRON) VALUES ('734b1ce9-05eb-46b8-8d87-50cdbc4bbce2', 0, 'Hello Cron!', 2, 1,  '*/10 * * * * *');

INSERT INTO T_TEMPLATE(ID, ACTIVE, TEMPLATE_JSON) VALUES (3, 1, '{"name": "SOME_TABLE Report","owner": "me","visibility": {"type": "PUBLIC","value": null},"globalFilters": [{"column": "SALES_DATE","operator": "GREATER_THAN_OR_EQUAL","value": "TODAY"}],"sections": [{"name": "Section 1","type": "REPORT","query": "SELECT ID, SALES_DATE, QUANTITY, PRICE FROM SOME_TABLE;","columns": [{"name": "ID","type": "NUMBER"},{"name": "SALES_DATE","type": "DATE"},{"name": "QUANTITY","type": "NUMBER"},{"name": "PRICE","type": "NUMBER"}],"report": {"dimensions": ["ID","SALES_DATE"],"facts": ["QUANTITY","PRICE"],"initialReport": {"dimensions": ["SALES_DATE"],"facts": ["QUANTITY","PRICE"]}},"dataGrid": null,"params": null}]}');
INSERT INTO T_TEMPLATE(ID, ACTIVE, TEMPLATE_JSON) VALUES (4, 1, '{"name": "SOME_TABLE Report 2","owner": "me","visibility": {"type": "PUBLIC","value": null},"globalFilters": [{"column": "SALES_DATE","operator": "GREATER_THAN_OR_EQUAL","value": "TODAY"}],"sections": [{"name": "Section 1","type": "REPORT","query": "SELECT ID, SALES_DATE, QUANTITY, PRICE FROM SOME_TABLE;","columns": [{"name": "ID","type": "NUMBER"},{"name": "SALES_DATE","type": "DATE"},{"name": "QUANTITY","type": "NUMBER"},{"name": "PRICE","type": "NUMBER"}],"report": {"dimensions": ["ID","SALES_DATE"],"facts": ["QUANTITY","PRICE"],"initialReport": {"dimensions": ["SALES_DATE"],"facts": ["QUANTITY","PRICE"]}},"dataGrid": null,"params": null},{"name": "Section 2","type": "DATA_GRID","query": "SELECT ID, SALES_DATE, QUANTITY FROM SOME_TABLE;","columns": [{"name": "ID","type": "NUMBER"},{"name": "SALES_DATE","type": "DATE"},{"name": "QUANTITY","type": "NUMBER"}],"report": null,"dataGrid": {"actions": [{"name": "Log it","action": "LOG","url": "ID[${ID}] DATE[${DT}] QTY[${QTY}]","type": null,"headers": null,"data": [{"ID": "ID"},{"DT": "SALES_DATE"},{"QTY": "QUANTITY"}]},{"name": "Also Log it","action": "LOG","url": "https://localhost:8080/get/${ID}/delete","type": null,"headers": null,"data": [{"ID": "ID"}]}]},"params": null}]}');

INSERT INTO T_USER (ID, USERNAME, PASSWORD, EMAIL, ACTIVE) VALUES (1, 'admin', '8d6a1354bd0ce789f8b1205f569d6f619e683fd455e5efdf573acffe2d518098', null, 1);
INSERT INTO T_USER (ID, USERNAME, PASSWORD, EMAIL, ACTIVE) VALUES (2, 'reports', '8d6a1354bd0ce789f8b1205f569d6f619e683fd455e5efdf573acffe2d518098', null, 1);
INSERT INTO T_USER (ID, USERNAME, PASSWORD, EMAIL, ACTIVE) VALUES (3, 'user', '8d6a1354bd0ce789f8b1205f569d6f619e683fd455e5efdf573acffe2d518098', null, 1);

INSERT INTO T_ROLES(ID, ROLES) VALUES (1, 1);
INSERT INTO T_ROLES(ID, ROLES) VALUES (1, 0);
INSERT INTO T_ROLES(ID, ROLES) VALUES (2, 0);
INSERT INTO T_ROLES(ID, ROLES) VALUES (2, 2);
INSERT INTO T_ROLES(ID, ROLES) VALUES (3, 0);

