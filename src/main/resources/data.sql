INSERT INTO TRIGGER(ID, ENABLED, INPUT, INPUT_TYPE, ACTION_TYPE, CRON) VALUES ('734b1ce9-05eb-46b8-8d87-50cdbc4bbce1', 0, 'SELECT * FROM TRIGGER;', 0, 0,  '0,30 * * * * *');
INSERT INTO TRIGGER(ID, ENABLED, INPUT, INPUT_TYPE, ACTION_TYPE, CRON) VALUES ('734b1ce9-05eb-46b8-8d87-50cdbc4bbce2', 0, 'Hello Cron!', 2, 1,  '*/10 * * * * *');


--INSERT INTO TEMPLATE(ID, TEMPLATE) VALUES (1, '{"name": "Test1","owner": "me","visibility": {"type": "PUBLIC","value": null},"sections": [{"name": "Section 1","type": "REPORT","query": "SELECT ID, INPUT, CRON FROM TRIGGER;","columns": [{"name": "ID","type": "TEXT"},{"name": "VALUE","type": "TEXT"},{"name": "CRON","type": "TEXT"}],"report": {"dimensions": ["COLUMN_NAME"],"facts": ["VALUE"],"defaultFilters": [{"column": "COLUMN_NAME","operator": "EQUAL","value": "TEST"}]},"dataGrid": null,"params": null},{"name": "Section 2","type": "DATA_GRID","query": "SELECT ID, INPUT, CRON FROM TRIGGER;","columns": [{"name": "ID","type": "TEXT"},{"name": "INPUT","type": "TEXT"},{"name": "CRON","type": "TEXT"}],"report": null,"dataGrid": {"actions": [{"name": "Log it","action": "LOG","url": "ID[${ID}] CRON[${CRON}] SQL[${SQL}]","type": null,"headers": null,"data": [{"ID": "ID"},{"SQL": "INPUT"},{"CRON": "CRON"}]}]},"params": null}]}');
--INSERT INTO TEMPLATE(ID, TEMPLATE) VALUES (2, '{"name": "Test2","owner": "me","visibility": {"type": "PUBLIC","value": null},"sections": [{"name": "Section 1","type": "REPORT","query": "SELECT ID, INPUT, CRON FROM TRIGGER;","columns": [{"name": "ID","type": "TEXT"},{"name": "VALUE","type": "TEXT"},{"name": "CRON","type": "TEXT"}],"report": {"dimensions": ["COLUMN_NAME"],"facts": ["VALUE"],"defaultFilters": [{"column": "COLUMN_NAME","operator": "EQUAL","value": "TEST"}]},"dataGrid": null,"params": null},{"name": "Section 2","type": "DATA_GRID","query": "SELECT ID, INPUT, CRON FROM TRIGGER;","columns": [{"name": "ID","type": "TEXT"},{"name": "INPUT","type": "TEXT"},{"name": "CRON","type": "TEXT"}],"report": null,"dataGrid": {"actions": [{"name": "Log it","action": "LOG","url": "ID[${ID}] CRON[${CRON}] SQL[${SQL}]","type": null,"headers": null,"data": [{"ID": "ID"},{"SQL": "INPUT"},{"CRON": "CRON"}]},{"name": "Also Log it","action": "LOG","url": "SecondButton/${ID}/somethingSomething","type": null,"headers": null,"data": [{"ID": "ID"}]}]},"params": null}]}');
INSERT INTO TEMPLATE(ID, TEMPLATE) VALUES (3, '{"name": "SOME_TABLE Report","owner": "me","visibility": {"type": "PUBLIC","value": null},"globalFilters": [{"column": "SALES_DATE","operator": "LESS_THAN_OR_EQUAL","value": "TODAY"}],"sections": [{"name": "Section 1","type": "REPORT","query": "SELECT ID, SALES_DATE, QUANTITY, PRICE FROM SOME_TABLE;","columns": [{"name": "ID","type": "NUMBER"},{"name": "SALES_DATE","type": "DATE"},{"name": "QUANTITY","type": "NUMBER"},{"name": "PRICE","type": "NUMBER"}],"report": {"dimensions": ["ID","SALES_DATE"],"facts": ["QUANTITY","PRICE"],"initialReport": {"dimensions": ["SALES_DATE"],"facts": ["QUANTITY","PRICE"]}},"dataGrid": null,"params": null}]}');



INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-01', 1, 25.2);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-02', 4, 66.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-03', 3, 46.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-03', 3, 46.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100034, '2023-04-04', 5, 78.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-05', 3, 46.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-05', 3, 46.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100034, '2023-04-05', 5, 78.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-06', 1, 16.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-06', 3, 45.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100034, '2023-04-06', 2, 22.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-07', 5, 76.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-07', 2, 25.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100034, '2023-04-07', 3, 52.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-08', 1, 25.2);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-08', 4, 66.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-09', 3, 46.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-09', 3, 46.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100034, '2023-04-09', 5, 78.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-10', 3, 46.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-10', 3, 46.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100034, '2023-04-10', 5, 78.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-11', 1, 16.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-11', 3, 45.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100034, '2023-04-12', 2, 22.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100032, '2023-04-12', 5, 76.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100033, '2023-04-13', 2, 25.6);
INSERT INTO SOME_TABLE(ID, SALES_DATE, QUANTITY, PRICE) VALUES (100034, '2023-04-13', 3, 52.6);

COMMIT;