INSERT INTO TRIGGER(ID, ENABLED, INPUT, INPUT_TYPE, ACTION_TYPE, CRON) VALUES ('734b1ce9-05eb-46b8-8d87-50cdbc4bbce1', 0, 'SELECT * FROM TRIGGER;', 0, 0,  '0,30 * * * * *');
INSERT INTO TRIGGER(ID, ENABLED, INPUT, INPUT_TYPE, ACTION_TYPE, CRON) VALUES ('734b1ce9-05eb-46b8-8d87-50cdbc4bbce2', 0, 'Hello Cron!', 2, 1,  '*/10 * * * * *');


INSERT INTO TEMPLATE(ID, TEMPLATE) VALUES (1, '{"name": "Test1","owner": "me","visibility": {"type": "PUBLIC","value": null},"sections": [{"name": "Section 1","type": "REPORT","query": "SELECT ID, INPUT, CRON FROM TRIGGER;","columns": [{"name": "ID","type": "TEXT"},{"name": "VALUE","type": "TEXT"},{"name": "CRON","type": "TEXT"}],"report": {"dimensions": ["COLUMN_NAME"],"facts": ["VALUE"],"defaultFilters": [{"column": "COLUMN_NAME","operator": "EQUAL","value": "TEST"}]},"dataGrid": null,"params": null},{"name": "Section 2","type": "DATA_GRID","query": "SELECT ID, INPUT, CRON FROM TRIGGER;","columns": [{"name": "ID","type": "TEXT"},{"name": "INPUT","type": "TEXT"},{"name": "CRON","type": "TEXT"}],"report": null,"dataGrid": {"actions": [{"name": "Log it","action": "LOG","url": "ID[${ID}] CRON[${CRON}] SQL[${SQL}]","type": null,"headers": null,"data": [{"ID": "${ID}"},{"SQL": "${INPUT}"},{"CRON": "${CRON}"}]}]},"params": null}]}');




COMMIT;