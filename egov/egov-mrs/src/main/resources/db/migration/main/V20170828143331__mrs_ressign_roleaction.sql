INSERT INTO eg_appconfig (id,key_name,description,version,createdby,lastmodifiedby,createddate,lastmodifieddate,module) values (nextval('SEQ_EG_APPCONFIG'),'REASSIGN_BUTTONENABLED','Key to enable/disable reassign button for mrs', 0, (select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_module where name='Marriage Registration'));

INSERT INTO eg_appconfig_values (id, key_id, effective_from, createdby,lastmodifiedby,createddate,lastmodifieddate,value, version) values (nextval('SEQ_EG_APPCONFIG_VALUES'), (select id from eg_appconfig where key_name = 'REASSIGN_BUTTONENABLED' and module = (select id from eg_module where name = 'Marriage Registration')),now(), (select id from eg_user where username='egovernments'),(select id from eg_user where username='egovernments'),now(),now(), 'YES', 0);

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, "version", createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'),'ReassignMarriageRegistration','/reassignmrs', NULL, (SELECT id FROM eg_module WHERE name = 'MR-Transactions'), (select max(ordernumber)+1 from eg_module WHERE name ='Marriage Registration'), 'Reassign For Marriage Registration', false, 'mrs', 0, 1, now(), 1, now(), (select id from eg_module where name='Marriage Registration'));

INSERT INTO EG_ROLEACTION (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Marriage Registration Approver'), (select id from eg_action where name='ReassignMarriageRegistration'));

update eg_action set displayname ='ReIssue Marriage Certificate' where  url='/registration/reissuecertificate' and application=(select id from eg_module where name='Marriage Registration');