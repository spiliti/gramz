-------------------------Updating PROPERTY TRANSFER to REGISTERED TRANSFER---------------------------------------------

update eg_wf_matrix set additionalrule = 'REGISTERED TRANSFER' where objecttype = 'PropertyMutation' and additionalrule = 'PROPERTY TRANSFER';

------------------------------------FULL TRANSFER Work Flow Matrix-----------------------------------------------------

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'NEW', NULL, NULL, 'Senior Assistant,Junior Assistant', 'FULL TRANSFER', 'Assistant Approved', 'Ready For Payment', 'Commissioner', 'Assistant Approved', 'Save', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Assistant Approved', NULL, NULL, 'Commissioner', 'FULL TRANSFER', 'Transfer Fee Collected', 'Commissioner Approval Pending', 'Commissioner', 'Commissioner Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Transfer Fee Collected', NULL, NULL, 'Commissioner', 'FULL TRANSFER', 'Commissioner Approved', 'Digital Signature Pending', 'Commissioner', 'Digitally Signed', 'Approve', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Commissioner Approved', NULL, NULL, 'Commissioner', 'FULL TRANSFER', 'Digitally Signed', 'Transfer Notice Print Pending', NULL, NULL, 'Preview,Sign', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Digitally Signed', NULL, NULL, 'Senior Assistant,Junior Assistant', 'FULL TRANSFER', 'END', 'END', NULL, NULL, 'Generate Title Transfer Notice', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Rejected', NULL, NULL, 'Senior Assistant,Junior Assistant', 'FULL TRANSFER', 'Assistant Approved', 'Commissioner Approval Pending', 'Commissioner', NULL, 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Created', NULL, NULL, 'NULL', 'FULL TRANSFER', 'NEW', 'Assistant approval pending', 'Senior Assistant,Junior Assistant', 'Assistant Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');

--------------------------------------Sample Data for Mutation Fee Collection------------------------------------------


INSERT INTO EGPT_MUTATION_FEE_DETAILS (ID,LOW_LIMIT,HIGH_LIMIT,FLAT_AMOUNT,PERCENTAGE,IS_RECURSIVE,RECURSIVE_FACTOR,RECURSIVE_AMOUNT,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) VALUES (nextval('SEQ_EGPT_MUTATION_FEE_DETAILS'),1, 50000, 750, NULL,'N',NULL,NULL, current_timestamp,current_timestamp,1, 1);
INSERT INTO EGPT_MUTATION_FEE_DETAILS (ID,LOW_LIMIT,HIGH_LIMIT,FLAT_AMOUNT,PERCENTAGE,IS_RECURSIVE,RECURSIVE_FACTOR,RECURSIVE_AMOUNT,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) VALUES (nextval('SEQ_EGPT_MUTATION_FEE_DETAILS'),50001, 100000, 1250, NULL,'N',NULL,NULL, current_timestamp,current_timestamp,1, 1);
INSERT INTO EGPT_MUTATION_FEE_DETAILS (ID,LOW_LIMIT,HIGH_LIMIT,FLAT_AMOUNT,PERCENTAGE,IS_RECURSIVE,RECURSIVE_FACTOR,RECURSIVE_AMOUNT,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) VALUES (nextval('SEQ_EGPT_MUTATION_FEE_DETAILS'),100001, 500000, 2250, NULL,'N',NULL,NULL, current_timestamp,current_timestamp,1, 1);
INSERT INTO EGPT_MUTATION_FEE_DETAILS (ID,LOW_LIMIT,HIGH_LIMIT,FLAT_AMOUNT,PERCENTAGE,IS_RECURSIVE,RECURSIVE_FACTOR,RECURSIVE_AMOUNT,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) VALUES (nextval('SEQ_EGPT_MUTATION_FEE_DETAILS'),500001, 1000000, 3750, NULL,'N',NULL,NULL, current_timestamp,current_timestamp,1, 1);
INSERT INTO EGPT_MUTATION_FEE_DETAILS (ID,LOW_LIMIT,HIGH_LIMIT,FLAT_AMOUNT,PERCENTAGE,IS_RECURSIVE,RECURSIVE_FACTOR,RECURSIVE_AMOUNT,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) VALUES (nextval('SEQ_EGPT_MUTATION_FEE_DETAILS'),1000001, 2000000, 5250, NULL,'N',NULL,NULL, current_timestamp,current_timestamp,1, 1);
INSERT INTO EGPT_MUTATION_FEE_DETAILS (ID,LOW_LIMIT,HIGH_LIMIT,FLAT_AMOUNT,PERCENTAGE,IS_RECURSIVE,RECURSIVE_FACTOR,RECURSIVE_AMOUNT,CREATEDDATE,LASTMODIFIEDDATE,CREATEDBY,LASTMODIFIEDBY) VALUES (nextval('SEQ_EGPT_MUTATION_FEE_DETAILS'),2000001, NULL, 10250, NULL,'N',NULL,NULL, current_timestamp,current_timestamp,1, 1);
