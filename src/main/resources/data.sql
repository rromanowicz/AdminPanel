INSERT INTO TRIGGER(ID, ENABLED, INPUT, INPUT_TYPE, ACTION_TYPE, CRON) VALUES ('734b1ce9-05eb-46b8-8d87-50cdbc4bbce1', 0, 'SELECT * FROM TRIGGER;', 0, 0,  '0,30 * * * * *');
INSERT INTO TRIGGER(ID, ENABLED, INPUT, INPUT_TYPE, ACTION_TYPE, CRON) VALUES ('734b1ce9-05eb-46b8-8d87-50cdbc4bbce2', 0, 'Hello Cron!', 2, 1,  '*/10 * * * * *');
COMMIT;