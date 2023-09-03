---FULL TRANSFER

delete from eg_wf_matrix where objecttype = 'PropertyMutation' and additionalrule='FULL TRANSFER';

INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'NEW', NULL, 'Assistant Approval Pending', 'Senior Assistant,Junior Assistant', 'FULL TRANSFER', 'Assistant Approved', 'Ready For Payment', 'Commissioner', 'Assistant Approved', 'Save', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Assistant Approved', NULL, 'Ready for payment', 'Commissioner', 'FULL TRANSFER', 'Transfer Fee Collected', 'Registration Pending', 'Commissioner', 'Commissioner Approved', 'Reject', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Transfer Fee Collected', NULL, 'Registration Pending', 'Commissioner', 'FULL TRANSFER', 'Registration Completed', 'Commissioner Approval Pending', 'Commissioner', 'Digitally Signed', 'Reject', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Commissioner Approved', NULL, 'Digital Signature Pending', 'Commissioner', 'FULL TRANSFER', 'Digitally Signed', 'Transfer Notice Print Pending', NULL, NULL, 'Preview,Sign', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Digitally Signed', NULL, 'Transfer Notice Print Pending', 'Senior Assistant,Junior Assistant', 'FULL TRANSFER', 'END', 'END', NULL, NULL, 'Generate Title Transfer Notice', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus, validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation', 'Created', NULL, NULL, 'NULL', 'FULL TRANSFER', 'NEW', 'Assistant approval pending', 'Senior Assistant,Junior Assistant', 'Assistant Approved', 'Forward', NULL, NULL, '2015-04-01', '2099-04-01');
INSERT INTO eg_wf_matrix (id, department, objecttype, currentstate, currentstatus, pendingactions, currentdesignation, additionalrule, nextstate, nextaction, nextdesignation, nextstatus,  validactions, fromqty, toqty, fromdate, todate) VALUES (NEXTVAL('seq_eg_wf_matrix'), 'ANY', 'PropertyMutation',  'Registration Completed', NULL, 'Commissioner Approval Pending', 'Commissioner', 'FULL TRANSFER', 'Commissioner Approved','Digital Signature Pending', 'Commissioner', 'Digitally Signed', 'Approve', NULL, NULL, '2015-04-01', '2099-04-01');



