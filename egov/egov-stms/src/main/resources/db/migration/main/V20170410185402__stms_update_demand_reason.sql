update EG_DEMAND_REASON set GLCODEID =(select id from chartofaccounts where glcode='3504199') where id_demand_reason_master in ( select id from eg_demand_reason_master where reasonmaster='SEWERAGEADVANCE' and module=(select id from eg_module where name='Sewerage Tax Management'));
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Sewerage Tax' and module=(select id from eg_module where name='Sewerage Tax Management')),inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where glcode='1404019') from eg_installment_master inst  where inst.id_module=(select id from eg_module where name='Sewerage Tax Management') and inst.financial_year='2017-18');
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Donation Charge' and module=(select id from eg_module where name='Sewerage Tax Management')),inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where glcode='1603006') from eg_installment_master inst  where inst.id_module=(select id from eg_module where name='Sewerage Tax Management') and inst.financial_year='2017-18');
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Inspection Charge' and module=(select id from eg_module where name='Sewerage Tax Management')),inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where glcode='1404019') from eg_installment_master inst  where inst.id_module=(select id from eg_module where name='Sewerage Tax Management') and inst.financial_year='2017-18');
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Estimation Charge' and module=(select id from eg_module where name='Sewerage Tax Management')),inst.id, null, null, current_timestamp, current_timestamp, (select id from chartofaccounts where glcode='1404019') from eg_installment_master inst  where inst.id_module=(select id from eg_module where name='Sewerage Tax Management') and inst.financial_year='2017-18'); 