INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'ISVOUCHERCREATIONONRECEIPTANDSTATUSDISPLAY','Service details master display voucher on receipt creation and status field',0, (select id from eg_module where name='Collection')); 
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='ISVOUCHERCREATIONONRECEIPTANDSTATUSDISPLAY'),current_date, 'N',0);