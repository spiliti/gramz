
insert into EGCL_BANKACCOUNTSERVICEMAPPING(ID,SERVICEDETAILS,BANKACCOUNT,DEPARTMENT,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,ECSTYPE, FROMDATE, TODATE) values(NEXTVAL('SEQ_EGCL_BANKACCOUNTSERVICEMAPPING'),(select id from egcl_servicedetails where code='PT'),(select id from bankaccount where glcodeid=(select id from chartofaccounts where glcode='4504204')),(select id from eg_department where code = 'R'),(select id from eg_user where username='egovernments'),current_timestamp, (select id from eg_user where username='egovernments'),current_timestamp,null, '01-apr-2000', '31-mar-2099');

insert into EGCL_BANKACCOUNTSERVICEMAPPING(ID,SERVICEDETAILS,BANKACCOUNT,DEPARTMENT,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,ECSTYPE, FROMDATE, TODATE) values(NEXTVAL('SEQ_EGCL_BANKACCOUNTSERVICEMAPPING'),(select id from egcl_servicedetails where code='WT'),(select id from bankaccount where glcodeid=(select id from chartofaccounts where glcode='4504204')),(select id from eg_department where code = 'R'),(select id from eg_user where username='egovernments'),current_timestamp, (select id from eg_user where username='egovernments'),current_timestamp,null, '01-apr-2000', '31-mar-2099');

