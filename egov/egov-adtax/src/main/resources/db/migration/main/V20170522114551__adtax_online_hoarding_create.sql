
INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'CitizenHoardingCreate', '/hoarding/createbycitizen', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Citizen Create Hoarding', true, 'adtax', 0, 1, now(), 1, now(), (select id from eg_module where name='Advertisement Tax' and parentmodule is null));


INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'AdtaxCitizenSupport', '/citizen/search', NULL, (select id from eg_module where name='AdvertisementTaxTransactions'), 1, 'Adtax Citizen Support', true, 'adtax', 0, 1, now(), 1, now(), (select id from eg_module where name='Advertisement Tax' and parentmodule is null));