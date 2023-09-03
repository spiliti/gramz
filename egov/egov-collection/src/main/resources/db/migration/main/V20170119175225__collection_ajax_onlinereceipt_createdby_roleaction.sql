Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'ajaxOnlineReceiptCreatedByList','/receipts/ajaxReceiptCreate-ajaxOnlineReceiptCreatedByList',null,(select id from eg_module where name='Receipt Services'),1,'ajaxOnlineReceiptCreatedByList',false,'collection',0,1,now(),1,now(),(select id from eg_module where name='Collection'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='ajaxOnlineReceiptCreatedByList'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='ajaxOnlineReceiptCreatedByList'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Remitter'),(select id from eg_action where name='ajaxOnlineReceiptCreatedByList'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='ajaxOnlineReceiptCreatedByList'));

