Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'ProjectCode','Created',now(),'CREATED',1);
Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (NEXTVAL('SEQ_EGW_STATUS'),'ProjectCode','Closed',now(),'CLOSED',2);

--rollback delete from EGW_STATUS where MODULETYPE='ProjectCode';